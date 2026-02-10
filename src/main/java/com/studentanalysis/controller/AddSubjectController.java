package com.studentanalysis.controller;

import com.studentanalysis.model.Subject;
import com.studentanalysis.service.SubjectService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddSubjectController {

    @FXML private TextField subjectNameField;
    @FXML private TextField subjectCodeField;
    @FXML private TextField creditsField;
    @FXML private Label messageLabel;

    private SubjectService subjectService;

    public AddSubjectController() {
        this.subjectService = new SubjectService();
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs
            if (subjectNameField.getText().trim().isEmpty()) {
                showError("Subject name is required");
                return;
            }
            if (subjectCodeField.getText().trim().isEmpty()) {
                showError("Subject code is required");
                return;
            }
            if (creditsField.getText().trim().isEmpty()) {
                showError("Credits is required");
                return;
            }

            int credits;
            try {
                credits = Integer.parseInt(creditsField.getText().trim());
            } catch (NumberFormatException e) {
                showError("Credits must be a valid number");
                return;
            }

            // Create subject object
            Subject subject = new Subject();
            subject.setSubjectName(subjectNameField.getText().trim());
            subject.setSubjectCode(subjectCodeField.getText().trim());
            subject.setCredits(credits);

            // Save to database
            subjectService.addSubject(subject);

            showSuccess("Subject added successfully!");
            clearForm();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) subjectNameField.getScene().getWindow();
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
        subjectNameField.clear();
        subjectCodeField.clear();
        creditsField.clear();
    }
}
