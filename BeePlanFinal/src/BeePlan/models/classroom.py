"""
Classroom model for managing classroom resources
"""
from enum import Enum
from typing import Optional
from dataclasses import dataclass


class ClassroomType(Enum):
    """Classroom type enumeration"""
    THEORY = "Teori"
    LAB = "Lab"
    COMPUTER_LAB = "Bilgisayar Lab"


@dataclass
class Classroom:
    """Represents a classroom with capacity and type"""
    name: str
    capacity: int
    classroom_type: ClassroomType
    building: Optional[str] = None
    floor: Optional[int] = None
    
    def can_accommodate(self, student_count: int, is_lab: bool = False) -> bool:
        """Check if classroom can accommodate the student count"""
        if is_lab and self.classroom_type != ClassroomType.LAB and \
           self.classroom_type != ClassroomType.COMPUTER_LAB:
            return False
        
        # Lab capacity constraint: max 40 students
        if is_lab and student_count > 40:
            return False
        
        return student_count <= self.capacity
    
    def __str__(self) -> str:
        return f"{self.name} ({self.capacity} kapasite, {self.classroom_type.value})"
    
    def __repr__(self) -> str:
        return f"Classroom(name='{self.name}', capacity={self.capacity}, type={self.classroom_type})"

