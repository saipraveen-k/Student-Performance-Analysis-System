package com.studentanalysis.controller;

import com.studentanalysis.model.Student;
import com.studentanalysis.service.StudentService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddStudentController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker enrollmentDatePicker;
    @FXML private Label messageLabel;

    private StudentService studentService;

    public AddStudentController() {
        this.studentService = new StudentService();
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs
            if (nameField.getText().trim().isEmpty()) {
                showError("Name is required");
                return;
            }
            if (emailField.getText().trim().isEmpty()) {
                showError("Email is required");
                return;
            }
            if (enrollmentDatePicker.getValue() == null) {
                showError("Enrollment date is required");
                return;
            }

            // Create student object
            Student student = new Student();
            student.setName(nameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setPhone(phoneField.getText().trim());
            student.setEnrollmentDate(Date.valueOf(enrollmentDatePicker.getValue()));

            // Save to database
            studentService.addStudent(student);

            showSuccess("Student added successfully!");
            clearForm();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
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
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        enrollmentDatePicker.setValue(null);
    }
}
