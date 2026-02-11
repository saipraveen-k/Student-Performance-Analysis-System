package com.studentanalysis.controller;

import com.studentanalysis.model.Student;
import com.studentanalysis.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudentListController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> enrollmentDateColumn;
    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    private StudentService studentService;
    private ObservableList<Student> studentList;
    private FilteredList<Student> filteredList;

    public StudentListController() {
        this.studentService = new StudentService();
    }

    @FXML
    public void initialize() {
        setupTable();
        loadStudents();
        setupSearch();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        enrollmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));

        // Add actions column
        TableColumn<Student, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<Student, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setSpacing(5);
                editButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleEditStudent(student);
                });
                deleteButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleDeleteStudent(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        studentTable.getColumns().add(actionsColumn);
    }

    private void loadStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            studentList = FXCollections.observableArrayList(students);
            filteredList = new FilteredList<>(studentList, p -> true);
            studentTable.setItems(filteredList);
            messageLabel.setText("Loaded " + students.size() + " students");
        } catch (SQLException e) {
            showError("Failed to load students: " + e.getMessage());
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return student.getName().toLowerCase().contains(lowerCaseFilter) ||
                       student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    private void handleAddStudent() {
        openWindow("/fxml/AddStudentView.fxml", "Add Student");
        loadStudents(); // Refresh after adding
    }

    private void handleEditStudent(Student student) {
        // For now, just show info. In a full implementation, open edit dialog
        showInfo("Edit Student", "Editing student: " + student.getName());
    }

    private void handleDeleteStudent(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Are you sure you want to delete this student?");
        alert.setContentText("Student: " + student.getName());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    studentService.deleteStudent(student.getStudentId());
                    loadStudents(); // Refresh after deleting
                    showInfo("Success", "Student deleted successfully");
                } catch (SQLException e) {
                    showError("Failed to delete student: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleRefresh() {
        loadStudents();
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
        } catch (IOException e) {
            showError("Failed to open " + title + " window: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
