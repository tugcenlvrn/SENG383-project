"""
Data controller for managing course and instructor data
"""
import os
from typing import List, Dict, Optional
from pathlib import Path

from models.course import Course
from models.instructor import Instructor
from utils.csv_loader import CSVLoader


class DataController:
    """Manages loading and accessing course and instructor data"""
    
    def __init__(self, data_dir: str = "data"):
        self.data_dir = Path(data_dir)
        self.common_courses: List[Dict] = []
        self.department_courses: List[Course] = []
        self.instructors: Dict[str, Instructor] = {}
        
        # Ensure data directory exists
        self.data_dir.mkdir(exist_ok=True)
        
        # Load data
        self.load_data()
    
    def load_data(self) -> None:
        """Load all data from CSV files"""
        common_courses_path = self.data_dir / "common_courses.csv"
        curriculum_path = self.data_dir / "department_curriculum.csv"
        
        # Load common courses
        if common_courses_path.exists():
            self.common_courses = CSVLoader.load_common_courses(str(common_courses_path))
        
        # Load department curriculum
        if curriculum_path.exists():
            self.department_courses = CSVLoader.load_department_curriculum(str(curriculum_path))
            self.instructors = CSVLoader.create_instructors_from_courses(self.department_courses)
    
    def get_courses_by_year(self, year: int) -> List[Course]:
        """Get all courses for a specific year (excluding ELEC placeholders which will be replaced)"""
        # Get all courses for this year, but exclude ELEC placeholders
        # ELEC courses will be replaced with actual selected electives during schedule generation
        # SENG 429 (Technical Lab Elective) is already included since it's year=4 in CSV
        courses = [c for c in self.department_courses if c.year == year and c.code != "ELEC"]
        return courses
    
    def get_common_courses_by_year(self, year: int) -> List[Dict]:
        """Get common courses for a specific year"""
        year_str = f"{year}. Sınıf"
        return [c for c in self.common_courses if year_str in c.get('Sınıf', '')]
    
    def get_instructor(self, name: str) -> Optional[Instructor]:
        """Get instructor by name"""
        return self.instructors.get(name)
    
    def get_all_instructors(self) -> List[Instructor]:
        """Get all instructors"""
        return list(self.instructors.values())
    
    def get_elective_courses(self) -> List[Course]:
        """Get all elective courses (excluding technical lab electives which are auto-added)"""
        return [c for c in self.department_courses 
                if c.is_elective and c.course_type.value != "Technical Lab Elective"]
    
    def get_courses_by_instructor(self, instructor_name: str) -> List[Course]:
        """Get all courses taught by an instructor"""
        instructor = self.get_instructor(instructor_name)
        if instructor:
            return instructor.courses
        return []

