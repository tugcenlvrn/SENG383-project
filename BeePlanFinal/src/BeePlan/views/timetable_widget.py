"""
Timetable widget for displaying weekly schedule
Final Version: Matrix Logic + Conflict Handling + Color Coding
"""
from PySide6.QtWidgets import (
    QWidget, QVBoxLayout, QHBoxLayout, QTableWidget, QTableWidgetItem,
    QHeaderView, QLabel, QComboBox, QPushButton
)
from PySide6.QtCore import Qt
from PySide6.QtGui import QColor, QFont

from models.schedule import Schedule, DayOfWeek
from utils.time_utils import TimeUtils


class TimetableWidget(QWidget):
    """Widget for displaying weekly timetable"""
    
    def __init__(self):
        super().__init__()
        self.current_schedule: Schedule = None
        self._setup_ui()
        self._apply_styles()
    
    def _setup_ui(self):
        """Setup the UI components"""
        layout = QVBoxLayout()
        layout.setSpacing(10)
        layout.setContentsMargins(20, 20, 20, 20)
        
        # Header with year selector
        header_layout = QHBoxLayout()
        
        year_label = QLabel("Select Class:")
        header_layout.addWidget(year_label)
        
        self.year_combo = QComboBox()
        self.year_combo.addItems(["Year 1", "Year 2", "Year 3", "Year 4"])
        header_layout.addWidget(self.year_combo)
        
        header_layout.addStretch()
        
        self.generate_button = QPushButton("Create Schedule")
        header_layout.addWidget(self.generate_button)
        
        layout.addLayout(header_layout)
        
        # Timetable table
        self.table = QTableWidget()
        self.table.setRowCount(len(TimeUtils.TIME_SLOTS))
        self.table.setColumnCount(5)  # 5 days
        
        # Set headers
        days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
        self.table.setHorizontalHeaderLabels(days)
        
        # Set time slot labels
        time_labels = []
        for start, end in TimeUtils.TIME_SLOTS:
            time_labels.append(f"{start.strftime('%H:%M')}-{end.strftime('%H:%M')}")
        self.table.setVerticalHeaderLabels(time_labels)
        
        # Configure table
        self.table.setEditTriggers(QTableWidget.NoEditTriggers)
        self.table.setSelectionBehavior(QTableWidget.SelectItems)
        self.table.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.table.verticalHeader().setSectionResizeMode(QHeaderView.ResizeToContents)
        self.table.setAlternatingRowColors(False) 
        
        layout.addWidget(self.table)
        
        self.setLayout(layout)
    
    def _apply_styles(self):
        """
        Apply modern styling with design colors.
        CRITICAL FIX: 'color' attribute removed from generic selectors 
        to allow Python code to set text colors dynamically.
        """
        self.setStyleSheet("""
            QTableWidget {
                border: 1px solid #dbe2ec;
                border-radius: 8px;
                background-color: white;
                gridline-color: #dbe2ec;
            }
            QTableWidget::item {
                padding: 8px;
                border: none;
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
            QComboBox {
                padding: 8px;
                border: 2px solid #dbe2ec;
                border-radius: 5px;
                background-color: white;
                color: #0f1b40;
                min-width: 150px;
            }
            QComboBox:focus {
                border-color: #f4bd2a;
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
            QLabel {
                color: #0f1b40;
            }
        """)
    
    def display_schedule(self, schedule: Schedule):
        """
        Matrix Yöntemi ile Program Gösterimi.
        - Aynı slotta birden fazla FARKLI ders varsa: DersA/DersB formatında + KIRMIZI renk
        - Ortak Dersleri Koyu Hardal (#b9770e)
        - Normal Dersleri Lacivert (#0f1b40)
        """
        self.current_schedule = schedule
        
        # 1. Tabloyu Temizle
        self.table.clearContents() 
        # Spanları sıfırla
        for r in range(self.table.rowCount()):
            for c in range(self.table.columnCount()):
                self.table.setSpan(r, c, 1, 1)

        days_list = [
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        ]
        
        # -----------------------------------------------------------
        # ADIM 1: SANAL MATRİS OLUŞTUR (GRID)
        # Her slot için SADECE İLK HÜCREYE ekle (çakışma tespiti için)
        # -----------------------------------------------------------
        rows = self.table.rowCount()
        cols = self.table.columnCount()
        grid = [[[] for _ in range(cols)] for _ in range(rows)]
        
        for slot in schedule.time_slots:
            if not slot.course: 
                continue
            
            start_row = TimeUtils.get_time_slot_index(slot.start_time)
            if start_row == -1: 
                continue
            
            try:
                col = days_list.index(slot.day)
            except ValueError: 
                continue
            
            # ÖNEMLİ: Dersi SADECE başlangıç hücresine ekle
            # Böylece aynı slotta farklı dersler tespit edilebilir
            grid[start_row][col].append(slot)

        # -----------------------------------------------------------
        # ADIM 2: EKRANA BAS (RENDER)
        # -----------------------------------------------------------
        ortak_dersler = ["MATH", "PHYS", "HIST", "TURK", "ENG", "BIO", "CHEM", "MAN", "ECON", "ESHS"]
        
        # İşlenmiş dersleri takip et
        processed_slots = set()

        for r in range(rows):
            for c in range(cols):
                slots_in_cell = grid[r][c]
                
                if not slots_in_cell:
                    continue
                
                # --- ÇAKIŞMA TESPİTİ ---
                # Aynı hücrede farklı ders kodları + section kombinasyonu varsa ÇAKIŞMA
                unique_courses = {}
                for s in slots_in_cell:
                    # Her dersi benzersiz yapan anahtar: kod-section-lab_durumu
                    course_key = f"{s.course.code}-{s.section}-{s.is_lab}"
                    if course_key not in unique_courses:
                        unique_courses[course_key] = s
                
                # FARKLI DERSLER varsa çakışma
                unique_course_codes = set(s.course.code for s in unique_courses.values())
                is_conflict = len(unique_course_codes) > 1
                
                # Eğer çakışma YOKSA ve bu slot daha önce işlenmişse atla
                primary_slot = slots_in_cell[0]
                if not is_conflict and id(primary_slot) in processed_slots:
                    continue

                # --- METİN OLUŞTURMA ---
                if is_conflict:
                    # ÇAKIŞMA DURUMU: DersA/DersB formatı
                    conflict_texts = []
                    seen_codes = set()
                    
                    for slot in slots_in_cell:
                        code = slot.course.code
                        # Aynı kodu iki kez ekleme (ama farklı section'lar olabilir)
                        if code in seen_codes:
                            continue
                        seen_codes.add(code)
                        
                        if slot.is_lab:
                            conflict_texts.append(f"{code} (Lab-{slot.section})")
                        else:
                            conflict_texts.append(f"{code}")
                    
                    final_text = " / ".join(conflict_texts)
                    
                    # Tüm çakışan slotları işlenmiş olarak işaretle
                    for s in slots_in_cell:
                        processed_slots.add(id(s))
                    
                else:
                    # NORMAL DURUM: Tek ders
                    slot = primary_slot
                    txt = f"{slot.course.code}"
                    if slot.is_lab:
                        txt += f" (Lab-{slot.section})"
                    else:
                        txt += f"\n{slot.course.name[:15]}"
                    
                    if slot.course.instructor:
                        txt += f"\n{slot.course.instructor}"
                    
                    final_text = txt
                    processed_slots.add(id(slot))

                # --- HÜCRE OLUŞTUR ---
                item = QTableWidgetItem(final_text)
                item.setTextAlignment(Qt.AlignCenter)
                item.setBackground(QColor("white"))

                # --- RENKLENDİRME ---
                font = QFont()
                
                if is_conflict:
                    # ÇAKIŞMA -> KIRMIZI + KALIN YAZI
                    item.setForeground(QColor("#ea220c"))
                    font.setBold(True)
                    item.setFont(font)
                    
                else:
                    # NORMAL DURUM
                    code = str(primary_slot.course.code).upper().strip()
                    if any(code.startswith(pre) for pre in ortak_dersler):
                        # ORTAK DERS -> KOYU HARDAL + KALIN YAZI
                        item.setForeground(QColor("#b9770e"))
                        font.setBold(True)
                        item.setFont(font)
                    else:
                        # BÖLÜM DERSİ -> LACİVERT
                        item.setForeground(QColor("#0f1b40"))
                        item.setFont(font)
                
                self.table.setItem(r, c, item)

                # --- AKILLI SPAN (SADECE ÇAKIŞMA YOKSA) ---
                if not is_conflict:
                    slot = primary_slot
                    duration = (slot.end_time.hour * 60 + slot.end_time.minute) - \
                               (slot.start_time.hour * 60 + slot.start_time.minute)
                    hours = (duration + 49) // 50
                    
                    # Aşağıdaki hücrelerin güvenli olup olmadığını kontrol et
                    safe_span = 1
                    for h in range(1, hours):
                        if r + h < rows:
                            next_cell_slots = grid[r + h][c]
                            # Aşağısı boş olmalı (çünkü artık sadece ilk hücreye ekliyoruz)
                            if not next_cell_slots:
                                safe_span += 1
                            else:
                                break  # Aşağıda başka ders var, dur!
                    
                    if safe_span > 1:
                        self.table.setSpan(r, c, safe_span, 1)

    def _on_year_changed(self, index: int):
        pass
    
    def _on_generate_clicked(self):
        pass
    
    def get_selected_year(self) -> int:
        return self.year_combo.currentIndex() + 1