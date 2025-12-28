"""
Curriculum view window
"""
from PySide6.QtWidgets import (
    QWidget, QVBoxLayout, QHBoxLayout, QLabel, QPushButton,
    QTableWidget, QTableWidgetItem, QHeaderView, QMessageBox
)
from PySide6.QtCore import Qt, Signal
from PySide6.QtGui import QFont

from controllers.data_controller import DataController


class CurriculumView(QWidget):
    """Window for viewing curriculum"""
    
    back_requested = Signal()
    
    def __init__(self, data_controller: DataController, parent=None):
        super().__init__(parent)
        self.data_controller = data_controller
        self.setWindowTitle("Curriculum View")
        self.setMinimumSize(1000, 700)
        self._setup_ui()
        self._apply_styles()
        self._load_curriculum()
    
    def _setup_ui(self):
        """Setup the UI components"""
        layout = QVBoxLayout()
        layout.setContentsMargins(20, 20, 20, 20)
        layout.setSpacing(15)
        
        # Header
        header_layout = QHBoxLayout()
        
        title = QLabel("Curriculum View")
        title_font = QFont()
        title_font.setPointSize(24)
        title_font.setBold(True)
        title.setFont(title_font)
        header_layout.addWidget(title)
        
        header_layout.addStretch()
        
        back_button = QPushButton("← Back to Home")
        back_button.clicked.connect(self._on_back)
        header_layout.addWidget(back_button)
        
        layout.addLayout(header_layout)
        
        # Table
        self.table = QTableWidget()
        self.table.setColumnCount(7)
        self.table.setHorizontalHeaderLabels([
            "Course Code", "Course Name", "Year", "Type", 
            "Theory Hours", "Lab Hours", "Instructor"
        ])
        self.table.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.table.setEditTriggers(QTableWidget.NoEditTriggers)
        self.table.setAlternatingRowColors(True)
        
        layout.addWidget(self.table)
        
        self.setLayout(layout)
    
    def _apply_styles(self):
        """Apply modern styling with design colors"""
        self.setStyleSheet("""
            QWidget {
                background-color: #ffffff;
            }
            QLabel {
                color: #0f1b40;
            }
            QTableWidget {
                border: 1px solid #dbe2ec;
                border-radius: 8px;
                background-color: white;
                gridline-color: #dbe2ec;
                color: #0f1b40;
            }
            QTableWidget::item {
                padding: 8px;
                border: none;
                color: #0f1b40;
            }
            QTableWidget::item:selected {
                background-color: #f4bd2a;
                color: #0f1b40;
            }
            QHeaderView::section {
                background-color: #0f1b40;
                color: white;
                padding: 10px;
                border: none;
                font-weight: bold;
            }
            QPushButton {
                background-color: #f4bd2a;
                color: #0f1b40;
                border: none;
                border-radius: 5px;
                padding: 10px 20px;
                font-weight: bold;
            }
            QPushButton:hover {
                background-color: #e8b025;
            }
        """)
    
    def _load_curriculum(self):
        """Load curriculum data into table"""
        courses = self.data_controller.department_courses
        self.table.setRowCount(len(courses))
        
        for row, course in enumerate(courses):
            self.table.setItem(row, 0, QTableWidgetItem(course.code))
            self.table.setItem(row, 1, QTableWidgetItem(course.name))
            year_text = f"Year {course.year}" if course.year > 0 else "Elective/Service"
            self.table.setItem(row, 2, QTableWidgetItem(year_text))
            self.table.setItem(row, 3, QTableWidgetItem(course.course_type.value))
            self.table.setItem(row, 4, QTableWidgetItem(str(course.theory_hours)))
            self.table.setItem(row, 5, QTableWidgetItem(str(course.lab_hours)))
            instructor_text = course.instructor or "Not Specified"
            self.table.setItem(row, 6, QTableWidgetItem(instructor_text))
    
    def _on_back(self):
        """Handle back button click"""
        self.back_requested.emit()

