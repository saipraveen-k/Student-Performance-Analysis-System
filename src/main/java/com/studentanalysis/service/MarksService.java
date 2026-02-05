package com.studentanalysis.service;

import com.studentanalysis.dao.MarksDAO;
import com.studentanalysis.model.Marks;

import java.sql.SQLException;
import java.util.List;

public class MarksService {
    private MarksDAO marksDAO;

    public MarksService() {
        this.marksDAO = new MarksDAO();
    }

    public void addMarks(Marks marks) throws SQLException {
        // Business logic validation
        if (marks.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (marks.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        if (marks.getMarksObtained() < 0) {
            throw new IllegalArgumentException("Marks obtained cannot be negative");
        }
        if (marks.getTotalMarks() <= 0) {
            throw new IllegalArgumentException("Total marks must be positive");
        }
        if (marks.getMarksObtained() > marks.getTotalMarks()) {
            throw new IllegalArgumentException("Marks obtained cannot exceed total marks");
        }
        if (marks.getExamDate() == null) {
            throw new IllegalArgumentException("Exam date cannot be null");
        }

        marksDAO.addMarks(marks);
    }

    public Marks getMarksById(int markId) throws SQLException {
        if (markId <= 0) {
            throw new IllegalArgumentException("Invalid mark ID");
        }
        return marksDAO.getMarksById(markId);
    }

    public List<Marks> getMarksByStudentId(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        return marksDAO.getMarksByStudentId(studentId);
    }

    public List<Marks> getMarksBySubjectId(int subjectId) throws SQLException {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        return marksDAO.getMarksBySubjectId(subjectId);
    }

    public List<Marks> getAllMarks() throws SQLException {
        return marksDAO.getAllMarks();
    }

    public void updateMarks(Marks marks) throws SQLException {
        // Business logic validation
        if (marks.getMarkId() <= 0) {
            throw new IllegalArgumentException("Invalid mark ID");
        }
        if (marks.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (marks.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        if (marks.getMarksObtained() < 0) {
            throw new IllegalArgumentException("Marks obtained cannot be negative");
        }
        if (marks.getTotalMarks() <= 0) {
            throw new IllegalArgumentException("Total marks must be positive");
        }
        if (marks.getMarksObtained() > marks.getTotalMarks()) {
            throw new IllegalArgumentException("Marks obtained cannot exceed total marks");
        }
        if (marks.getExamDate() == null) {
            throw new IllegalArgumentException("Exam date cannot be null");
        }

        marksDAO.updateMarks(marks);
    }

    public void deleteMarks(int markId) throws SQLException {
        if (markId <= 0) {
            throw new IllegalArgumentException("Invalid mark ID");
        }
        marksDAO.deleteMarks(markId);
    }

    // Analysis methods
    public double getAverageMarksForSubject(int subjectId) throws SQLException {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        return marksDAO.getAverageMarksForSubject(subjectId);
    }

    public double getAverageMarksForStudent(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        return marksDAO.getAverageMarksForStudent(studentId);
    }

    // Get marks with percentage
    public List<Marks> getMarksWithPercentage(int studentId) throws SQLException {
        List<Marks> marks = getMarksByStudentId(studentId);
        // The percentage is calculated in the Marks model getPercentage() method
        return marks;
    }
}
