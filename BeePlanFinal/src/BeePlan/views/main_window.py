"""
Main window for BeePlan dashboard
"""
from PySide6.QtWidgets import (
    QMainWindow, QWidget, QVBoxLayout, QHBoxLayout, QLabel,
    QPushButton, QStackedWidget, QMessageBox, QDialog
)
from PySide6.QtCore import Qt, Signal
from PySide6.QtGui import QFont

from views.timetable_widget import TimetableWidget
from views.report_dialog import ReportDialog
from views.elective_selection_dialog import ElectiveSelectionDialog
from controllers.data_controller import DataController
from controllers.schedule_controller import ScheduleController
from models.schedule import Schedule


class MainWindow(QMainWindow):
    """Main application window with sidebar navigation"""
    
    back_requested = Signal()
    
    def __init__(self):
        super().__init__()
        self.setWindowTitle("BeePlan - Course Scheduling System")
        self.setMinimumSize(1200, 800)
        
        # Initialize controllers
        self.data_controller = DataController()
        self.schedule_controller = ScheduleController(self.data_controller)
        
        # Store schedules for each year
        self.schedules = {1: None, 2: None, 3: None, 4: None}
        
        self._setup_ui()
        self._apply_styles()
    
    def _setup_ui(self):
        """Setup the main UI"""
        central_widget = QWidget()
        main_layout = QHBoxLayout()
        main_layout.setContentsMargins(0, 0, 0, 0)
        main_layout.setSpacing(0)
        
        # Sidebar
        sidebar = self._create_sidebar()
        main_layout.addWidget(sidebar)
        
        # Main content area
        content_widget = QWidget()
        content_layout = QVBoxLayout()
        content_layout.setContentsMargins(20, 20, 20, 20)
        content_layout.setSpacing(10)
        
        # Header with back button
        header_layout = QHBoxLayout()
        
        header = QLabel("Schedule Creation")
        header_font = QFont()
        header_font.setPointSize(20)
        header_font.setBold(True)
        header.setFont(header_font)
        header_layout.addWidget(header)
        
        header_layout.addStretch()
        
        back_button = QPushButton("← Back to Home")
        back_button.setObjectName("backButton")
        back_button.clicked.connect(self._on_back_to_welcome)
        header_layout.addWidget(back_button)
        
        content_layout.addLayout(header_layout)
        
        # Info label
        info_label = QLabel(
            "Click 'Create Schedule' button to generate schedules for all classes. "
            "You can select a class to view the generated schedule."
        )
        info_label.setWordWrap(True)
        content_layout.addWidget(info_label)
        
        # Timetable widget
        self.timetable = TimetableWidget()
        self.timetable.year_combo.currentIndexChanged.connect(self._on_year_changed)
        self.timetable.generate_button.clicked.connect(self._on_generate_schedule)
        self.timetable.generate_button.setText("Create Schedule for All Classes")
        content_layout.addWidget(self.timetable)
        
        # Action buttons
        button_layout = QHBoxLayout()
        button_layout.addStretch()
        
        self.view_report_button = QPushButton("View Report")
        self.view_report_button.clicked.connect(self._on_view_report)
        self.view_report_button.setEnabled(False)
        button_layout.addWidget(self.view_report_button)
        
        content_layout.addLayout(button_layout)
        
        content_widget.setLayout(content_layout)
        main_layout.addWidget(content_widget, stretch=1)
        
        central_widget.setLayout(main_layout)
        self.setCentralWidget(central_widget)
    
    def _create_sidebar(self) -> QWidget:
        """Create sidebar navigation"""
        sidebar = QWidget()
        sidebar.setFixedWidth(250)
        sidebar_layout = QVBoxLayout()
        sidebar_layout.setContentsMargins(20, 20, 20, 20)
        sidebar_layout.setSpacing(10)
        
        # Logo/Title
        logo = QLabel("BeePlan")
        logo_font = QFont()
        logo_font.setPointSize(18)
        logo_font.setBold(True)
        logo.setFont(logo_font)
        logo.setAlignment(Qt.AlignCenter)
        sidebar_layout.addWidget(logo)
        
        sidebar_layout.addSpacing(20)
        
        sidebar_layout.addStretch()
        
        sidebar.setLayout(sidebar_layout)
        return sidebar
    
    def _apply_styles(self):
        """Apply modern styling with design colors"""
        self.setStyleSheet("""
            QMainWindow {
                background-color: #ffffff;
            }
            QWidget {
                font-family: 'Segoe UI', Arial, sans-serif;
            }
            QPushButton {
                padding: 12px;
                border: none;
                border-radius: 5px;
                font-size: 14px;
                text-align: left;
                background-color: #ffffff;
                color: #0f1b40;
            }
            QPushButton:hover {
                background-color: #dbe2ec;
            }
            QPushButton:checked {
                background-color: #f4bd2a;
                color: #0f1b40;
                font-weight: bold;
            }
            QPushButton#backButton {
                background-color: #f4bd2a;
                color: #0f1b40;
                font-weight: bold;
                text-align: center;
            }
            QPushButton#backButton:hover {
                background-color: #e8b025;
            }
            QLabel {
                color: #0f1b40;
            }
        """)
    
    def _on_year_changed(self, index: int):
        """Handle year selection change"""
        year = index + 1
        schedule = self.schedules.get(year)
        if schedule:
            self.timetable.display_schedule(schedule)
            self.view_report_button.setEnabled(True)
        else:
            # Clear timetable
            self.timetable.display_schedule(Schedule(year))
            self.view_report_button.setEnabled(False)
    
    def _on_generate_schedule(self):
        """Generate schedules for all years (1-4) simultaneously"""
        # First, show elective selection dialog
        elective_courses = self.data_controller.get_elective_courses()
        selected_electives = []
        
        if elective_courses:
            dialog = ElectiveSelectionDialog(elective_courses, self)
            if dialog.exec() == QDialog.Accepted:
                selected_electives = dialog.get_selected_courses()
            else:
                return  # User cancelled
        
        # Show progress message
        QMessageBox.information(
            self,
            "Creating Schedule",
            "Creating schedules for all classes...\nPlease wait."
        )
        
        try:
            # Generate schedules for all years
            all_schedules = self.schedule_controller.generate_all_schedules(selected_electives)
            
            # Store all schedules
            for year, schedule in all_schedules.items():
                self.schedules[year] = schedule
            
            # Display the first year's schedule by default
            first_year = 1
            if self.schedules.get(first_year):
                self.timetable.year_combo.setCurrentIndex(0)
                self.timetable.display_schedule(self.schedules[first_year])
                self.view_report_button.setEnabled(True)
            
            # Check for conflicts
            total_conflicts = sum(1 for s in all_schedules.values() if s.has_conflicts())
            
            if total_conflicts > 0:
                QMessageBox.warning(
                    self,
                    "Schedule Created",
                    f"Schedules created for all classes but some conflicts were detected.\n"
                    f"Please check the report."
                )
            else:
                QMessageBox.information(
                    self,
                    "Success",
                    "Schedules successfully created for all classes!"
                )
        except Exception as e:
            QMessageBox.critical(
                self,
                "Error",
                f"An error occurred while creating schedules:\n{str(e)}"
            )
    
    def _on_view_report(self):
        """Show validation report"""
        year = self.timetable.get_selected_year()
        schedule = self.schedules.get(year)
        
        if not schedule:
            QMessageBox.warning(
                self,
                "Warning",
                "You need to create a schedule first."
            )
            return
        
        dialog = ReportDialog(schedule, self.data_controller, self)
        dialog.exec()
    
    def _on_back_to_welcome(self):
        """Handle back to welcome button click"""
        self.back_requested.emit()
    
    def _on_logout(self):
        """Handle logout"""
        reply = QMessageBox.question(
            self,
            "Logout",
            "Are you sure you want to logout?",
            QMessageBox.Yes | QMessageBox.No
        )
        
        if reply == QMessageBox.Yes:
            self.close()

