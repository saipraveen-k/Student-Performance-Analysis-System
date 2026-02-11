# 🎓 Student Performance Analysis System

A Java-based desktop application that analyzes and evaluates student academic performance using structured data, automated analysis, and visual insights.

---

## 📌 Project Overview

The **Student Performance Analysis System** is designed to help teachers and academic staff move beyond simple marks storage. It provides automated performance analysis, identifies weak students and subjects, predicts ranks, and visualizes results using graphs. Key features include:

- **Student and Subject Management:** Easily add, update, and manage student profiles and subject details.
- **Marks Entry and Validation:** Secure entry of marks with built-in validation to prevent errors.
- **Automated Analysis:** Calculate averages, identify top performers, and detect underperforming areas.
- **Weak Subject Detection:** Highlight subjects where students struggle most, enabling targeted interventions.
- **Rank Prediction:** Predict student rankings based on current performance trends.
- **Graphical Visualizations:** Display performance data through charts and graphs for intuitive understanding.
- **Data Integrity:** Ensure accurate data handling with relational database constraints.

The system reduces manual effort, avoids calculation errors, and supports data-driven academic decision-making. By transforming raw marks into actionable insights, it empowers educators to improve teaching strategies, provide personalized support, and enhance overall learning outcomes.

---

## ❓ Problem Statement

In traditional academic systems, student marks are often stored in spreadsheets or basic databases without deeper analysis. Teachers face several challenges:

- **Lack of Insights:** Marks are recorded but not processed to reveal trends, such as declining performance in specific subjects or overall class averages.
- **Manual Identification of Issues:** Identifying weak students or subjects requires tedious manual calculations, leading to inefficiencies and potential oversights.
- **Error-Prone Processes:** Human errors in calculations can skew results, affecting decisions on student support or curriculum adjustments.
- **No Predictive Capabilities:** Without analysis tools, predicting future performance or rankings is impossible.
- **Limited Visualization:** Data is not presented in visual formats, making it hard to communicate findings to stakeholders.

This system addresses these issues by automating analysis, providing real-time insights, and offering visual tools to make data accessible and actionable, ultimately fostering a more effective educational environment.

---

## 🏗️ Architecture Plan

The system follows a layered architecture to ensure separation of concerns, modularity, scalability, and maintainability. It is divided into three main layers:

### 1. **Presentation Layer (UI Layer)**
   - **Technology:** JavaFX with FXML for layout and CSS for styling.
   - **Components:** Controllers handle user interactions, bind data to UI elements, and communicate with the business logic layer.
   - **Responsibilities:** Render the user interface, validate user inputs, display results (e.g., graphs, tables), and provide navigation between modules.
   - **Interactions:** Receives user inputs and sends them to the business logic layer; displays data retrieved from the service layer.

### 2. **Business Logic Layer (Service Layer)**
   - **Technology:** Pure Java classes implementing application logic.
   - **Components:** Service classes (e.g., StudentService, AnalysisService) encapsulate business rules, perform calculations, and orchestrate data operations.
   - **Responsibilities:** Process data (e.g., calculate averages, predict ranks), enforce business rules (e.g., validation logic), and coordinate between the UI and data access layers.
   - **Interactions:** Receives requests from controllers, processes them using data from the DAO layer, and returns processed results.

### 3. **Data Access Layer (DAO Layer)**
   - **Technology:** JDBC for database connectivity.
   - **Components:** DAO classes (e.g., StudentDAO, MarksDAO) handle CRUD operations.
   - **Responsibilities:** Abstract database interactions, execute SQL queries, manage connections, and ensure data integrity.
- **Interactions:** Receives data operation requests from the service layer and interacts directly with the PostgreSQL database.

### Overall Architecture Diagram
```
JavaFX User Interface (FXML/CSS)
          ↓ (User Events/Data Binding)
    Controllers (Handle UI Logic)
          ↓ (Service Calls)
    Service Layer (Business Logic)
          ↓ (DAO Calls)
    DAO Layer (JDBC Queries)
          ↓ (SQL Operations)
    PostgreSQL Database (PostgreSQL Server)
```

This layered approach promotes:
- **Modularity:** Each layer can be developed and tested independently.
- **Scalability:** Easy to add new features or scale components.
- **Maintainability:** Changes in one layer minimally affect others.
- **Security:** Data access is abstracted, reducing direct exposure.

---

## 🔄 Data Flow

The data flow in the system follows a request-response pattern, ensuring smooth interaction between layers:

1. **User Input:** User interacts with the JavaFX UI (e.g., enters student marks or requests analysis).
2. **UI to Controller:** Controllers capture inputs, perform basic validation, and invoke service methods.
3. **Controller to Service:** Services process business logic (e.g., calculate performance metrics) and call DAO methods for data retrieval/storage.
4. **Service to DAO:** DAOs execute database operations (e.g., INSERT marks, SELECT student data) using JDBC.
5. **DAO to Database:** Data is stored/retrieved from PostgreSQL tables.
6. **Response Flow:** Results flow back: Database → DAO → Service → Controller → UI, where visualizations or confirmations are displayed.

### Data Flow Diagram (ASCII)
```
User Action (e.g., Enter Marks)
       ↓
JavaFX UI (Input Validation)
       ↓
Controller (Process Request)
       ↓
Service (Business Logic, e.g., Validate & Calculate)
       ↓
DAO (CRUD Operations)
       ↓
PostgreSQL DB (Store/Retrieve Data)
       ↓
DAO (Return Results)
       ↓
Service (Process Results)
       ↓
Controller (Prepare UI Data)
       ↓
JavaFX UI (Display Graphs/Tables)
```

This flow ensures data integrity, minimizes errors, and provides real-time feedback to users.


---


## 🧩 Modules

The system is organized into modular components, each handling specific functionalities:

- **Student Management Module:** Allows adding, editing, and deleting student records, including personal details like name, ID, and contact information.
- **Subject Management Module:** Manages subject details such as subject names, codes, and associated teachers, enabling dynamic curriculum updates.
- **Marks Entry Module:** Provides a secure interface for entering and updating student marks per subject, with validation to ensure marks are within acceptable ranges.
- **Performance Analysis Module:** Computes key metrics like averages, pass/fail rates, and trends across students and subjects.
- **Weak Subject Detection Module:** Identifies subjects with the lowest average scores or highest failure rates, flagging areas needing attention.
- **Rank Prediction Module:** Uses current marks to predict student rankings based on algorithms (e.g., weighted averages), helping forecast academic standings.
- **Graph Visualization Module:** Generates charts (e.g., bar graphs for subject performance, line graphs for trends) using JavaFX charting libraries for easy interpretation.


---


## 🛠️ Technology Stack


- **Programming Language:** Java  
- **User Interface:** JavaFX  
- **Database:** PostgreSQL
- **Database Server:** PostgreSQL Server
- **Database Connectivity:** JDBC (Add PostgreSQL JDBC driver to classpath, e.g., postgresql-42.7.9.jar)
- **IDE:** VS Code / Eclipse  


---


## 🗄️ Database Design

The database is designed using PostgreSQL with a relational model to store and manage student performance data efficiently. It consists of three main tables with defined relationships to maintain data integrity.

### Tables Used

- **`students`**:
  - **Purpose:** Stores student personal information.
  - **Schema:**
    - `student_id` (INT, PRIMARY KEY, AUTO_INCREMENT): Unique identifier for each student.
    - `name` (VARCHAR(100), NOT NULL): Full name of the student.
    - `email` (VARCHAR(100), UNIQUE): Email address for communication.
    - `phone` (VARCHAR(15)): Contact phone number.
    - `enrollment_date` (DATE): Date when the student was enrolled.

- **`subjects`**:
  - **Purpose:** Manages subject details offered in the curriculum.
  - **Schema:**
    - `subject_id` (INT, PRIMARY KEY, AUTO_INCREMENT): Unique identifier for each subject.
    - `subject_name` (VARCHAR(100), NOT NULL): Name of the subject (e.g., Mathematics).
    - `subject_code` (VARCHAR(10), UNIQUE, NOT NULL): Short code for the subject (e.g., MATH101).
    - `credits` (INT): Number of credits assigned to the subject.

- **`marks`**:
  - **Purpose:** Records marks obtained by students in various subjects.
  - **Schema:**
    - `mark_id` (INT, PRIMARY KEY, AUTO_INCREMENT): Unique identifier for each mark entry.
    - `student_id` (INT, FOREIGN KEY REFERENCES students(student_id)): Links to the student.
    - `subject_id` (INT, FOREIGN KEY REFERENCES subjects(subject_id)): Links to the subject.
    - `marks_obtained` (DECIMAL(5,2)): Marks scored (e.g., out of 100).
    - `total_marks` (DECIMAL(5,2)): Total possible marks for the assessment.
    - `exam_date` (DATE): Date of the examination.

### Relationships
- **One-to-Many:** A student can have multiple marks (one per subject), and a subject can have marks from multiple students.
- **Foreign Keys:** `marks.student_id` references `students.student_id`; `marks.subject_id` references `subjects.subject_id`.
- **Constraints:** Primary keys ensure uniqueness; foreign keys enforce referential integrity; NOT NULL and UNIQUE constraints prevent invalid data entry.

This design avoids redundancy through normalization and supports efficient queries for analysis and reporting.


---


## 📁 Project Structure

The project follows a standard Maven-like structure for Java applications, ensuring organized code separation and easy navigation.

```
Student-Performance-Analysis-System/
│
├── README.md                          # Project documentation and setup guide
├── src/
│   ├── main/
│   │   ├── java/com/studentanalysis/   # Main source code package
│   │   │   ├── controller/             # JavaFX controllers for UI logic and event handling
│   │   │   ├── service/                # Business logic services (e.g., analysis calculations)
│   │   │   ├── dao/                    # Data Access Objects for database interactions
│   │   │   ├── model/                  # POJO classes representing data entities (e.g., Student, Subject)
│   │   │   └── util/                   # Utility classes (e.g., DBConnection for JDBC setup)
│   │   └── resources/                  # Non-Java resources
│   │       ├── fxml/                   # FXML files defining UI layouts
│   │       └── css/                    # Stylesheets for UI customization
│
├── database/
│   └── schema.sql                      # SQL script to create and populate database tables
│
└── docs/
    ├── architecture-diagram.png        # Visual diagram of system architecture
    └── er-diagram.png                  # Entity-Relationship diagram of the database
```

- **src/main/java/com/studentanalysis/**: Core application code, divided into packages for separation of concerns.
- **src/main/resources/**: Static resources like UI definitions and styles.
- **database/**: Contains database setup scripts for easy initialization.
- **docs/**: Documentation assets for understanding the system design.



---


## ▶️ How to Run the Project

Follow these steps to set up and run the Student Performance Analysis System on your local machine.

### Prerequisites
- **Java JDK (25):** Ensure JDK is installed and JAVA_HOME is set to `E:\java\clg`.
- **JavaFX SDK (25):** Ensure JavaFX SDK is installed in `E:\java\clg\javafx-sdk-25`.
- **PostgreSQL:** Install PostgreSQL server. Download from [PostgreSQL Official Site](https://www.postgresql.org/download/).
- **VS Code or Eclipse IDE:** Recommended for editing and running the project. Install Java extensions for VS Code.

### Setup Steps

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/sai-praveen-k/student-performance-analysis-system.git
   cd student-performance-analysis-system
   ```

2. **Set Up the Database:**
   - Install and start PostgreSQL server.
   - Use psql or pgAdmin to create a new database named `student_performance_db`.
   - Run the `database/schema.sql` script in the database to create tables and insert sample data.

3. **Configure Database Connection:**
   - Open `src/main/java/com/studentanalysis/util/DBConnection.java`.
   - Update the database URL, username, and password to match your PostgreSQL setup (default: username `postgres`, password as set).

4. **Open and Run the Project:**
   - Open the project in VS Code or Eclipse.
   - Ensure JavaFX libraries are configured (add VM arguments if needed: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`).
   - Run `MainApp.java` as the main class.

### Usage Instructions

Once the application is running, follow these steps to use the system:

1. **Launch the Application:** The JavaFX window will open with a dashboard or main menu.

2. **Manage Students:**
   - Navigate to the Student Management module.
   - Add new students by entering name, email, phone, and enrollment date.
   - Edit or delete existing records as needed.

3. **Manage Subjects:**
   - Go to the Subject Management module.
   - Add subjects with name, code, and credits.
   - Update subject details for curriculum changes.

4. **Enter Marks:**
   - Access the Marks Entry module.
   - Select a student and subject, then input marks obtained and total marks.
   - The system validates inputs (e.g., marks cannot exceed total).

5. **Perform Analysis:**
   - Use the Performance Analysis module to view averages, trends, and pass/fail rates.
   - The Weak Subject Detection module highlights underperforming subjects.
   - Rank Prediction provides forecasted standings based on current data.

6. **Visualize Data:**
   - Switch to the Graph Visualization module.
   - Generate bar charts for subject performance or line graphs for trends.
   - Export or print visualizations if supported.

7. **Exit the Application:** Close the window to save data and exit.

### Troubleshooting Tips

- **Database Connection Issues:** Verify PostgreSQL is running. Check credentials in `DBConnection.java`. Ensure the database exists and the schema is applied.
- **JavaFX Errors:** Confirm JavaFX is installed and added to the classpath/module path. For JDK 11+, use the `--module-path` flag.
- **Port Conflicts:** If PostgreSQL port 5432 is in use, change it in PostgreSQL configuration and update the connection string.
- **Build Errors:** Clean and rebuild the project in your IDE. Ensure all dependencies are resolved.
- **Performance Issues:** For large datasets, optimize queries in DAO classes or increase JVM heap size.
- **Common Errors:** If "Class not found" occurs, check package imports. For UI not loading, verify FXML paths in resources.

If issues persist, check the console logs for detailed error messages.

📈 Future Enhancements

Machine Learning-based performance prediction

Student login dashboard

Attendance analysis integration

Export reports as PDF

Role-based access control

👨‍💻 Team Members

Sai Praveen Korubilli (25b21ai024)

Manoj Sri Jai Ram Karri (25b21ai006)

Lakshmikanth Sai Sidagam (25b21ai002)

Department of Artificial Intelligence & Machine Learning

🏁 Conclusion

The Student Performance Analysis System provides an efficient and scalable way to analyze academic performance.
It transforms raw marks into meaningful insights, helping educators improve learning outcomes.

📄 License

This project is developed for academic and educational purposes only.