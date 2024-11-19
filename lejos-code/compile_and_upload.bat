@echo off
echo *** Compiling Java File ***
nxjc Main.java
if %errorlevel% neq 0 (
    echo Fehler beim Kompilieren.
    pause
    exit /b
)

echo *** Linking NXJ File ***
nxjlink -o Main.nxj Main
if %errorlevel% neq 0 (
    echo Fehler beim Linken.
    pause
    exit /b
)

echo *** Uploading to NXT ***
nxjupload -r Main.nxj
if %errorlevel% neq 0 (
    echo Fehler beim Hochladen.
    pause
    exit /b
)

echo *** Fertig! ***
pause
