"""
Login window for BeePlan
"""
from PySide6.QtWidgets import (
    QWidget, QVBoxLayout, QHBoxLayout, QLabel, QLineEdit,
    QPushButton, QMessageBox
)
from PySide6.QtCore import Qt, Signal
from PySide6.QtGui import QFont


class LoginWindow(QWidget):
    """Login window with mock authentication"""
    
    login_successful = Signal()
    
    def __init__(self):
        super().__init__()
        self.setWindowTitle("BeePlan - Login")
        self.setFixedSize(420, 450)
        self._setup_ui()
        self._apply_styles()
    
    def _setup_ui(self):
        """Setup the UI components"""
        layout = QVBoxLayout()
        layout.setSpacing(15)
        layout.setContentsMargins(40, 50, 40, 50)
        
        # Title
        title = QLabel("BeePlan")
        title.setAlignment(Qt.AlignCenter)
        title_font = QFont()
        title_font.setPointSize(24)
        title_font.setBold(True)
        title.setFont(title_font)
        title.setContentsMargins(0, 0, 0, 5)
        layout.addWidget(title)
        
        subtitle = QLabel("Course Scheduling System")
        subtitle.setAlignment(Qt.AlignCenter)
        subtitle_font = QFont()
        subtitle_font.setPointSize(12)
        subtitle.setFont(subtitle_font)
        layout.addWidget(subtitle)
        
        layout.addSpacing(20)
        
        # Email field
        email_layout = QVBoxLayout()
        email_layout.setSpacing(5)
        email_label = QLabel("Email:")
        email_layout.addWidget(email_label)
        self.email_input = QLineEdit()
        self.email_input.setPlaceholderText("admin@cankaya.edu.tr")
        email_layout.addWidget(self.email_input)
        layout.addLayout(email_layout)
        
        layout.addSpacing(5)
        
        # Password field
        password_layout = QVBoxLayout()
        password_layout.setSpacing(5)
        password_label = QLabel("Password:")
        password_layout.addWidget(password_label)
        self.password_input = QLineEdit()
        self.password_input.setPlaceholderText("123456")
        self.password_input.setEchoMode(QLineEdit.Password)
        password_layout.addWidget(self.password_input)
        layout.addLayout(password_layout)
        
        layout.addSpacing(15)
        
        # Login button
        self.login_button = QPushButton("Login")
        self.login_button.clicked.connect(self._handle_login)
        self.login_button.setMinimumHeight(40)
        layout.addWidget(self.login_button)
        
        # Enter key support
        self.password_input.returnPressed.connect(self._handle_login)
        
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
            QLineEdit {
                padding: 12px;
                border: 2px solid #dbe2ec;
                border-radius: 5px;
                font-size: 15px;
                background-color: white;
                color: #0f1b40;
                min-height: 24px;
            }
            QLineEdit:focus {
                border-color: #f4bd2a;
            }
            QPushButton {
                background-color: #f4bd2a;
                color: #0f1b40;
                border: none;
                border-radius: 5px;
                font-size: 14px;
                font-weight: bold;
                padding: 10px;
            }
            QPushButton:hover {
                background-color: #e8b025;
            }
            QPushButton:pressed {
                background-color: #dba420;
            }
        """)
    
    def _handle_login(self):
        """Handle login attempt"""
        email = self.email_input.text().strip()
        password = self.password_input.text().strip()
        
        # Mock authentication
        if email == "admin@cankaya.edu.tr" and password == "123456":
            self.login_successful.emit()
            self.close()
        else:
            QMessageBox.warning(
                self,
                "Login Error",
                "Invalid email or password!\n\nTest credentials:\nEmail: admin@cankaya.edu.tr\nPassword: 123456"
            )

