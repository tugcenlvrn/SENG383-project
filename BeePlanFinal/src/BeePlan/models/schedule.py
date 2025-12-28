"""
Schedule model for time slots and scheduling
"""
from enum import Enum
from typing import Optional, List, Tuple
from dataclasses import dataclass
from datetime import time
from .course import Course


class DayOfWeek(Enum):
    """Days of the week"""
    MONDAY = "Monday"
    TUESDAY = "Tuesday"
    WEDNESDAY = "Wednesday"
    THURSDAY = "Thursday"
    FRIDAY = "Friday"
    
    @classmethod
    def from_string(cls, day_str: str) -> 'DayOfWeek':
        """Convert string to DayOfWeek enum"""
        mapping = {
            "Pazartesi": cls.MONDAY,
            "Pzt": cls.MONDAY,
            "Monday": cls.MONDAY,
            "Mon": cls.MONDAY,
            "Salı": cls.TUESDAY,
            "Tuesday": cls.TUESDAY,
            "Tue": cls.TUESDAY,
            "Çarşamba": cls.WEDNESDAY,
            "Çar": cls.WEDNESDAY,
            "Wednesday": cls.WEDNESDAY,
            "Wed": cls.WEDNESDAY,
            "Perşembe": cls.THURSDAY,
            "Per": cls.THURSDAY,
            "Thursday": cls.THURSDAY,
            "Thu": cls.THURSDAY,
            "Cuma": cls.FRIDAY,
            "Cum": cls.FRIDAY,
            "Friday": cls.FRIDAY,
            "Fri": cls.FRIDAY
        }
        return mapping.get(day_str, cls.MONDAY)
    
    def __str__(self) -> str:
        return self.value


@dataclass
class TimeSlot:
    """Represents a time slot in the schedule"""
    day: DayOfWeek
    start_time: time
    end_time: time
    course: Optional[Course] = None
    is_lab: bool = False
    section: int = 1
    classroom: Optional[str] = None
    
    def overlaps_with(self, other: 'TimeSlot') -> bool:
        """Check if this time slot overlaps with another"""
        if self.day != other.day:
            return False
        
        # Check time overlap
        return not (self.end_time <= other.start_time or self.start_time >= other.end_time)
    
    def __str__(self) -> str:
        course_str = f"{self.course.code}" if self.course else "Empty"
        lab_str = " (Lab)" if self.is_lab else ""
        return f"{self.day.value} {self.start_time.strftime('%H:%M')}-{self.end_time.strftime('%H:%M')}: {course_str}{lab_str}"


class Schedule:
    """Represents a complete schedule for a year"""
    
    def __init__(self, year: int):
        self.year: int = year
        self.time_slots: List[TimeSlot] = []
        self.conflicts: List[Tuple[TimeSlot, TimeSlot, str]] = []
    
    def add_time_slot(self, time_slot: TimeSlot) -> bool:
        """
        Add a time slot to the schedule, checking for conflicts.
        ÖNEMLİ: Çakışma olsa bile slot'u ekler, sadece conflicts listesine de kaydeder.
        """
        has_conflict = False
        
        # Check for conflicts
        for existing_slot in self.time_slots:
            if time_slot.overlaps_with(existing_slot):
                conflict_reason = self._get_conflict_reason(time_slot, existing_slot)
                self.conflicts.append((time_slot, existing_slot, conflict_reason))
                has_conflict = True
        
        # ÖNEMLİ DEĞİŞİKLİK: Çakışma olsa bile slot'u ekle
        self.time_slots.append(time_slot)
        
        # Çakışma varsa False döndür (bilgilendirme amaçlı)
        return not has_conflict
    
    def _get_conflict_reason(self, slot1: TimeSlot, slot2: TimeSlot) -> str:
        """Determine the reason for conflict between two slots"""
        if slot1.course and slot2.course:
            if slot1.course.instructor == slot2.course.instructor:
                return f"Instructor {slot1.course.instructor} conflict"
            if slot1.course.department == slot2.course.department == "SENG" and \
               slot1.course.is_elective and slot2.course.is_elective:
                return "SENG elective overlap"
            if slot1.course.department == slot2.course.department == "CENG" and \
               slot1.course.is_elective and slot2.course.is_elective:
                return "CENG elective overlap"
        return "Time overlap"
    
    def get_slots_by_day(self, day: DayOfWeek) -> List[TimeSlot]:
        """Get all time slots for a specific day"""
        return [slot for slot in self.time_slots if slot.day == day]
    
    def has_conflicts(self) -> bool:
        """Check if schedule has any conflicts"""
        return len(self.conflicts) > 0
    
    def clear(self) -> None:
        """Clear all time slots and conflicts"""
        self.time_slots.clear()
        self.conflicts.clear()

