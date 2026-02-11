# Architecture Documentation

## System Architecture

The Student Performance Analysis System follows a layered architecture pattern to ensure separation of concerns, modularity, and maintainability.

### Layers

#### 1. Presentation Layer (UI Layer)
- **Technology:** JavaFX with FXML and CSS
- **Components:** Controllers, FXML views, CSS stylesheets
- **Responsibilities:**
  - User interface rendering
  - Input validation
  - Event handling
  - Data binding to UI components

#### 2. Business Logic Layer (Service Layer)
- **Technology:** Pure Java
- **Components:** Service classes (StudentService, SubjectService, MarksService, AnalysisService)
- **Responsibilities:**
  - Business rule enforcement
  - Data processing and calculations
  - Orchestration between UI and data layers
  - Validation logic

#### 3. Data Access Layer (DAO Layer)
- **Technology:** JDBC
- **Components:** DAO classes (StudentDAO, SubjectDAO, MarksDAO)
- **Responsibilities:**
  - Database operations (CRUD)
  - SQL query execution
  - Connection management
  - Data mapping

#### 4. Database Layer
- **Technology:** PostgreSQL
- **Components:** Tables, constraints, indexes
- **Responsibilities:**
  - Data persistence
  - Referential integrity
  - Query optimization

### Data Flow

```
User Input → Controller → Service → DAO → Database
      ↓
   Response ← Controller ← Service ← DAO ← Database
```

### Design Patterns Used

- **MVC (Model-View-Controller):** Separates data, UI, and control logic
- **DAO Pattern:** Abstracts data access operations
- **Service Layer Pattern:** Encapsulates business logic
- **Factory Pattern:** For service instantiation
- **Observer Pattern:** For UI updates (JavaFX bindings)

### Component Diagram

```
[JavaFX UI]
    |
[Controllers]
    |
[Services] ←→ [DAOs]
    |
[PostgreSQL DB]
```

### Security Considerations

- Input validation at multiple layers
- Parameterized SQL queries to prevent injection
- Connection pooling for resource management
- No sensitive data storage in application

### Scalability

- Modular design allows for easy extension
- Service layer can be distributed
- Database can be scaled independently
- UI can be enhanced without affecting backend
