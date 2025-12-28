"""
Dialog for selecting elective courses
"""
from typing import List
from PySide6.QtWidgets import (
    QDialog, QVBoxLayout, QHBoxLayout, QLabel, QPushButton,
    QListWidget, QListWidgetItem, QMessageBox
)
from PySide6.QtCore import Qt
from PySide6.QtGui import QFont

from models.course import Course


class ElectiveSelectionDialog(QDialog):
    """Dialog for selecting elective courses before schedule generation"""
    
    def __init__(self, elective_courses: List[Course], parent=None):
        super().__init__(parent)
        self.elective_courses = elective_courses
        self.selected_courses = []
        self.setWindowTitle("Elective Course Selection")
        self.setMinimumSize(600, 500)
        self._setup_ui()
        self._apply_styles()
    
    def _setup_ui(self):
        """Setup the UI components"""
        layout = QVBoxLayout()
        layout.setSpacing(15)
        layout.setContentsMargins(20, 20, 20, 20)
        
        # Title
        title = QLabel("Select Elective Courses")
        title_font = QFont()
        title_font.setPointSize(16)
        title_font.setBold(True)
        title.setFont(title_font)
        title.setAlignment(Qt.AlignCenter)
        layout.addWidget(title)
        
        # Instruction
        instruction = QLabel(
            "Select elective courses to add to the schedule. "
            "You can select multiple courses."
        )
        instruction.setWordWrap(True)
        layout.addWidget(instruction)
        
        # List of elective courses
        self.course_list = QListWidget()
        self.course_list.setSelectionMode(QListWidget.MultiSelection)
        
        # Group courses by year
        courses_by_year = {}
        for course in self.elective_courses:
            year = course.year if course.year > 0 else 0
            if year not in courses_by_year:
                courses_by_year[year] = []
            courses_by_year[year].append(course)
        
        # Add courses to list, grouped by year
        for year in sorted(courses_by_year.keys()):
            if year > 0:
                year_item = QListWidgetItem(f"--- Year {year} Electives ---")
                year_item.setFlags(Qt.NoItemFlags)
                year_item.setBackground(Qt.lightGray)
                self.course_list.addItem(year_item)
            else:
                item = QListWidgetItem("--- General Electives ---")
                item.setFlags(Qt.NoItemFlags)
                item.setBackground(Qt.lightGray)
                self.course_list.addItem(item)
            
            for course in courses_by_year[year]:
                item_text = f"{course.code}: {course.name}"
                if course.instructor:
                    item_text += f" - {course.instructor}"
                item = QListWidgetItem(item_text)
                item.setData(Qt.UserRole, course)
                self.course_list.addItem(item)
        
        layout.addWidget(self.course_list)
        
        # Buttons
        button_layout = QHBoxLayout()
        button_layout.addStretch()
        
        self.cancel_button = QPushButton("Cancel")
        self.cancel_button.clicked.connect(self.reject)
        button_layout.addWidget(self.cancel_button)
        
        self.ok_button = QPushButton("Confirm Selection")
        self.ok_button.clicked.connect(self._on_ok_clicked)
        button_layout.addWidget(self.ok_button)
        
        layout.addLayout(button_layout)
        
        self.setLayout(layout)
    
    def _apply_styles(self):
        """Apply modern styling with design colors"""
        self.setStyleSheet("""
            QDialog {
                background-color: #ffffff;
            }
            QLabel {
                color: #0f1b40;
            }
            QListWidget {
                border: 1px solid #dbe2ec;
                border-radius: 5px;
                background-color: white;
                color: #0f1b40;
                padding: 5px;
            }
            QListWidget::item {
                padding: 8px;
                border-bottom: 1px solid #dbe2ec;
            }
            QListWidget::item:selected {
                background-color: #f4bd2a;
                color: #0f1b40;
            }
            QListWidget::item:hover {
                background-color: #dbe2ec;
            }
            QPushButton {
                background-color: #f4bd2a;
                color: #0f1b40;
                border: none;
                border-radius: 5px;
                padding: 10px 20px;
                font-weight: bold;
                min-width: 100px;
            }
            QPushButton:hover {
                background-color: #e8b025;
            }
        """)
    
    def _on_ok_clicked(self):
        """Handle OK button click"""
        selected_items = self.course_list.selectedItems()
        
        if not selected_items:
            QMessageBox.information(
                self,
                "Information",
                "No elective courses selected. Only mandatory courses will be added to the schedule."
            )
        
        self.selected_courses = []
        for item in selected_items:
            course = item.data(Qt.UserRole)
            if course:
                self.selected_courses.append(course)
        
        self.accept()
    
    def get_selected_courses(self) -> List[Course]:
        """Get list of selected elective courses"""
        return self.selected_courses

