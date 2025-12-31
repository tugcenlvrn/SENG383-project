# JavaFX Kurulum Rehberi

## Yöntem 1: Maven ile (Önerilen)

### Adım 1: Maven Kurulumu

1. **Maven İndir:**
   - https://maven.apache.org/download.cgi adresinden Maven'i indirin
   - `apache-maven-3.9.x-bin.zip` dosyasını indirin

2. **Maven Kurulumu:**
   - İndirdiğiniz zip dosyasını bir klasöre çıkarın (örn: `C:\Program Files\Apache\maven`)
   - Sistem değişkenlerine ekleyin:
     - `MAVEN_HOME` = `C:\Program Files\Apache\maven`
     - `PATH`'e ekleyin: `%MAVEN_HOME%\bin`

3. **Kontrol:**
   ```bash
   mvn --version
   ```

### Adım 2: Bağımlılıkları Yükle

Proje klasöründe şu komutu çalıştırın:
```bash
mvn clean install
```

Bu komut JavaFX bağımlılıklarını otomatik olarak indirecektir.

---

## Yöntem 2: IDE ile (IntelliJ IDEA / Eclipse / VS Code)

### IntelliJ IDEA:

1. Projeyi açın
2. `File` > `Project Structure` > `Libraries`
3. `+` butonuna tıklayın
4. `From Maven...` seçin
5. Şu bağımlılıkları ekleyin:
   - `org.openjfx:javafx-controls:17.0.2`
   - `org.openjfx:javafx-fxml:17.0.2`

### Eclipse:

1. Projeyi sağ tıklayın > `Configure` > `Convert to Maven Project`
2. Eclipse otomatik olarak `pom.xml`'i okuyup bağımlılıkları indirecektir
3. Eğer indirmezse: `Project` > `Maven` > `Update Project`

### VS Code:

1. Java Extension Pack yüklü olmalı
2. `pom.xml` dosyasını açın
3. VS Code otomatik olarak bağımlılıkları indirmeyi önerecektir
4. Veya terminalde: `mvn clean install`

---

## Yöntem 3: Manuel JavaFX SDK Kurulumu

Eğer Maven kullanmak istemiyorsanız:

1. **JavaFX SDK İndir:**
   - https://openjfx.io/ adresinden JavaFX 17 SDK'yı indirin
   - Windows için `JavaFX Windows x64 SDK` seçin

2. **SDK'yı Çıkar:**
   - Bir klasöre çıkarın (örn: `C:\javafx-sdk-17.0.2`)

3. **IDE'de Modül Path Ekle:**
   - IntelliJ IDEA: `File` > `Project Structure` > `Libraries` > `+` > `Java` > SDK klasörünü seç
   - Eclipse: `Project Properties` > `Java Build Path` > `Libraries` > `Modulepath` > `Add External JARs...`

4. **VM Options Ekle:**
   - IntelliJ IDEA: `Run` > `Edit Configurations` > `VM options`:
     ```
     --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml
     ```
   - Eclipse: `Run Configurations` > `Arguments` > `VM arguments`:
     ```
     --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml
     ```

---

## Hızlı Test

Kurulumdan sonra projeyi çalıştırmak için:

```bash
mvn javafx:run
```

Veya IDE'de `KidTaskApplication.java` dosyasını `Run` edin.

---

## Sorun Giderme

### "JavaFX runtime components are missing" hatası:
- VM options'da `--module-path` ve `--add-modules` parametrelerini eklediğinizden emin olun

### "Cannot resolve symbol javafx" hatası:
- IDE'yi yeniden başlatın
- `File` > `Invalidate Caches / Restart` (IntelliJ)
- Maven projesini yenileyin: `mvn clean install`

### Maven bulunamıyor:
- PATH değişkenini kontrol edin
- Terminal'i yeniden açın
- Maven kurulumunu doğrulayın: `mvn --version`

