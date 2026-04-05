@echo off
REM Copy resources if needed
xcopy "src\main\resources" "bin" /E /I /Y >nul 2>&1

REM Run the application
"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe" --module-path "javafx\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp "bin;postgresql-42.7.9.jar" com.studentanalysis.MainApp

pause
