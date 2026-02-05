package com.studentanalysis.service;

import com.studentanalysis.dao.StudentDAO;
import com.studentanalysis.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class StudentService {
    private StudentDAO studentDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public void addStudent(Student student) throws SQLException {
        // Business logic validation
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (student.getEnrollmentDate() == null) {
            throw new IllegalArgumentException("Enrollment date cannot be null");
        }

        // Check if email already exists
        if (studentDAO.getStudentByEmail(student.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        studentDAO.addStudent(student);
    }

    public Student getStudentById(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        return studentDAO.getStudentById(studentId);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void updateStudent(Student student) throws SQLException {
        // Business logic validation
        if (student.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (student.getEnrollmentDate() == null) {
            throw new IllegalArgumentException("Enrollment date cannot be null");
        }

        // Check if email already exists for another student
        Student existingStudent = studentDAO.getStudentByEmail(student.getEmail());
        if (existingStudent != null && existingStudent.getStudentId() != student.getStudentId()) {
            throw new IllegalArgumentException("Email already exists for another student");
        }

        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        studentDAO.deleteStudent(studentId);
    }

    public Student getStudentByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return studentDAO.getStudentByEmail(email);
    }
}
