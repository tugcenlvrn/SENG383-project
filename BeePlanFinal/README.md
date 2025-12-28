# BeePlan - Ders Programı Oluşturma Sistemi

BeePlan, Çankaya Üniversitesi Yazılım Mühendisliği Bölümü için geliştirilmiş profesyonel bir ders programı oluşturma uygulamasıdır.

## Özellikler

- 🎯 **Çakışmasız Program Oluşturma**: Otomatik çakışma kontrolü ile ders programı oluşturma
- 📊 **Görsel Zaman Çizelgesi**: Haftalık ders programını görsel olarak görüntüleme
- ✅ **Kural Doğrulama**: Tüm kısıtlamaları kontrol eden doğrulama sistemi
- 📝 **Detaylı Raporlar**: Program doğrulama raporları
- 💾 **CSV Desteği**: Ders bilgilerini CSV dosyalarından yükleme

## Kurulum

1. Gerekli paketleri yükleyin:
```bash
pip install -r requirements.txt
```

**Windows'ta DLL hatası alıyorsanız:**
- `install_requirements.bat` dosyasını çalıştırın
- Veya `python test_pyside6.py` ile PySide6'nın düzgün yüklendiğini test edin
- Detaylı çözümler için `TROUBLESHOOTING.md` dosyasına bakın

2. Uygulamayı çalıştırın:
```bash
python main.py
```

## Giriş Bilgileri

- **E-posta**: admin@cankaya.edu.tr
- **Şifre**: 123456

## Kullanım

1. Giriş yapın
2. Sınıf seçin (1-4)
3. "Program Oluştur" butonuna tıklayın
4. Oluşturulan programı görüntüleyin
5. "Raporu Görüntüle" butonu ile doğrulama raporunu inceleyin

## Programlama Kuralları

- Cuma 13:20-15:10 aralığına ders konulamaz (Sınav Bloğu)
- Bir öğretim elemanı günde en fazla 4 saat teori dersi verebilir
- Lab dersleri teorik derslerden sonra olmalıdır
- 3. sınıf teknik seçmeli dersleri çakışmamalıdır
- CENG ve SENG seçmeli dersleri çakışmamalıdır
- Lab kapasitesi en fazla 40 öğrencidir

## Proje Yapısı

```
BeePlan/
├── main.py                 # Ana uygulama giriş noktası
├── models/                 # Veri modelleri
│   ├── course.py
│   ├── instructor.py
│   ├── schedule.py
│   └── classroom.py
├── views/                  # GUI bileşenleri
│   ├── login_window.py
│   ├── main_window.py
│   ├── timetable_widget.py
│   └── report_dialog.py
├── controllers/            # İş mantığı kontrolcüleri
│   ├── data_controller.py
│   └── schedule_controller.py
├── utils/                  # Yardımcı fonksiyonlar
│   ├── csv_loader.py
│   └── time_utils.py
└── data/                   # CSV veri dosyaları
    ├── common_courses.csv
    └── department_curriculum.csv
```

## Teknolojiler

- **Python 3.8+**
- **PySide6**: Modern GUI framework
- **CSV**: Veri yönetimi

## Lisans

Bu proje Çankaya Üniversitesi için geliştirilmiştir.

