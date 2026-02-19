package com.studentanalysis.controller;

import com.studentanalysis.dao.StudentDAO;
import com.studentanalysis.model.Student;
import com.studentanalysis.service.AnalysisService;
import com.studentanalysis.model.Marks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReportController {

    @FXML private ComboBox<Student> studentComboBox;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Label emailLabel;
    @FXML private Label totalSubjectsLabel;
    @FXML private Label averageMarksLabel;
    @FXML private Label gradeLabel;
    @FXML private Label rankLabel;
    @FXML private BarChart<String, Number> performanceChart;
    @FXML private Label messageLabel;

    private StudentDAO studentDAO;
    private AnalysisService analysisService;

    public ReportController() {
        this.studentDAO = new StudentDAO();
        this.analysisService = new AnalysisService();
    }

    @FXML
    public void initialize() {
        loadStudents();
        
        // Setup ComboBox to display student names
        studentComboBox.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? "" : student.getName() + " (ID: " + student.getStudentId() + ")";
            }

            @Override
            public Student fromString(String string) {
                return null; // Not needed for this use case
            }
        });
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentComboBox.setItems(FXCollections.observableArrayList(students));
        } catch (SQLException e) {
            messageLabel.setText("Error loading students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateReport() {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            messageLabel.setText("Please select a student first.");
            return;
        }

        generateReport(selectedStudent);
    }

    private void generateReport(Student student) {
        try {
            // Clear previous data
            performanceChart.getData().clear();

            // Populate Student Details
            nameLabel.setText(student.getName());
            idLabel.setText(String.valueOf(student.getStudentId()));
            emailLabel.setText(student.getEmail());

            // Get Analysis Data
            double average = analysisService.getAverageMarksForStudent(student.getStudentId());
            averageMarksLabel.setText(String.format("%.2f", average));

            // Populate Chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Marks Obtained");
            
            // We need a method in AnalysisService or MarksDAO to get marks with subject names
            // For now, let's assume we can get a map or list of objects
            Map<String, Double> subjectPerformance = analysisService.getStudentPerformanceMap(student.getStudentId());
            
            int totalSubjects = subjectPerformance.size();
            totalSubjectsLabel.setText(String.valueOf(totalSubjects));

            for (Map.Entry<String, Double> entry : subjectPerformance.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            performanceChart.getData().add(series);

            // Grade Calculation (Simple logic for now)
            if (average >= 90) gradeLabel.setText("A+");
            else if (average >= 80) gradeLabel.setText("A");
            else if (average >= 70) gradeLabel.setText("B");
            else if (average >= 60) gradeLabel.setText("C");
            else if (average >= 50) gradeLabel.setText("D");
            else gradeLabel.setText("F");

            // Rank Prediction (Mock for now or implement in service)
            rankLabel.setText("Top " + (average > 80 ? "10%" : "50%")); // Placeholder logic

            messageLabel.setText("Report generated successfully.");

        } catch (SQLException e) {
            messageLabel.setText("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
