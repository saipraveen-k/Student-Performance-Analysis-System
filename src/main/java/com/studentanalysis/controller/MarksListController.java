package com.studentanalysis.controller;

import com.studentanalysis.dao.MarksDAO;
import com.studentanalysis.model.Marks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;

public class MarksListController {

    @FXML private TextField studentIdFilter;
    @FXML private TableView<Marks> marksTable;
    @FXML private TableColumn<Marks, Integer> colMarkId;
    @FXML private TableColumn<Marks, Integer> colStudentId;
    @FXML private TableColumn<Marks, Integer> colSubjectId;
    @FXML private TableColumn<Marks, Double> colMarksObtained;
    @FXML private TableColumn<Marks, Double> colTotalMarks;
    @FXML private TableColumn<Marks, String> colExamDate;
    @FXML private Label messageLabel;

    private MarksDAO marksDAO;
    private ObservableList<Marks> marksList;

    public MarksListController() {
        this.marksDAO = new MarksDAO();
        this.marksList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        colMarkId = (TableColumn<Marks, Integer>) marksTable.getColumns().get(0);
        colStudentId = (TableColumn<Marks, Integer>) marksTable.getColumns().get(1);
        colSubjectId = (TableColumn<Marks, Integer>) marksTable.getColumns().get(2);
        colMarksObtained = (TableColumn<Marks, Double>) marksTable.getColumns().get(3);
        colTotalMarks = (TableColumn<Marks, Double>) marksTable.getColumns().get(4);
        colExamDate = (TableColumn<Marks, String>) marksTable.getColumns().get(5);

        colMarkId.setCellValueFactory(new PropertyValueFactory<>("markId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colSubjectId.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        colMarksObtained.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        colTotalMarks.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        colExamDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));

        loadMarks();
    }

    private void loadMarks() {
        try {
            List<Marks> marks = marksDAO.getAllMarks();
            marksList.setAll(marks);
            marksTable.setItems(marksList);
        } catch (SQLException e) {
            messageLabel.setText("Error loading marks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEnterMarks() {
         try {
             javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/EnterMarksView.fxml"));
             javafx.scene.Parent root = loader.load();
             javafx.scene.Scene scene = new javafx.scene.Scene(root);
             javafx.stage.Stage stage = new javafx.stage.Stage();
             stage.setScene(scene);
             stage.setTitle("Enter Marks");
             stage.showAndWait();
             loadMarks();
        } catch (java.io.IOException e) {
            messageLabel.setText("Error opening Enter Marks view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadMarks();
        messageLabel.setText("Refreshed marks list.");
    }

    @FXML
    private void handleFilter() {
        String studentIdStr = studentIdFilter.getText();
        if (studentIdStr == null || studentIdStr.trim().isEmpty()) {
            loadMarks();
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr.trim());
            List<Marks> filteredMarks = marksDAO.getMarksByStudentId(studentId);
            marksList.setAll(filteredMarks);
            marksTable.setItems(marksList);
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Student ID format.");
        } catch (SQLException e) {
            messageLabel.setText("Error filtering marks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
