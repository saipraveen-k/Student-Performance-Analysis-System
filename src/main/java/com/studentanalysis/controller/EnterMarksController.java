package com.studentanalysis.controller;

import com.studentanalysis.model.Marks;
import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;
import com.studentanalysis.service.MarksService;
import com.studentanalysis.service.StudentService;
import com.studentanalysis.service.SubjectService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class EnterMarksController {

    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private TextField marksObtainedField;
    @FXML private TextField totalMarksField;
    @FXML private DatePicker examDatePicker;
    @FXML private Label messageLabel;

    private MarksService marksService;
    private StudentService studentService;
    private SubjectService subjectService;

    public EnterMarksController() {
        this.marksService = new MarksService();
        this.studentService = new StudentService();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        loadStudents();
        loadSubjects();
    }

    private void loadStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            ObservableList<Student> studentList = FXCollections.observableArrayList(students);
            studentComboBox.setItems(studentList);
        } catch (SQLException e) {
            showError("Failed to load students: " + e.getMessage());
        }
    }

    private void loadSubjects() {
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            ObservableList<Subject> subjectList = FXCollections.observableArrayList(subjects);
            subjectComboBox.setItems(subjectList);
        } catch (SQLException e) {
            showError("Failed to load subjects: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs
            if (studentComboBox.getValue() == null) {
                showError("Please select a student");
                return;
            }
            if (subjectComboBox.getValue() == null) {
                showError("Please select a subject");
                return;
            }
            if (marksObtainedField.getText().trim().isEmpty()) {
                showError("Marks obtained is required");
                return;
            }
            if (totalMarksField.getText().trim().isEmpty()) {
                showError("Total marks is required");
                return;
            }
            if (examDatePicker.getValue() == null) {
                showError("Exam date is required");
                return;
            }

            double marksObtained, totalMarks;
            try {
                marksObtained = Double.parseDouble(marksObtainedField.getText().trim());
                totalMarks = Double.parseDouble(totalMarksField.getText().trim());
            } catch (NumberFormatException e) {
                showError("Marks must be valid numbers");
                return;
            }

            if (marksObtained > totalMarks) {
                showError("Marks obtained cannot exceed total marks");
                return;
            }

            // Create marks object
            Marks marks = new Marks();
            marks.setStudentId(studentComboBox.getValue().getStudentId());
            marks.setSubjectId(subjectComboBox.getValue().getSubjectId());
            marks.setMarksObtained(marksObtained);
            marks.setTotalMarks(totalMarks);
            marks.setExamDate(Date.valueOf(examDatePicker.getValue()));

            // Save to database
            marksService.addMarks(marks);

            showSuccess("Marks entered successfully!");
            clearForm();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) studentComboBox.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("success-message");
        messageLabel.getStyleClass().add("error-message");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("error-message");
        messageLabel.getStyleClass().add("success-message");
    }

    private void clearForm() {
        studentComboBox.setValue(null);
        subjectComboBox.setValue(null);
        marksObtainedField.clear();
        totalMarksField.clear();
        examDatePicker.setValue(null);
    }
}
