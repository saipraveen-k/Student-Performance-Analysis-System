package com.studentanalysis.dao;

import com.studentanalysis.model.Subject;
import com.studentanalysis.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public void addSubject(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (subject_name, subject_code, credits) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, subject.getSubjectName());
            stmt.setString(2, subject.getSubjectCode());
            stmt.setInt(3, subject.getCredits());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        subject.setSubjectId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public Subject getSubjectById(int subjectId) throws SQLException {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        rs.getString("subject_code"),
                        rs.getInt("credits")
                    );
                }
            }
        }
        return null;
    }

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                subjects.add(new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getString("subject_code"),
                    rs.getInt("credits")
                ));
            }
        }
        return subjects;
    }

    public void updateSubject(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET subject_name = ?, subject_code = ?, credits = ? WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject.getSubjectName());
            stmt.setString(2, subject.getSubjectCode());
            stmt.setInt(3, subject.getCredits());
            stmt.setInt(4, subject.getSubjectId());

            stmt.executeUpdate();
        }
    }

    public void deleteSubject(int subjectId) throws SQLException {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            stmt.executeUpdate();
        }
    }
}
