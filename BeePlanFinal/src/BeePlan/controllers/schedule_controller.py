"""
BeePlan - Schedule Controller (Final Polish)
- SENG 101 Section Fix: Özel kural ile kesinlikle 2 section açar.
- PHYS 131 Label Fix: İsimden 'Lab-2'yi yakalar ve section ID'sini 2 yapar.
- Blok Lab: 2 saatlik bloklar korunur.
"""
from typing import List, Dict, Optional, Tuple
from datetime import time, datetime

from models.course import Course, CourseType
from models.instructor import Instructor
from models.schedule import Schedule, TimeSlot, DayOfWeek
from models.classroom import Classroom
from utils.time_utils import TimeUtils
from controllers.data_controller import DataController
from utils.csv_loader import CSVLoader

class ScheduleController:
    
    def __init__(self, data_controller: DataController):
        self.data_controller = data_controller
        self.classrooms = self._initialize_classrooms()
    
    def _initialize_classrooms(self) -> List[Classroom]:
        from models.classroom import Classroom, ClassroomType
        classrooms = []
        for i in range(1, 11): classrooms.append(Classroom(f"T-{i:02d}", 60, ClassroomType.THEORY))
        for i in range(1, 6): classrooms.append(Classroom(f"Lab-{i}", 40, ClassroomType.COMPUTER_LAB))
        return classrooms
    
    def generate_all_schedules(self, selected_electives: List[Course] = None) -> Dict[int, Schedule]:
        all_schedules = {}
        used_elective_codes = set()
        
        for year in [1, 2, 3, 4]:
            year_specific_electives = []
            if selected_electives:
                for elective in selected_electives:
                    if (elective.year == year or elective.year == 0) and elective.code not in used_elective_codes:
                        year_specific_electives.append(elective)
            
            schedule = self.generate_schedule(year, year_specific_electives, all_schedules)
            all_schedules[year] = schedule
            
            for slot in schedule.time_slots:
                if slot.course and slot.course.is_elective and slot.course.code != "ELEC":
                    used_elective_codes.add(slot.course.code)
        return all_schedules

    def generate_schedule(self, year: int, selected_electives: List[Course] = None, all_schedules: Dict[int, Schedule] = None) -> Schedule:
        schedule = Schedule(year)
        courses = self.data_controller.get_courses_by_year(year)
        common_courses = self.data_controller.get_common_courses_by_year(year)
        
        # 1. Ortak Dersler (Label Fix Dahil)
        self._add_common_courses(schedule, common_courses)
        
        # 2. Seçmeli Placeholder
        elec_placeholders = [c for c in self.data_controller.department_courses if c.year == year and c.code == "ELEC"]
        if selected_electives and elec_placeholders:
            count = 0
            for real_elective in selected_electives:
                if count >= len(elec_placeholders): break
                courses.append(real_elective)
                count += 1
        
        # 3. Bölüm Derslerini Planla
        self._schedule_department_courses(schedule, courses, all_schedules)
        return schedule
    
    def _schedule_department_courses(self, schedule: Schedule, courses: List[Course], all_schedules: Dict[int, Schedule]) -> None:
        priority_courses = []
        other_electives = []
        
        for c in courses:
            if c.code == "SENG 429": # VIP Ders
                priority_courses.append(c)
                continue
                
            c_type_str = str(c.course_type.value) if hasattr(c.course_type, 'value') else str(c.course_type)
            if c.code == "ELEC": continue
            
            if "Mandatory" in c_type_str or "Zorunlu" in c_type_str or "Teknik" in c_type_str:
                priority_courses.append(c)
            elif c.is_elective or "Seçmeli" in c_type_str:
                other_electives.append(c)
        
        for course in priority_courses:
            if not self._is_course_scheduled(schedule, course):
                self._schedule_course(schedule, course, all_schedules)
        
        for course in other_electives:
            if not self._is_course_scheduled(schedule, course):
                self._schedule_course(schedule, course, all_schedules)

    def _schedule_course(self, schedule: Schedule, course: Course, all_schedules: Dict[int, Schedule]) -> bool:
        if course.theory_hours > 0:
            if not self._schedule_consecutive_slots(schedule, course, course.theory_hours, is_lab=False, all_schedules=all_schedules):
                return False
        
        if course.lab_hours > 0:
            if not self._schedule_consecutive_slots(schedule, course, course.lab_hours, is_lab=True, all_schedules=all_schedules):
                return False
        return True

    def _schedule_consecutive_slots(self, schedule: Schedule, course: Course, hours: int, is_lab: bool, all_schedules: Dict[int, Schedule]) -> bool:
        days_list = TimeUtils.get_all_days()
        
        # Lab kuralı
        target_days = self._get_smart_distributed_days(schedule, course, all_schedules)
        if is_lab:
            theory_slots = [s for s in schedule.time_slots if s.course.code == course.code and not s.is_lab]
            if theory_slots:
                last_theory_day_idx = max(days_list.index(s.day) for s in theory_slots)
                target_days = [d for d in target_days if days_list.index(d) > last_theory_day_idx]
            else:
                return False

        # --- SECTION SAYISI HESAPLAMA (GÜÇLENDİRİLMİŞ) ---
        num_sections = 1
        if is_lab:
            # SENG 101 İÇİN ÖZEL KURAL (FORCE 2 SECTIONS)
            if "SENG 101" in course.code:
                num_sections = 2
            else:
                try:
                    # String temizliği yapıp int'e çevir
                    cnt = int(str(course.student_count).strip())
                    if cnt > 40:
                        num_sections = 2
                except:
                    # Veri bozuksa ve lab ise varsayılan olarak kontrol et
                    num_sections = 1
        # ---------------------------------------------------
        
        for section in range(1, num_sections + 1):
            section_scheduled = False
            for day in target_days:
                valid_slots = self._get_valid_slots_for_day(day)
                for start_idx in valid_slots:
                    if start_idx + hours > len(TimeUtils.TIME_SLOTS): continue
                    
                    is_valid_block = True
                    temp_slots = []
                    
                    for i in range(hours):
                        st, et = TimeUtils.TIME_SLOTS[start_idx + i]
                        if day == DayOfWeek.FRIDAY and self._is_friday_exam_block(st, et): is_valid_block = False; break
                        if not self._is_instructor_available(course.instructor, day, st, et, all_schedules, schedule): is_valid_block = False; break
                        
                        slot_obj = TimeSlot(day, st, et, course, is_lab=is_lab, section=section)
                        if self._has_conflict(schedule, slot_obj, all_schedules): is_valid_block = False; break
                        temp_slots.append(slot_obj)
                    
                    if is_valid_block and not is_lab:
                        if not self._check_daily_theory_limit(course.instructor, day, hours, all_schedules, schedule):
                            is_valid_block = False
                        
                    if is_valid_block:
                        for s in temp_slots: schedule.add_time_slot(s)
                        section_scheduled = True; break
                if section_scheduled: break
            if not section_scheduled: return False
        return True

    def _get_smart_distributed_days(self, schedule: Schedule, course: Course, all_schedules: Dict[int, Schedule]) -> List[DayOfWeek]:
        days = TimeUtils.get_all_days()
        rankings = []
        for day in days:
            day_slots = schedule.get_slots_by_day(day)
            student_load = len(day_slots) 
            instructor_load = 0
            if course.instructor:
                all_checks = list(all_schedules.values()) + [schedule]
                for sch in all_checks:
                    instructor_load += sum(1 for s in sch.get_slots_by_day(day) if s.course and s.course.instructor == course.instructor)
            score = (student_load * 4) + instructor_load
            rankings.append((score, day))
        rankings.sort(key=lambda x: x[0])
        return [d for _, d in rankings]

    def _is_friday_exam_block(self, start: time, end: time) -> bool:
        return not (end <= time(13, 20) or start >= time(15, 10))

    def _is_instructor_available(self, instructor: str, day: DayOfWeek, start: time, end: time, all_schedules: Dict[int, Schedule], current: Schedule) -> bool:
        if not instructor or instructor == "---": return True
        all_checks = list(all_schedules.values()) + [current]
        unique_schedules = {id(s): s for s in all_checks}.values()
        for sch in unique_schedules:
            for slot in sch.time_slots:
                if slot.course and slot.course.instructor == instructor:
                    if slot.day == day and not (end <= slot.start_time or start >= slot.end_time): return False
        return True

    def _check_daily_theory_limit(self, instructor: str, day: DayOfWeek, hours: int, all_schedules: Dict[int, Schedule], current: Schedule) -> bool:
        if not instructor or instructor == "---": return True
        total = 0
        all_checks = list(all_schedules.values()) + [current]
        unique_schedules = {id(s): s for s in all_checks}.values()
        for sch in unique_schedules:
            for slot in sch.get_slots_by_day(day):
                if slot.course and slot.course.instructor == instructor and not slot.is_lab:
                    total += 1
        return (total + hours) <= 4

    def _has_conflict(self, schedule: Schedule, new_slot: TimeSlot, all_schedules: Dict[int, Schedule]) -> bool:
        for existing in schedule.time_slots:
            if new_slot.overlaps_with(existing): return True
        if new_slot.course and (new_slot.course.is_elective or "Seçmeli" in str(new_slot.course.course_type)):
            if all_schedules:
                for sch in all_schedules.values():
                    for existing in sch.time_slots:
                        if existing.course and (existing.course.is_elective or "Seçmeli" in str(existing.course.course_type)):
                            if new_slot.overlaps_with(existing): return True
        return False

    def _add_common_courses(self, schedule: Schedule, common_courses: List[Dict]) -> None:
        """Ortak dersleri işler ve Lab-2 etiketini zorla atar."""
        from utils.csv_loader import CSVLoader
        for data in common_courses:
            code = data.get('Ders Kodu', '')
            if not code or code == '---': continue
            
            days = str(data.get('Gün', '')).split('/')
            starts = str(data.get('Başlangıç', '')).split('/')
            ends = str(data.get('Bitiş', '')).split('/')
            
            # --- SECTION BELİRLEME (HARD FIX) ---
            # Varsayılan section 1
            section = 1
            
            # Eğer ders isminde Lab-2, (2), Gr2, Grup 2 gibi ibareler varsa section 2 yap
            code_upper = code.upper()
            if "LAB-2" in code_upper or "(2)" in code or "GR2" in code_upper or "GR 2" in code_upper:
                section = 2
            # ------------------------------------

            for i, d_str in enumerate(days):
                try:
                    day = DayOfWeek.from_string(d_str.strip())
                    st_val = starts[i].strip() if i < len(starts) else starts[0].strip()
                    et_val = ends[i].strip() if i < len(ends) else ends[0].strip()
                    st = CSVLoader.parse_time(st_val)
                    et = CSVLoader.parse_time(et_val)
                    
                    if st and et:
                        course = self._find_course_by_code(code) or Course(code, code, schedule.year, 2, 0, CourseType.COMMON, None, 0, 0)
                        
                        for slot_start, slot_end in TimeUtils.TIME_SLOTS:
                            if slot_start >= st and slot_end <= et:
                                # Section bilgisini TimeSlot'a zorla yaz
                                schedule.add_time_slot(TimeSlot(day, slot_start, slot_end, course, 'Lab' in code, section=section))
                except: continue

    def _find_course_by_code(self, code: str) -> Optional[Course]:
        clean = code.split('(')[0].strip()
        for c in self.data_controller.department_courses:
            if c.code == code or c.code == clean: return c
        return None
    
    def _is_course_scheduled(self, schedule: Schedule, course: Course) -> bool:
        return any(s.course and s.course.code == course.code for s in schedule.time_slots)

    def _get_valid_slots_for_day(self, day: DayOfWeek) -> List[int]:
        valid_indices = []
        for i, (start, end) in enumerate(TimeUtils.TIME_SLOTS):
            if day == DayOfWeek.FRIDAY and self._is_friday_exam_block(start, end): continue
            valid_indices.append(i)
        return valid_indices
