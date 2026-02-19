package com.studentanalysis.controller;

import com.studentanalysis.dao.StudentDAO;
import com.studentanalysis.dao.SubjectDAO;
import com.studentanalysis.dao.MarksDAO;
import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;
import com.studentanalysis.model.Marks;
import com.studentanalysis.service.AnalysisService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private TabPane mainTabPane;
    
    // Dashboard Controls
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalSubjectsLabel;
    @FXML private Label totalMarksLabel;
    @FXML private TextArea recentActivityTextArea;

    // Students Tab Controls
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentEmailColumn;
    @FXML private TableColumn<Student, String> studentPhoneColumn;
    @FXML private TableColumn<Student, String> studentEnrollmentDateColumn;
    @FXML private TableColumn<Student, Void> studentActionsColumn;

    // Subjects Tab Controls
    @FXML private TableView<Subject> subjectsTable;
    @FXML private TableColumn<Subject, Integer> subjectIdColumn;
    @FXML private TableColumn<Subject, String> subjectNameColumn;
    @FXML private TableColumn<Subject, String> subjectCodeColumn;
    @FXML private TableColumn<Subject, Integer> subjectCreditsColumn;
    @FXML private TableColumn<Subject, Void> subjectActionsColumn;

    // Marks Tab Controls
    @FXML private TableView<Marks> marksTable;
    @FXML private TableColumn<Marks, Integer> markIdColumn;
    @FXML private TableColumn<Marks, Integer> markStudentIdColumn;
    @FXML private TableColumn<Marks, Integer> markSubjectIdColumn;
    @FXML private TableColumn<Marks, Double> marksObtainedColumn;
    @FXML private TableColumn<Marks, Double> totalMarksColumn;
    @FXML private TableColumn<Marks, String> examDateColumn;
    @FXML private TableColumn<Marks, Void> markActionsColumn;

    // Analysis Tab Controls
    @FXML private BarChart<String, Number> subjectChart;
    @FXML private BarChart<String, Number> studentChart;

    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private MarksDAO marksDAO;
    private AnalysisService analysisService;

    public MainController() {
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
        this.marksDAO = new MarksDAO();
        this.analysisService = new AnalysisService();
    }

    @FXML
    public void initialize() {
        setupStudentTable();
        setupSubjectTable();
        setupMarksTable();
        
        refreshAllData();
        
        recentActivityTextArea.setText("Application started successfully.\nDatabase connection established.");
        
        // Add listener to refresh data when tabs are switched
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                refreshAllData();
            }
        });
    }

    private void refreshAllData() {
        updateDashboardStats();
        loadStudents();
        loadSubjects();
        loadMarks();
        updateCharts();
    }

    private void setupStudentTable() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        studentEnrollmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));
        
        setupDeleteColumn(studentActionsColumn, "student");
    }

    private void setupSubjectTable() {
        // Need to bind columns from FXML if they don't have fx:id, or update FXML. 
        // Assuming FXML is updated to include fx:ids for these columns or we grab them by index if needed.
        // For robustness, it's better to update FXML. But here I will try to binding assuming the FXML 
        // will be updated to match these injections or I will rely on the fact that I will update FXML next.
        
        // Actually, looking at the previous MainView.fxml read, the Subjects and Marks tables 
        // didn't have fx:ids for columns. I need to update MainView.fxml to match this controller's expectations 
        // or get columns by index. Let's get by index for now to avoid massive FXML rewrite if possible, 
        // or better, I will update FXML in the next step to add fx:ids which is cleaner.
        // BUT wait, I have already injected them with @FXML above. 
        // So I MUST update MainView.fxml to have these fx:ids.
    }

    private void setupMarksTable() {
        // Similar to subjects, will setup after FXML update.
    }

    // Helper to setup delete buttons
    private <T> void setupDeleteColumn(TableColumn<T, Void> column, String type) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    T item = getTableView().getItems().get(getIndex());
                    handleDelete(item, type);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void handleDelete(Object item, String type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + type);
        alert.setContentText("Are you sure you want to delete this " + type + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (type.equals("student")) {
                    studentDAO.deleteStudent(((Student) item).getStudentId());
                    loadStudents();
                } else if (type.equals("subject")) {
                    subjectDAO.deleteSubject(((Subject) item).getSubjectId());
                    loadSubjects();
                } else if (type.equals("mark")) {
                    marksDAO.deleteMarks(((Marks) item).getMarkId());
                    loadMarks();
                }
                updateDashboardStats();
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete " + type + ": " + e.getMessage());
            }
        }
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentsTable.setItems(FXCollections.observableArrayList(students));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load students: " + e.getMessage());
        }
    }

    private void loadSubjects() {
        try {
            List<Subject> subjects = subjectDAO.getAllSubjects();
            subjectsTable.setItems(FXCollections.observableArrayList(subjects));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load subjects: " + e.getMessage());
        }
    }

    private void loadMarks() {
        try {
            List<Marks> marks = marksDAO.getAllMarks();
            marksTable.setItems(FXCollections.observableArrayList(marks));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load marks: " + e.getMessage());
        }
    }

    private void updateDashboardStats() {
        try {
            totalStudentsLabel.setText(String.valueOf(studentDAO.getAllStudents().size()));
            totalSubjectsLabel.setText(String.valueOf(subjectDAO.getAllSubjects().size()));
            totalMarksLabel.setText(String.valueOf(marksDAO.getAllMarks().size()));
        } catch (SQLException e) {
            // Quiet fail or log
            e.printStackTrace();
        }
    }

    private void updateCharts() {
        try {
            // Subject Performance (Avg Marks)
            XYChart.Series<String, Number> subjectSeries = new XYChart.Series<>();
            subjectSeries.setName("Average Marks");
            List<Subject> subjects = subjectDAO.getAllSubjects();
            for (Subject subject : subjects) {
                double avg = marksDAO.getAverageMarksForSubject(subject.getSubjectId());
                subjectSeries.getData().add(new XYChart.Data<>(subject.getSubjectName(), avg));
            }
            subjectChart.getData().clear();
            subjectChart.getData().add(subjectSeries);

            // Student Performance (Avg Marks)
            XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
            studentSeries.setName("Average Marks");
            // Limit to top 10 for readability
            List<Student> students = studentDAO.getAllStudents();
            int count = 0;
            for (Student student : students) {
                if (count++ > 10) break; 
                double avg = marksDAO.getAverageMarksForStudent(student.getStudentId());
                studentSeries.getData().add(new XYChart.Data<>(student.getName(), avg));
            }
            studentChart.getData().clear();
            studentChart.getData().add(studentSeries);

        } catch (SQLException e) {
            // Quiet fail or log
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAddStudent() {
        openWindow("/fxml/AddStudentView.fxml", "Add Student");
        refreshAllData();
    }

    @FXML
    private void handleRefreshStudents() {
        loadStudents();
    }

    @FXML
    private void handleViewStudents() {
        mainTabPane.getSelectionModel().select(1); // Select Students Tab
    }

    @FXML
    private void handleAddSubject() {
        openWindow("/fxml/AddSubjectView.fxml", "Add Subject");
        refreshAllData();
    }

    @FXML
    private void handleViewSubjects() {
        mainTabPane.getSelectionModel().select(2); // Select Subjects Tab
    }

    @FXML
    private void handleEnterMarks() {
        openWindow("/fxml/EnterMarksView.fxml", "Enter Marks");
        refreshAllData();
    }

    @FXML
    private void handleViewMarks() {
        mainTabPane.getSelectionModel().select(3); // Select Marks Tab
    }

    @FXML
    private void handlePerformanceAnalysis() {
        mainTabPane.getSelectionModel().select(4); // Select Analysis Tab
    }

    @FXML
    private void handleGenerateReport() {
        openWindow("/fxml/ReportView.fxml", "Generate Report");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshAllData();
        } catch (IOException e) {
            showAlert("Error", "Failed to open " + title + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
