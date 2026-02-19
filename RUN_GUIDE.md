# 🚀 Run Guide: Student Performance Analysis System

This guide provides step-by-step instructions to compile and run the application on Windows using **PowerShell** or **Command Prompt (CMD)**.

## ✅ Prerequisites

Ensure you have the following installed and configured:

1.  **Java JDK 25**:
    *   **Path**: `E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2`
    *   *Verify*: `java -version`
2.  **JavaFX SDK 17.0.2**:
    *   **Path**: `javafx\javafx-sdk-17.0.2` (Located in project root)
3.  **PostgreSQL**:
    *   Running on `localhost:5432`
    *   **Username**: `postgres`
    *   **Password**: `root` (Default in `DBConnection.java`)

---

## 🗄️ Step 1: Database Setup

1.  Open your database management tool (pgAdmin or psql).
2.  Create the database:
    ```sql
    CREATE DATABASE student_performance_db;
    ```
3.  Execute the schema script located at `database/schema.sql` to create tables.

---

## 📝 Step 2: Generate Source List

Before compiling, we need to list all Java source files.

**PowerShell:**
```powershell
Get-ChildItem -Recurse -Filter *.java -Name | Out-File -Encoding ASCII sources.txt
```

**Command Prompt (CMD):**
```cmd
dir /s /b *.java > sources.txt
```

---

## ⚙️ Step 3: Run the Application

Execute the following commands from the **project root directory**:
`C:\tempp\projects\Student-Performance-Analysis-System\Student-Performance-Analysis-System`

### Option A: PowerShell (Recommended)

**1. Compile:**
```powershell
& "E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\javac.exe" -d bin -cp "src/main/java;lib/*;postgresql-42.7.9.jar" --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml '@sources.txt'
```

**2. Copy Resources:**
```powershell
Copy-Item -Path "src\main\resources\*" -Destination "bin" -Recurse -Force
```

**3. Run:**
```powershell
& "E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe" -cp "bin;lib/*;postgresql-42.7.9.jar" --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml com.studentanalysis.MainApp
```

### Option B: Command Prompt (CMD)

**1. Compile:**
```cmd
"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\javac.exe" -d bin -cp "src/main/java;lib/*;postgresql-42.7.9.jar" --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml @sources.txt
```

**2. Copy Resources:**
```cmd
xcopy "src\main\resources" "bin" /E /I /Y
```

**3. Run:**
```cmd
"E:\java\openjdk-25.0.2_windows-x64_bin\jdk-25.0.2\bin\java.exe" -cp "bin;lib/*;postgresql-42.7.9.jar" --module-path "javafx/javafx-sdk-17.0.2/lib" --add-modules javafx.controls,javafx.fxml com.studentanalysis.MainApp
```

---

## ❓ Troubleshooting

| Issue | Solution |
|-------|----------|
| **"File not found: sources.txt"** | Ensure you ran **Step 2** to generate the file list. |
| **"PostgreSQL JDBC Driver not found"** | Check if `postgresql-42.7.9.jar` is in the project root. |
| **"Database connection failed"** | Verify PostgreSQL service is running and credentials in `DBConnection.java` match your setup. |
| **"Module not found: javafx.controls"** | Ensure the `--module-path` points correctly to `javafx/javafx-sdk-17.0.2/lib`. |
