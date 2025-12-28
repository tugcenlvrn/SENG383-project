"""
Course model representing a course in the curriculum
"""
from enum import Enum
from typing import Optional, List
from dataclasses import dataclass


class CourseType(Enum):
    """Course type enumeration"""
    MANDATORY = "Mandatory"
    ELECTIVE = "Elective"
    SERVICE = "Service"
    COMMON = "Common"
    GRADUATE = "Graduate"
    TECHNICAL_LAB_ELECTIVE = "Technical Lab Elective"


@dataclass
class Course:
    """Represents a course with all its properties"""
    code: str
    name: str
    year: int
    theory_hours: int
    lab_hours: int
    course_type: CourseType
    instructor: Optional[str]
    student_count: int
    lab_sections: int
    department: str = "SENG"  # SENG, CENG, or COMMON
    
    def __post_init__(self):
        """Validate course data after initialization"""
        if self.code.startswith("CENG"):
            self.department = "CENG"
        elif self.code.startswith(("PHYS", "MATH", "ENG", "TURK", "HIST", "BIO", "ESR")):
            self.department = "COMMON"
        else:
            self.department = "SENG"
    
    @property
    def total_hours(self) -> int:
        """Total hours per week (theory + lab)"""
        return self.theory_hours + self.lab_hours
    
    @property
    def is_lab_course(self) -> bool:
        """Check if course has lab hours"""
        return self.lab_hours > 0
    
    @property
    def is_elective(self) -> bool:
        """Check if course is elective"""
        return self.course_type == CourseType.ELECTIVE
    
    @property
    def is_common(self) -> bool:
        """Check if course is common course"""
        return self.department == "COMMON"
    
    def __str__(self) -> str:
        return f"{self.code}: {self.name}"
    
    def __repr__(self) -> str:
        return f"Course(code='{self.code}', name='{self.name}', year={self.year})"

