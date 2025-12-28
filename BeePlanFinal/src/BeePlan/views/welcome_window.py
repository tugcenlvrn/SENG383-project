"""
BeePlan - Welcome Window (Final Version)
Tasarım: Sol tarafta özel PNG arka plan, Sağ tarafta fonksiyonel menü.
"""
import os
from PySide6.QtWidgets import (
    QWidget, QVBoxLayout, QHBoxLayout, QLabel, QPushButton, QMenu
)
from PySide6.QtCore import Qt, Signal
from PySide6.QtGui import QFont, QPixmap

class WelcomeWindow(QWidget):
    """
    Uygulama açılışında gösterilen karşılama ekranı.
    Sol tarafta tasarım görseli, sağ tarafta menü bulunur.
    """
    
    # Sinyaller (Logic katmanı ile haberleşme)
    login_requested = Signal()
    logout_requested = Signal()
    create_schedule_requested = Signal()
    view_curriculum_requested = Signal()
    view_instructors_requested = Signal()
    
    def __init__(self):
        super().__init__()
        self.setWindowTitle("BeePlan - Course Scheduling System")
        self.setFixedSize(1200, 800)
        self.logged_in = False
        
        self._setup_ui()
        self._apply_styles()
        
        # Başlangıçta giriş yapılmadığı için butonları pasif yap
        self.create_schedule_button.setEnabled(False)
        self.curriculum_button.setEnabled(False)
        self.instructors_button.setEnabled(False)
    
    def _setup_ui(self):
        """Arayüz bileşenlerini oluşturur ve yerleştirir."""
        
        # --- RESİM YOLU TANIMLAMASI ---
        # Windows dosya yolu hatasını önlemek için 'r' (raw string) kullanıyoruz.
        image_path = r"D:\BeePlan\data\background.png"
        
        main_layout = QHBoxLayout()
        main_layout.setContentsMargins(0, 0, 0, 0)
        main_layout.setSpacing(0)
        
        # =========================================================
        # SOL TARAF (GÖRSEL ALANI)
        # =========================================================
        left_widget = QWidget()
        left_widget.setObjectName("leftWidget")
        
        left_layout = QVBoxLayout()
        # Resmin tam kenara yapışması için marginleri sıfırlıyoruz
        left_layout.setContentsMargins(0, 0, 0, 0)
        left_layout.setSpacing(0)
        
        # 1. Üst Kısım: Login Butonu (Resmin üzerinde/üstünde duracak)
        # Butona biraz boşluk vermek için layout içinde margin kullanıyoruz
        top_bar_layout = QHBoxLayout()
        top_bar_layout.setContentsMargins(0, 20, 20, 0) # Üstten ve Sağdan boşluk
        top_bar_layout.addStretch() # Butonu sağa it
        
        self.profile_button = QPushButton("Login")
        self.profile_button.setObjectName("profileButton")
        self.profile_button.setFixedSize(100, 40)
        self.profile_button.clicked.connect(self._on_profile_clicked)
        
        top_bar_layout.addWidget(self.profile_button)
        left_layout.addLayout(top_bar_layout)
        
        # 2. Orta Kısım: Tasarım Resmi (Background PNG)
        self.bg_image_label = QLabel()
        pixmap = QPixmap(image_path)
        
        # Resim yükleme kontrolü (Hata ayıklama için)
        if pixmap.isNull():
            print(f"UYARI: Resim yüklenemedi! Yol: {image_path}")
            self.bg_image_label.setText(f"Görsel Yüklenemedi:\n{image_path}")
            self.bg_image_label.setAlignment(Qt.AlignCenter)
        else:
            self.bg_image_label.setPixmap(pixmap)
        
        # Resmi Sola Yasla (Tasarım soldan kesildiği için)
        # Dikeyde de ortala ki güzel dursun
        self.bg_image_label.setAlignment(Qt.AlignLeft | Qt.AlignVCenter)
        self.bg_image_label.setScaledContents(False) # Resmi sündürme, orjinal kalsın
        
        left_layout.addWidget(self.bg_image_label)
        
        # 3. Alt Kısım: Dengelemek için boşluk
        left_layout.addStretch()
        
        left_widget.setLayout(left_layout)
        
        # Sol taraf 3 birim yer kaplasın
        main_layout.addWidget(left_widget, stretch=3)
        
        # =========================================================
        # SAĞ TARAF (MENÜ ALANI - HİÇ DOKUNULMADI)
        # =========================================================
        right_widget = QWidget()
        right_widget.setObjectName("rightWidget")
        
        right_layout = QVBoxLayout()
        right_layout.setContentsMargins(40, 60, 40, 40)
        right_layout.setSpacing(20)
        
        # Menü Başlığı
        panel_title = QLabel("Menu")
        panel_title_font = QFont()
        panel_title_font.setPointSize(24)
        panel_title_font.setBold(True)
        panel_title.setFont(panel_title_font)
        right_layout.addWidget(panel_title)
        
        right_layout.addSpacing(40)
        
        # Butonlar
        self.create_schedule_button = QPushButton("📅 Create Schedule")
        self.create_schedule_button.setMinimumHeight(60)
        self.create_schedule_button.clicked.connect(self._on_create_schedule)
        right_layout.addWidget(self.create_schedule_button)
        
        self.curriculum_button = QPushButton("📚 View Curriculum")
        self.curriculum_button.setMinimumHeight(60)
        self.curriculum_button.clicked.connect(self._on_view_curriculum)
        right_layout.addWidget(self.curriculum_button)
        
        self.instructors_button = QPushButton("👨‍🏫 View Instructors")
        self.instructors_button.setMinimumHeight(60)
        self.instructors_button.clicked.connect(self._on_view_instructors)
        right_layout.addWidget(self.instructors_button)
        
        right_layout.addStretch()
        
        right_widget.setLayout(right_layout)
        
        # Sağ taraf 1 birim yer kaplasın
        main_layout.addWidget(right_widget, stretch=1)
        
        self.setLayout(main_layout)

    def _apply_styles(self):
        """Renk ve stil ayarları (QSS)"""
        self.setStyleSheet("""
            QWidget#leftWidget {
                background-color: white; /* Resimle bütünleşmesi için beyaz */
            }
            QWidget#rightWidget {
                background-color: #dbe2ec; /* Menü arka planı */
            }
            QLabel {
                color: #0f1b40;
            }
            /* Login Butonu Stili */
            QPushButton#profileButton {
                background-color: #0f1b40;
                color: white;
                border: none;
                border-radius: 5px;
                font-weight: bold;
            }
            QPushButton#profileButton:hover {
                background-color: #2c3e50;
            }
            /* Menü Butonları Stili */
            QPushButton {
                background-color: #dbe2ec; /* Widget rengiyle aynı başlasın */
                color: #0f1b40;
                border: 2px solid #c5d0e0;
                border-radius: 10px;
                font-size: 16px;
                font-weight: bold;
                padding: 10px;
                text-align: left;
                padding-left: 20px;
            }
            QPushButton:hover {
                background-color: #ffffff;
                border-color: #f4bd2a; /* Sarı vurgu */
            }
            QPushButton:disabled {
                color: #a0aec0;
                border-color: #e2e8f0;
                background-color: #edf2f7;
            }
        """)

    # =========================================================
    # FONKSİYONEL MANTIK (HİÇ DOKUNULMADI)
    # =========================================================
    
    def _on_profile_clicked(self):
        if not self.logged_in:
            self.login_requested.emit()
        else:
            menu = QMenu(self)
            menu.addAction("admin@cankaya.edu.tr")
            menu.addSeparator()
            logout_action = menu.addAction("Logout")
            logout_action.triggered.connect(self._on_logout)
            
            # Menüyü butonun altında aç
            button_pos = self.profile_button.mapToGlobal(self.profile_button.rect().bottomLeft())
            menu.exec(button_pos)
    
    def _on_logout(self):
        self.logout_requested.emit()
    
    def _on_create_schedule(self):
        if self.logged_in:
            self.create_schedule_requested.emit()
    
    def _on_view_curriculum(self):
        if self.logged_in:
            self.view_curriculum_requested.emit()
    
    def _on_view_instructors(self):
        if self.logged_in:
            self.view_instructors_requested.emit()
    
    def set_logged_in(self, logged_in: bool):
        self.logged_in = logged_in
        if logged_in:
            self.profile_button.setText("Profile")
            self.create_schedule_button.setEnabled(True)
            self.curriculum_button.setEnabled(True)
            self.instructors_button.setEnabled(True)
        else:
            self.profile_button.setText("Login")
            self.create_schedule_button.setEnabled(False)
            self.curriculum_button.setEnabled(False)
            self.instructors_button.setEnabled(False)
            