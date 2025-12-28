"""
Instructor model representing a faculty member
"""
from typing import List, Optional
from dataclasses import dataclass, field
from .course import Course


@dataclass
class Instructor:
    """Represents an instructor with their courses and constraints"""
    name: str
    courses: List[Course] = field(default_factory=list)
    is_part_time: bool = False
    max_hours_per_day: int = 4
    
    def add_course(self, course: Course) -> None:
        """Add a course to instructor's teaching load"""
        if course not in self.courses:
            self.courses.append(course)
    
    def get_total_hours(self) -> int:
        """Get total teaching hours per week"""
        return sum(course.theory_hours for course in self.courses)
    
    def get_courses_by_year(self, year: int) -> List[Course]:
        """Get all courses taught by this instructor for a specific year"""
        return [c for c in self.courses if c.year == year]
    
    def __str__(self) -> str:
        return self.name
    
    def __repr__(self) -> str:
        return f"Instructor(name='{self.name}', courses={len(self.courses)})"

