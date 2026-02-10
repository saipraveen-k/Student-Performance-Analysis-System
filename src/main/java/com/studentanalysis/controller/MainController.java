package com.studentanalysis.controller;

import com.studentanalysis.service.StudentService;
import com.studentanalysis.service.SubjectService;
import com.studentanalysis.service.MarksService;
import com.studentanalysis.service.AnalysisService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    @FXML private TabPane mainTabPane;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalSubjectsLabel;
    @FXML private Label totalMarksLabel;
    @FXML private TextArea recentActivityTextArea;

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
