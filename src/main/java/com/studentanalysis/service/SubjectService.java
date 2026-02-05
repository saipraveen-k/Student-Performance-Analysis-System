package com.studentanalysis.service;

import com.studentanalysis.dao.SubjectDAO;
import com.studentanalysis.model.Subject;

import java.sql.SQLException;
import java.util.List;

public class SubjectService {
    private SubjectDAO subjectDAO;

    public SubjectService() {
        this.subjectDAO = new SubjectDAO();
    }

    public void addSubject(Subject subject) throws SQLException {
        // Business logic validation
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        if (subject.getSubjectCode() == null || subject.getSubjectCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be empty");
        }
        if (subject.getCredits() <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }
        // Additional validation can be added here

        subjectDAO.addSubject(subject);
    }

    public Subject getSubjectById(int subjectId) throws SQLException {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        return subjectDAO.getSubjectById(subjectId);
    }

    public List<Subject> getAllSubjects() throws SQLException {
        return subjectDAO.getAllSubjects();
    }

    public void updateSubject(Subject subject) throws SQLException {
        // Business logic validation
        if (subject.getSubjectId() <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        if (subject.getSubjectCode() == null || subject.getSubjectCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be empty");
        }
        if (subject.getCredits() <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }

        subjectDAO.updateSubject(subject);
    }

    public void deleteSubject(int subjectId) throws SQLException {
        if (subjectId <= 0) {
            throw new IllegalArgumentException("Invalid subject ID");
        }
        subjectDAO.deleteSubject(subjectId);
    }
}
