package com.studentanalysis.controller;

import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;
import com.studentanalysis.model.Marks;
import com.studentanalysis.service.StudentService;
import com.studentanalysis.service.SubjectService;
import com.studentanalysis.service.MarksService;
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
import java.util.Map;

public class MainController {

    @FXML private TabPane mainTabPane;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalSubjectsLabel;
    @FXML private Label totalMarksLabel;
    @FXML private TextArea recentActivityTextArea;
    @FXML private BarChart<String, Number> subjectChart;
    @FXML private BarChart<String, Number> studentChart;

    private StudentService studentService;
    private SubjectService subjectService;
    private MarksService marksService;
    private AnalysisService analysisService;

    public MainController() {
        this.studentService = new StudentService();
        this.subjectService = new SubjectService();
        this.marksService = new MarksService();
        this.analysisService = new AnalysisService();
    }

    @FXML
    public void initialize() {
        updateDashboardStats();
        recentActivityTextArea.setText("Application started successfully.\nDatabase connection established.");
        updateCharts();
    }

    private void updateDashboardStats() {
        try {
            totalStudentsLabel.setText(String.valueOf(studentService.getAllStudents().size()));
            totalSubjectsLabel.setText(String.valueOf(subjectService.getAllSubjects().size()));
            totalMarksLabel.setText(String.valueOf(marksService.getAllMarks().size()));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load dashboard statistics: " + e.getMessage());
        }
    }

    private void updateCharts() {
        try {
            // Update subject performance chart
            XYChart.Series<String, Number> subjectSeries = new XYChart.Series<>();
            subjectSeries.setName("Average Marks");
            
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject subject : subjects) {
                double avgMarks = analysisService.getAverageMarksForSubject(subject.getSubjectId());
                subjectSeries.getData().add(new XYChart.Data<>(subject.getSubjectName(), avgMarks));
            }
            subjectChart.getData().clear();
            subjectChart.getData().add(subjectSeries);
            
            // Update student performance chart
            XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
            studentSeries.setName("Total Marks");
            
            List<Student> students = studentService.getAllStudents();
            for (Student student : students) {
                double totalMarks = marksService.getTotalMarksForStudent(student.getStudentId());
                studentSeries.getData().add(new XYChart.Data<>(student.getName(), totalMarks));
            }
            studentChart.getData().clear();
            studentChart.getData().add(studentSeries);
            
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update charts: " + e.getMessage());
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAddStudent() {
        openWindow("/fxml/AddStudentView.fxml", "Add Student");
        updateDashboardStats(); // Refresh stats after adding student
    }

    @FXML
    private void handleViewStudents() {
        openWindow("/fxml/StudentListView.fxml", "Student List");
    }

    @FXML
    private void handleAddSubject() {
        openWindow("/fxml/AddSubjectView.fxml", "Add Subject");
    }

    @FXML
    private void handleViewSubjects() {
        openWindow("/fxml/SubjectListView.fxml", "Subject List");
    }

    @FXML
    private void handleEnterMarks() {
        openWindow("/fxml/EnterMarksView.fxml", "Enter Marks");
        updateDashboardStats(); // Refresh stats after entering marks
    }

    @FXML
    private void handleViewMarks() {
        openWindow("/fxml/MarksListView.fxml", "Marks List");
    }

    @FXML
    private void handlePerformanceAnalysis() {
        openWindow("/fxml/AnalysisView.fxml", "Performance Analysis");
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

            // Refresh dashboard after closing modal windows
            updateDashboardStats();
        } catch (IOException e) {
            showAlert("Error", "Failed to open " + title + " window: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void logActivity(String activity) {
        recentActivityTextArea.appendText("\n" + activity);
    }
}
