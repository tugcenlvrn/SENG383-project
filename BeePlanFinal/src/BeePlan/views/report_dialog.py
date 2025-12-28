"""
Report dialog for displaying schedule validation results
"""
from PySide6.QtWidgets import (
    QDialog, QVBoxLayout, QHBoxLayout, QLabel, QPushButton,
    QTextEdit, QScrollArea, QWidget
)
from PySide6.QtCore import Qt
from PySide6.QtGui import QFont

from models.schedule import Schedule, DayOfWeek
from utils.time_utils import TimeUtils


class ReportDialog(QDialog):
    """Dialog for displaying schedule validation report"""
    
    def __init__(self, schedule: Schedule, data_controller=None, parent=None):
        super().__init__(parent)
        self.schedule = schedule
        self.data_controller = data_controller
        self.setWindowTitle("Schedule Report")
        self.setMinimumSize(600, 500)
        self._setup_ui()
        self._apply_styles()
        self._generate_report()
    
    def _setup_ui(self):
        """Setup the UI components"""
        layout = QVBoxLayout()
        layout.setSpacing(15)
        layout.setContentsMargins(20, 20, 20, 20)
        
        # Title
        title = QLabel("Schedule Validation Report")
        title_font = QFont()
        title_font.setPointSize(16)
        title_font.setBold(True)
        title.setFont(title_font)
        title.setAlignment(Qt.AlignCenter)
        layout.addWidget(title)
        
        # Report text area
        self.report_text = QTextEdit()
        self.report_text.setReadOnly(True)
        layout.addWidget(self.report_text)
        
        # Buttons
        button_layout = QHBoxLayout()
        button_layout.addStretch()
        
        self.close_button = QPushButton("Close")
        self.close_button.clicked.connect(self.accept)
        button_layout.addWidget(self.close_button)
        
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
            QTextEdit {
                border: 1px solid #dbe2ec;
                border-radius: 5px;
                background-color: white;
                color: #0f1b40;
                padding: 10px;
                font-family: 'Consolas', 'Monaco', monospace;
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
    
    def _generate_report(self):
        """Generate validation report"""
        report = []
        report.append("=" * 60)
        report.append("SCHEDULE VALIDATION REPORT")
        report.append("=" * 60)
        report.append("")
        
        # Schedule summary
        report.append(f"Class: Year {self.schedule.year}")
        report.append(f"Total Course Hours: {len(self.schedule.time_slots)}")
        report.append("")
        
        # Conflicts
        if self.schedule.has_conflicts():
            report.append("⚠️  CONFLICTS:")
            report.append("-" * 60)
            for slot1, slot2, reason in self.schedule.conflicts:
                report.append(f"• {reason}")
                if slot1.course:
                    report.append(f"  - {slot1.course.code} ({slot1.day.value} {slot1.start_time.strftime('%H:%M')})")
                if slot2.course:
                    report.append(f"  - {slot2.course.code} ({slot2.day.value} {slot2.start_time.strftime('%H:%M')})")
                report.append("")
        else:
            report.append("✅ No conflicts!")
            report.append("")
        
        # Course list
        report.append("COURSE LIST:")
        report.append("-" * 60)
        
        courses_scheduled = {}
        for slot in self.schedule.time_slots:
            if slot.course:
                code = slot.course.code
                if code not in courses_scheduled:
                    courses_scheduled[code] = {
                        'course': slot.course,
                        'slots': []
                    }
                courses_scheduled[code]['slots'].append(slot)
        
        for code, data in sorted(courses_scheduled.items()):
            course = data['course']
            slots = data['slots']
            report.append(f"\n{code}: {course.name}")
            report.append(f"  Type: {course.course_type.value}")
            report.append(f"  Instructor: {course.instructor or 'Not Specified'}")
            report.append(f"  Student Count: {course.student_count}")
            report.append(f"  Times:")
            for slot in slots:
                lab_str = " (Lab)" if slot.is_lab else ""
                report.append(f"    - {slot.day.value} {slot.start_time.strftime('%H:%M')}-{slot.end_time.strftime('%H:%M')}{lab_str}")
        
        # Validation checks
        report.append("")
        report.append("VALIDATION CHECKS:")
        report.append("-" * 60)
        
        # Check Friday exam block
        friday_violations = [
            slot for slot in self.schedule.time_slots
            if slot.day == DayOfWeek.FRIDAY and
            TimeUtils.is_friday_exam_block(slot.start_time, slot.end_time)
        ]
        if friday_violations:
            report.append("❌ Course scheduled in Friday exam block (13:20-15:10)!")
            for slot in friday_violations:
                report.append(f"  - {slot.course.code if slot.course else 'Unknown'}")
        else:
            report.append("✅ Friday exam block rule compliant")
        
        # Check instructor daily hours constraint (max 4 hours per day)
        if self.data_controller:
            instructor_violations = {}
            for day in TimeUtils.get_all_days():
                day_slots = [s for s in self.schedule.time_slots if s.day == day and s.course and s.course.instructor]
                instructor_hours = {}
                for slot in day_slots:
                    if not slot.is_lab:  # Only count theory hours
                        instructor_name = slot.course.instructor
                        if instructor_name not in instructor_hours:
                            instructor_hours[instructor_name] = 0
                        instructor_hours[instructor_name] += 1
                
                for instructor_name, hours in instructor_hours.items():
                    instructor = self.data_controller.get_instructor(instructor_name)
                    max_hours = instructor.max_hours_per_day if instructor else 4
                    if hours > max_hours:
                        key = (instructor_name, day.value)
                        instructor_violations[key] = (hours, max_hours)
            
            if instructor_violations:
                report.append("❌ Instructor daily hours rule violation!")
                for (instructor_name, day), (actual, max_hours) in instructor_violations.items():
                    report.append(f"  - {instructor_name} ({day}): {actual} hours (Maximum: {max_hours} hours)")
            else:
                report.append("✅ Instructor daily hours rule compliant")
        
        # Check lab student capacity (max 40 students per lab section)
        if self.data_controller:
            lab_violations = []
            for slot in self.schedule.time_slots:
                if slot.is_lab and slot.course:
                    if slot.course.student_count > 40:
                        lab_violations.append((slot.course.code, slot.course.student_count))
            
            if lab_violations:
                report.append("❌ Lab student capacity rule violation! (Max: 40 students)")
                for course_code, student_count in lab_violations:
                    report.append(f"  - {course_code}: {student_count} students")
            else:
                report.append("✅ Lab student capacity rule compliant (Max: 40 students)")
        
        report.append("")
        report.append("=" * 60)
        
        self.report_text.setPlainText("\n".join(report))

