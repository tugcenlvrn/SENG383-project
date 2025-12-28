"""
CSV data loading utilities
"""
import csv
import os
from typing import List, Dict, Optional
from pathlib import Path

from models.course import Course, CourseType
from models.instructor import Instructor
from models.schedule import DayOfWeek, TimeSlot
from datetime import time


class CSVLoader:
    """Handles loading and parsing CSV files"""
    
    @staticmethod
    def load_common_courses(file_path: str) -> List[Dict]:
        """Load common courses from CSV"""
        courses = []
        
        if not os.path.exists(file_path):
            return courses
        
        with open(file_path, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if row['Ders Kodu'] and row['Ders Kodu'] != '---':
                    courses.append(row)
        
        return courses
    
    @staticmethod
    def load_department_curriculum(file_path: str) -> List[Course]:
        """Load department curriculum from CSV and convert to Course objects"""
        courses = []
        
        if not os.path.exists(file_path):
            return courses
        
        with open(file_path, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if not row.get('Ders Kodu') or row['Ders Kodu'] == '---':
                    continue
                
                # Parse year
                year_str = row.get('Sınıf', '')
                if '1.' in year_str:
                    year = 1
                elif '2.' in year_str:
                    year = 2
                elif '3.' in year_str:
                    year = 3
                elif '4.' in year_str:
                    year = 4
                else:
                    year = 0  # Elective or service courses
                
                # Parse course type
                course_type_str = row.get('Zorunlu/Seçmeli/Servis', 'Zorunlu')
                if 'Teknik Lab Seçmeli' in course_type_str or 'Technical Lab Elective' in course_type_str:
                    course_type = CourseType.TECHNICAL_LAB_ELECTIVE
                elif 'Yüksek Lisans' in course_type_str or 'Graduate' in course_type_str:
                    course_type = CourseType.GRADUATE
                elif 'Seçmeli' in course_type_str or 'Elective' in course_type_str:
                    course_type = CourseType.ELECTIVE
                elif 'Servis' in course_type_str or 'Service' in course_type_str:
                    course_type = CourseType.SERVICE
                else:
                    course_type = CourseType.MANDATORY
                
                # Parse hours
                try:
                    theory_hours = int(row.get('Teori Saati', 0) or 0)
                except ValueError:
                    theory_hours = 0
                
                try:
                    lab_hours = int(row.get('Lab Saati', 0) or 0)
                except ValueError:
                    lab_hours = 0
                
                # Parse student count
                try:
                    student_count = int(row.get('Öğrenci Sayısı', 0) or 0)
                except ValueError:
                    student_count = 0
                
                # Parse lab sections
                try:
                    lab_sections = int(row.get('Lab Şube Sayısı', 0) or 0)
                except ValueError:
                    lab_sections = 0
                
                course = Course(
                    code=row['Ders Kodu'],
                    name=row.get('Ders Adı', ''),
                    year=year,
                    theory_hours=theory_hours,
                    lab_hours=lab_hours,
                    course_type=course_type,
                    instructor=row.get('Öğretim Elemanı', '') or None,
                    student_count=student_count,
                    lab_sections=lab_sections
                )
                
                courses.append(course)
        
        return courses
    
    @staticmethod
    def parse_time(time_str: str) -> Optional[time]:
        """Parse time string to time object"""
        try:
            parts = time_str.strip().split(':')
            if len(parts) == 2:
                return time(int(parts[0]), int(parts[1]))
        except (ValueError, AttributeError):
            pass
        return None
    
    @staticmethod
    def parse_days(day_str: str) -> List[DayOfWeek]:
        """Parse day string to list of DayOfWeek"""
        days = []
        day_str = day_str.strip()
        
        # Handle multiple days separated by /
        day_parts = [d.strip() for d in day_str.split('/')]
        
        for day_part in day_parts:
            try:
                day = DayOfWeek.from_string(day_part)
                if day not in days:
                    days.append(day)
            except (KeyError, AttributeError):
                pass
        
        return days if days else [DayOfWeek.MONDAY]
    
    @staticmethod
    def create_instructors_from_courses(courses: List[Course]) -> Dict[str, Instructor]:
        """Create instructor dictionary from courses"""
        instructors = {}
        
        for course in courses:
            if course.instructor:
                if course.instructor not in instructors:
                    instructors[course.instructor] = Instructor(name=course.instructor)
                instructors[course.instructor].add_course(course)
        
        return instructors

