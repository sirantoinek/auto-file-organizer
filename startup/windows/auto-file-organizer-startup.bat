@echo off
rem ===================== file-organizer-startup.bat =====================
rem Place this file in: %APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup.
rem This will run Auto File Organizer automatically when Windows starts.

rem Set the path to your Auto File Organizer JAR file (CHANGE THIS PATH!)
set JAR_PATH=C:\Users\%USERNAME%\Desktop\auto-file-organizer-1.0.0.jar

rem Set the folder to organize (CHANGE THIS PATH!)
set ORGANIZE_FOLDER=C:\Users\%USERNAME%\Downloads

rem Set desired flags separated by a single space (SEE README.md FOR LIST OF VALID FLAGS)
rem Note: Auto File Organizer only organizes top-level by default.
set FLAGS=--by-type 

rem Check if JAR file exists
if not exist "%JAR_PATH%" (
    echo Auto File Organizer JAR not found at: %JAR_PATH%
    echo Please update the JAR_PATH in this batch file
    pause
    exit /b 1
)

rem Check if organize folder exists
if not exist "%ORGANIZE_FOLDER%" (
    echo Folder not found at: %ORGANIZE_FOLDER%
    echo Please update the ORGANIZE_FOLDER in this batch file
    pause
    exit /b 1
)

rem Run file organizer in auto mode
echo Starting File Organizer...
java -jar "%JAR_PATH%" "%ORGANIZE_FOLDER%" %FLAGS% --auto

rem Only show results if there was an error
if %ERRORLEVEL% neq 0 (
    echo Auto File Organizer encountered an error
    pause
    exit /b 1
)

rem Exit silently if successful