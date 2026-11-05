# English Trick - Mobile App

Aplikasi Android untuk tes dan pembelajaran bahasa Inggris berbasis Jetpack Compose. Aplikasi ini mencakup 4 kategori pengujian: **Listening**, **Speaking**, **Reading**, dan **Writing** dengan berbagai tingkat kesulitan (Beginner, Intermediate, Advanced).

## Fitur Utama
- **Listening Test**: Latihan mendengarkan menggunakan Text-to-Speech (TTS).
- **Speaking Test**: Latihan berbicara menggunakan Speech-to-Text / Speech Recognizer.
- **Reading Test**: Latihan membaca dan memahami teks bacaan.
- **Writing Test**: Latihan menyusun dan menulis kalimat dalam bahasa Inggris.
- **Result & Review**: Tampilan skor, akurasi, dan pembahasan jawaban.

---

## Prasyarat (Prerequisites)
- **Android Studio**: Android Studio Hedgehog / Iguana / Koala / Ladybug atau versi terbaru.
- **JDK**: Java 17 atau 21.
- **Android SDK**:
  - Minimum SDK: 24 (Android 7.0 Nougat)
  - Target & Compile SDK: 35 / 37

---

## Cara Clone dan Menjalankan di Android Studio

1. **Clone repository ini:**
   ```bash
   git clone https://github.com/Robinno11/English-Trick-Mobile.git
   ```

2. **Buka di Android Studio:**
   - Buka Android Studio.
   - Pilih **File > Open...** (atau **Open Project** di layar pembuka).
   - Arahkan ke folder hasil clone `AppMobileEnglishTrick`.
   - Tunggu proses Gradle Sync selesai secara otomatis.

3. **Jalankan Aplikasi:**
   - Sambungkan perangkat fisik Android (aktifkan USB Debugging) atau gunakan Android Emulator.
   - Klik tombol **Run** (`▶`) di Android Studio.

---

## Struktur Proyek
```
AppMobileEnglishTrick/
├── app/
│   ├── src/main/java/com/example/appmobileenglishtrick/
│   │   ├── audio/           # Pengaturan TTS & Speech Input
│   │   ├── data/            # Data model dan repository soal
│   │   ├── ui/              # Screen Compose & UI components
│   │   └── MainActivity.kt  # Entry point aplikasi
│   └── src/main/res/        # Asset drawable, icon, colors, strings
├── gradle/                  # Gradle wrapper & version catalog
├── build.gradle.kts
└── settings.gradle.kts
```
