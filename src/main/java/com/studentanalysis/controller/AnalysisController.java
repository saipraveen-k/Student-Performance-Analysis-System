package com.studentanalysis.controller;

import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;
import com.studentanalysis.service.AnalysisService;
import com.studentanalysis.service.StudentService;
import com.studentanalysis.service.SubjectService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnalysisController {

    @FXML private Label classAverageLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalSubjectsLabel;
    @FXML private Label passRateLabel;
    @FXML private TableView<StudentRanking> topStudentsTable;
    @FXML private TableView<SubjectPerformance> weakSubjectsTable;
    @FXML private BarChart<String, Number> subjectChart;
    @FXML private BarChart<String, Number> studentChart;

    private AnalysisService analysisService;
    private StudentService studentService;
    private SubjectService subjectService;

    public AnalysisController() {
        this.analysisService = new AnalysisService();
        this.studentService = new StudentService();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        setupTables();
        handleGenerateAnalysis();
    }

    private void setupTables() {
        // Top students table
        TableColumn<StudentRanking, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<StudentRanking, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudentRanking, Double> avgCol = new TableColumn<>("Average");
        avgCol.setCellValueFactory(new PropertyValueFactory<>("average"));

        topStudentsTable.getColumns().addAll(rankCol, nameCol, avgCol);

        // Weak subjects table
        TableColumn<SubjectPerformance, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        TableColumn<SubjectPerformance, Double> subjAvgCol = new TableColumn<>("Average");
        subjAvgCol.setCellValueFactory(new PropertyValueFactory<>("average"));

        weakSubjectsTable.getColumns().addAll(subjectCol, subjAvgCol);
    }

    @FXML
    private void handleGenerateAnalysis() {
        try {
            // Update statistics
            double classAverage = analysisService.getClassAverage();
            classAverageLabel.setText(String.format("%.2f%%", classAverage));

            List<Student> allStudents = studentService.getAllStudents();
            totalStudentsLabel.setText(String.valueOf(allStudents.size()));

            List<Subject> allSubjects = subjectService.getAllSubjects();
            totalSubjectsLabel.setText(String.valueOf(allSubjects.size()));

            Map<String, Integer> passFailStats = analysisService.getPassFailStatistics(40.0); // 40% pass threshold
            int totalMarks = passFailStats.get("total");
            int passed = passFailStats.get("passed");
            double passRate = totalMarks > 0 ? (double) passed / totalMarks * 100 : 0.0;
            passRateLabel.setText(String.format("%.2f%%", passRate));

            // Update top students table
            List<Student> topStudents = analysisService.getTopPerformingStudents(5);
            ObservableList<StudentRanking> rankings = FXCollections.observableArrayList();
            for (int i = 0; i < topStudents.size(); i++) {
                Student student = topStudents.get(i);
                double average = analysisService.marksDAO.getAverageMarksForStudent(student.getStudentId());
                rankings.add(new StudentRanking(i + 1, student.getName(), average));
            }
            topStudentsTable.setItems(rankings);

            // Update weak subjects table
            List<Subject> weakSubjects = analysisService.getWeakPerformingSubjects(5);
            ObservableList<SubjectPerformance> subjectPerformances = FXCollections.observableArrayList();
            for (Subject subject : weakSubjects) {
                double average = analysisService.marksDAO.getAverageMarksForSubject(subject.getSubjectId());
                subjectPerformances.add(new SubjectPerformance(subject.getSubjectName(), average));
            }
            weakSubjectsTable.setItems(subjectPerformances);

            // Update charts
            updateSubjectChart();
            updateStudentChart();

        } catch (SQLException e) {
            showError("Failed to generate analysis: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        handleGenerateAnalysis();
    }

    private void updateSubjectChart() throws SQLException {
        subjectChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Marks");

        Map<String, Double> subjectPerformance = analysisService.getSubjectWisePerformance();
        for (Map.Entry<String, Double> entry : subjectPerformance.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        subjectChart.getData().add(series);
    }

    private void updateStudentChart() throws SQLException {
        studentChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Marks");

        List<Map<String, Object>> rankings = analysisService.getStudentPerformanceRanking();
        for (Map<String, Object> ranking : rankings) {
            Student student = (Student) ranking.get("student");
            Double average = (Double) ranking.get("average");
            series.getData().add(new XYChart.Data<>(student.getName(), average));
        }

        studentChart.getData().add(series);
    }

    private void showError(String message) {
        // For now, just print to console. In a real app, show an alert.
        System.err.println("Error: " + message);
    }

    // Helper classes for table data
    public static class StudentRanking {
        private final int rank;
        private final String name;
        private final double average;

        public StudentRanking(int rank, String name, double average) {
            this.rank = rank;
            this.name = name;
            this.average = average;
        }

        public int getRank() { return rank; }
        public String getName() { return name; }
        public double getAverage() { return average; }
    }

    public static class SubjectPerformance {
        private final String subjectName;
        private final double average;

        public SubjectPerformance(String subjectName, double average) {
            this.subjectName = subjectName;
            this.average = average;
        }

        public String getSubjectName() { return subjectName; }
        public double getAverage() { return average; }
    }
}
