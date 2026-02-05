package com.studentanalysis.dao;

import com.studentanalysis.model.Marks;
import com.studentanalysis.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarksDAO {

    public void addMarks(Marks marks) throws SQLException {
        String sql = "INSERT INTO marks (student_id, subject_id, marks_obtained, total_marks, exam_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, marks.getStudentId());
            stmt.setInt(2, marks.getSubjectId());
            stmt.setDouble(3, marks.getMarksObtained());
            stmt.setDouble(4, marks.getTotalMarks());
            stmt.setDate(5, marks.getExamDate());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        marks.setMarkId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public Marks getMarksById(int markId) throws SQLException {
        String sql = "SELECT * FROM marks WHERE mark_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, markId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Marks(
                        rs.getInt("mark_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("marks_obtained"),
                        rs.getDouble("total_marks"),
                        rs.getDate("exam_date")
                    );
                }
            }
        }
        return null;
    }

    public List<Marks> getMarksByStudentId(int studentId) throws SQLException {
        List<Marks> marksList = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    marksList.add(new Marks(
                        rs.getInt("mark_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("marks_obtained"),
                        rs.getDouble("total_marks"),
                        rs.getDate("exam_date")
                    ));
                }
            }
        }
        return marksList;
    }

    public List<Marks> getMarksBySubjectId(int subjectId) throws SQLException {
        List<Marks> marksList = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    marksList.add(new Marks(
                        rs.getInt("mark_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("marks_obtained"),
                        rs.getDouble("total_marks"),
                        rs.getDate("exam_date")
                    ));
                }
            }
        }
        return marksList;
    }

    public List<Marks> getAllMarks() throws SQLException {
        List<Marks> marksList = new ArrayList<>();
        String sql = "SELECT * FROM marks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                marksList.add(new Marks(
                    rs.getInt("mark_id"),
                    rs.getInt("student_id"),
                    rs.getInt("subject_id"),
                    rs.getDouble("marks_obtained"),
                    rs.getDouble("total_marks"),
                    rs.getDate("exam_date")
                ));
            }
        }
        return marksList;
    }

    public void updateMarks(Marks marks) throws SQLException {
        String sql = "UPDATE marks SET student_id = ?, subject_id = ?, marks_obtained = ?, total_marks = ?, exam_date = ? WHERE mark_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, marks.getStudentId());
            stmt.setInt(2, marks.getSubjectId());
            stmt.setDouble(3, marks.getMarksObtained());
            stmt.setDouble(4, marks.getTotalMarks());
            stmt.setDate(5, marks.getExamDate());
            stmt.setInt(6, marks.getMarkId());

            stmt.executeUpdate();
        }
    }

    public void deleteMarks(int markId) throws SQLException {
        String sql = "DELETE FROM marks WHERE mark_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, markId);
            stmt.executeUpdate();
        }
    }

    // Get average marks for a subject
    public double getAverageMarksForSubject(int subjectId) throws SQLException {
        String sql = "SELECT AVG(marks_obtained) as average FROM marks WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                }
            }
        }
        return 0.0;
    }

    // Get average marks for a student
    public double getAverageMarksForStudent(int studentId) throws SQLException {
        String sql = "SELECT AVG(marks_obtained) as average FROM marks WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                }
            }
        }
        return 0.0;
    }
}
