package com.studentanalysis.controller;

import com.studentanalysis.dao.SubjectDAO;
import com.studentanalysis.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SubjectListController {

    @FXML private TextField searchField;
    @FXML private TableView<Subject> subjectTable;
    @FXML private TableColumn<Subject, Integer> colId;
    @FXML private TableColumn<Subject, String> colName;
    @FXML private TableColumn<Subject, String> colCode;
    @FXML private TableColumn<Subject, Integer> colCredits;
    @FXML private TableColumn<Subject, Void> colActions;
    @FXML private Label messageLabel;

    private SubjectDAO subjectDAO;
    private ObservableList<Subject> subjectList;

    public SubjectListController() {
        this.subjectDAO = new SubjectDAO();
        this.subjectList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialize columns
        // Note: FXML already sets cellValueFactory for basic columns, but we can re-confirm or setup actions here
        colId = (TableColumn<Subject, Integer>) subjectTable.getColumns().get(0);
        colName = (TableColumn<Subject, String>) subjectTable.getColumns().get(1);
        colCode = (TableColumn<Subject, String>) subjectTable.getColumns().get(2);
        colCredits = (TableColumn<Subject, Integer>) subjectTable.getColumns().get(3);
        colActions = (TableColumn<Subject, Void>) subjectTable.getColumns().get(4);

        colId.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

        setupActionColumn();
        loadSubjects();

        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSubjects(newValue);
        });
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Subject subject = getTableView().getItems().get(getIndex());
                    handleDeleteSubject(subject);
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

    private void loadSubjects() {
        try {
            List<Subject> subjects = subjectDAO.getAllSubjects();
            subjectList.setAll(subjects);
            subjectTable.setItems(subjectList);
        } catch (SQLException e) {
            messageLabel.setText("Error loading subjects: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterSubjects(String query) {
        if (query == null || query.isEmpty()) {
            subjectTable.setItems(subjectList);
            return;
        }

        ObservableList<Subject> filteredList = FXCollections.observableArrayList();
        for (Subject subject : subjectList) {
            if (subject.getSubjectName().toLowerCase().contains(query.toLowerCase()) ||
                subject.getSubjectCode().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(subject);
            }
        }
        subjectTable.setItems(filteredList);
    }

    @FXML
    private void handleAddSubject() {
        // Logic to open AddSubjectView
        // This would typically involve opening a new stage or dialog
        // For simplicity, we can assume MainController handles navigation or we open a dialog here
        // But since we are inside a tab, we might need to communicate with MainController
        // Or simply open a new Stage
        try {
             javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/AddSubjectView.fxml"));
             javafx.scene.Parent root = loader.load();
             javafx.scene.Scene scene = new javafx.scene.Scene(root);
             javafx.stage.Stage stage = new javafx.stage.Stage();
             stage.setScene(scene);
             stage.setTitle("Add Subject");
             stage.showAndWait();
             // Refresh list after add
             loadSubjects();
        } catch (java.io.IOException e) {
            messageLabel.setText("Error opening Add Subject view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadSubjects();
        messageLabel.setText("Refreshed subject list.");
    }

    private void handleDeleteSubject(Subject subject) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Subject");
        alert.setHeaderText("Are you sure you want to delete " + subject.getSubjectName() + "?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                subjectDAO.deleteSubject(subject.getSubjectId()); // Assuming deleteSubject method exists in DAO
                loadSubjects();
                messageLabel.setText("Subject deleted successfully.");
            } catch (SQLException e) {
                messageLabel.setText("Error deleting subject: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
