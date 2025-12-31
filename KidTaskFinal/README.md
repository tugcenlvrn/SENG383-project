# KidTask - Task Management System

Modern, dosya tabanlı görev yönetim masaüstü uygulaması. Glassmorphism tasarımı ile çocuklar, ebeveynler ve öğretmenler için kapsamlı bir görev takip sistemi.

## Özellikler

### 🎯 Kid (Çocuk) Dashboard
- **My Tasks**: Atanan görevleri görüntüleme ve tamamlama
- **Wishes**: Yeni dilek ekleme
- **Achievements**: Kazanılan rozetleri görüntüleme
- **Schedule**: Görevlerin son tarihlerini takvim görünümünde görme
- **Level & Progress**: Seviye ve ilerleme çubuğu

### 👨‍👩‍👧 Parent (Ebeveyn) Dashboard
- **Assign Task**: Eve özel görev atama
- **Approval Center**: Çocuğun tamamladığı görevleri onaylama/reddetme
- **Show Wishes**: Çocuğun eklediği dilekleri görüntüleme ve onaylama
- **Add Achievement**: Çocuğu motive edecek ödüller tanımlama
- **Family Progress**: Aile ilerleme çubuğu

### 👩‍🏫 Teacher (Öğretmen) Dashboard
- **Add School Task**: Sınıfa yönelik akademik görev ekleme
- **Rate Tasks**: Çocuğun tamamladığı okul görevlerini yıldız ile puanlama (1-5)
- **Add Achievement**: Okul başarımları ekleme
- **Class Average**: Sınıf ortalaması ilerleme çubuğu

## Teknik Özellikler

- **Dosya Tabanlı Veri Saklama**: Veritabanı kullanılmaz, tüm veriler TXT dosyalarında tutulur
- **Gerçek Zamanlı Senkronizasyon**: Bir rolün yaptığı değişiklik diğer rolde anında görünür
- **Glassmorphism Tasarım**: Modern, yarı saydam UI tasarımı
- **3 Farklı Tema**: 
  - Kid: Pastel Turuncu/Şeftali
  - Parent: Pastel Nane Yeşili
  - Teacher: Pastel Mor/Lila

## Gereksinimler

- Java 17 veya üzeri
- Maven 3.6+
- JavaFX 17

## Kurulum

1. Projeyi klonlayın veya indirin
2. Maven bağımlılıklarını yükleyin:
```bash
mvn clean install
```

## Çalıştırma

### Maven ile:
```bash
mvn javafx:run
```

### IDE ile:
- `KidTaskApplication.java` dosyasını main class olarak çalıştırın

## Veri Dosyaları

Uygulama `data/` klasöründe şu dosyaları kullanır:
- `Users.txt`: Kullanıcı bilgileri
- `Tasks.txt`: Görevler
- `Wishes.txt`: Dilekler
- `Achievements.txt`: Başarımlar

## İlk Kullanım

Uygulama ilk açıldığında veri dosyaları otomatik oluşturulur. Test için örnek kullanıcılar ekleyebilirsiniz:

**Users.txt formatı:**
```
username;password;role;level;currentPoints;totalExperience
```

**Örnek:**
```
kid1;pass123;KID;1;0;0
parent1;pass123;PARENT;0;0;0
teacher1;pass123;TEACHER;0;0;0
```

## Proje Yapısı

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── kidtask/
│   │           ├── models/          # Veri modelleri
│   │           ├── views/           # UI ekranları
│   │           ├── controllers/     # İş mantığı (gelecekte)
│   │           ├── utils/           # Yardımcı sınıflar
│   │           └── KidTaskApplication.java
│   └── resources/
│       ├── css/
│       │   └── style.css           # Tüm stiller
│       └── images/                 # İkonlar (placeholder)
data/                               # Veri dosyaları
```

## Notlar

- Uygulama çalışması için `src/main/resources/images/` klasörüne placeholder ikonlar eklenebilir (opsiyonel)
- Tüm veriler `data/` klasöründe saklanır
- Logout yapıldığında veriler yeniden yüklenir (senkronizasyon)

## Lisans

Bu proje eğitim amaçlı geliştirilmiştir.



