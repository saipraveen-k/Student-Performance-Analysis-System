# 🚀 Updated Run Guide: Student Performance Analysis System

Simplified instructions using batch files (Windows CMD/PowerShell compatible). Project is pre-compiled in `bin/`.

## ✅ Prerequisites
- **Java JDK 25+** (system PATH preferred: `java -version`)
- **JavaFX SDK 17** (bundled: `javafx/javafx-sdk-17.0.2`)
- **PostgreSQL** running on `localhost:5432`, user `postgres`/pass `root`
  - Create DB: `CREATE DATABASE student_performance_db;`
  - Run `psql -U postgres -d student_performance_db -f database/schema.sql`

## 🚀 Quick Run (Recommended)

**From project root** (`c:/tempp/projects/Student-Performance-Analysis-System/Student-Performance-Analysis-System`):

1. **Test DB**: `.\test_db.bat`
   - Verifies connection, adds test data.

2. **Launch App**: `.\run.bat`
   - Starts JavaFX UI. Ignore JavaFX warnings.

**Stop**: Ctrl+C in terminal.

## 🔧 Manual Run (if .bat fail)

Use system `java` (if JDK25 in PATH):

```
java --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;postgresql-42.7.9.jar" com.studentanalysis.MainApp
```

**Specific JDK path** (e.g., `E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe` replace above `java`):

```
\"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe\" --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;postgresql-42.7.9.jar" com.studentanalysis.MainApp
```

## 📋 .bat Contents

**test_db.bat**:
```
@echo off
\"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe\" -cp \"bin;postgresql-42.7.9.jar\" com.studentanalysis.TestDBConnection
pause
```

**run.bat**:
```
@echo off
xcopy \"src\main\resources\" \"bin\" /E /I /Y >nul 2>&1
\"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe\" --module-path \"javafx\javafx-sdk-17.0.2\lib\" --add-modules javafx.controls,javafx.fxml -cp \"bin;postgresql-42.7.9.jar\" com.studentanalysis.MainApp
pause
```

**Update paths**: Edit .bat files for your JDK location.

## 🛠️ Recompile (if source changes)

```
dir /s /b *.java > sources.txt
\"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\javac.exe\" -d bin -cp \"postgresql-42.7.9.jar\" --module-path \"javafx/javafx-sdk-17.0.2/lib\" --add-modules javafx.controls,javafx.fxml @sources.txt
xcopy \"src\main\resources\" \"bin\" /E /I /Y
```

## ❓ Troubleshooting
| Issue | Solution |
|-------|----------|
| `java not found` | Add JDK25 `bin` to PATH |
| DB fail | Start PostgreSQL, create DB/schema |
| Module warnings | Normal/Ignore |
| No UI | Check JavaFX path, JDK version |

