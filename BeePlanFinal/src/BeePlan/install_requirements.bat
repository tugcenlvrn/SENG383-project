@echo off
echo BeePlan - Gereksinimler Yükleniyor...
echo.

REM Python sürümünü kontrol et
python --version
echo.

REM Eski PySide6 kurulumunu kaldır
echo Eski PySide6 kurulumu kaldırılıyor...
pip uninstall -y PySide6 PyQt6 PyQt5

echo.
echo PySide6 yükleniyor...
pip install --upgrade pip
pip install PySide6

echo.
echo Kurulum tamamlandı!
pause

