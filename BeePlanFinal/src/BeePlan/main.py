"""
BeePlan - Course Scheduling System
Main application entry point
"""
import sys
from PySide6.QtWidgets import QApplication

from views.welcome_window import WelcomeWindow
from views.login_window import LoginWindow
from views.main_window import MainWindow
from views.curriculum_view import CurriculumView
from views.instructors_view import InstructorsView
from controllers.data_controller import DataController


def main():
    """Main application entry point"""
    app = QApplication(sys.argv)
    app.setApplicationName("BeePlan")
    app.setOrganizationName("Cankaya University")
    
    # Set application style
    app.setStyle('Fusion')
    
    # Initialize data controller (shared)
    data_controller = DataController()
    
    # Show welcome window
    welcome_window = WelcomeWindow()
    login_window = LoginWindow()
    main_window = MainWindow()
    curriculum_view = CurriculumView(data_controller)
    instructors_view = InstructorsView(data_controller)
    
    def on_login_requested():
        """Handle login request from welcome window"""
        login_window.show()
    
    def on_login_success():
        """Handle successful login"""
        login_window.close()
        welcome_window.set_logged_in(True)
    
    def on_logout():
        """Handle logout"""
        welcome_window.set_logged_in(False)
        main_window.hide()
        curriculum_view.hide()
        instructors_view.hide()
        welcome_window.show()
    
    def on_create_schedule():
        """Handle create schedule request"""
        welcome_window.hide()
        main_window.show()
    
    def on_main_window_back():
        """Handle back from main window"""
        main_window.hide()
        welcome_window.show()
    
    def on_view_curriculum():
        """Handle view curriculum request"""
        welcome_window.hide()
        curriculum_view.show()
    
    def on_view_instructors():
        """Handle view instructors request"""
        welcome_window.hide()
        instructors_view.show()
    
    def on_curriculum_back():
        """Handle back from curriculum view"""
        curriculum_view.hide()
        welcome_window.show()
    
    def on_instructors_back():
        """Handle back from instructors view"""
        instructors_view.hide()
        welcome_window.show()
    
    welcome_window.login_requested.connect(on_login_requested)
    welcome_window.logout_requested.connect(on_logout)
    login_window.login_successful.connect(on_login_success)
    welcome_window.create_schedule_requested.connect(on_create_schedule)
    welcome_window.view_curriculum_requested.connect(on_view_curriculum)
    welcome_window.view_instructors_requested.connect(on_view_instructors)
    main_window.back_requested.connect(on_main_window_back)
    curriculum_view.back_requested.connect(on_curriculum_back)
    instructors_view.back_requested.connect(on_instructors_back)
    
    welcome_window.show()
    
    sys.exit(app.exec())


if __name__ == '__main__':
    main()

