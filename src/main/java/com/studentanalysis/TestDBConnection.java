package com.studentanalysis;

import com.studentanalysis.util.DBConnection;
import com.studentanalysis.service.StudentService;
import com.studentanalysis.service.SubjectService;
import com.studentanalysis.service.MarksService;
import com.studentanalysis.service.AnalysisService;
import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;
import com.studentanalysis.model.Marks;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Simple test class to verify database connectivity and basic CRUD operations
 */
public class TestDBConnection {

    public static void main(String[] args) {
        System.out.println("=== Student Performance Analysis System - Database Test ===\n");

        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            if (DBConnection.getConnection() != null) {
                System.out.println("✅ Database connection successful!");
            }

            // Initialize services
            StudentService studentService = new StudentService();
            SubjectService subjectService = new SubjectService();
            MarksService marksService = new MarksService();
            AnalysisService analysisService = new AnalysisService();

            // Test adding a student
            System.out.println("\n2. Testing student operations...");
            Student testStudent = new Student("Test Student", "test@example.com", "1234567890", Date.valueOf("2024-01-01"));
            studentService.addStudent(testStudent);
            System.out.println("✅ Student added successfully: " + testStudent.getName());

            // Test adding a subject
            System.out.println("\n3. Testing subject operations...");
            Subject testSubject = new Subject("Test Subject", "TEST101", 3);
            subjectService.addSubject(testSubject);
            System.out.println("✅ Subject added successfully: " + testSubject.getSubjectName());

            // Test adding marks
            System.out.println("\n4. Testing marks operations...");
            Marks testMarks = new Marks(testStudent.getStudentId(), testSubject.getSubjectId(), 85.5, 100.0, Date.valueOf("2024-01-15"));
            marksService.addMarks(testMarks);
            System.out.println("✅ Marks added successfully: " + testMarks.getMarksObtained() + "/" + testMarks.getTotalMarks());

            // Test analysis
            System.out.println("\n5. Testing analysis operations...");
            double classAverage = analysisService.getClassAverage();
            System.out.println("✅ Class average calculated: " + String.format("%.2f", classAverage) + "%");

            // Test retrieving data
            System.out.println("\n6. Testing data retrieval...");
            var allStudents = studentService.getAllStudents();
            var allSubjects = subjectService.getAllSubjects();
            var allMarks = marksService.getAllMarks();

            System.out.println("✅ Retrieved " + allStudents.size() + " students");
            System.out.println("✅ Retrieved " + allSubjects.size() + " subjects");
            System.out.println("✅ Retrieved " + allMarks.size() + " marks entries");

            System.out.println("\n=== All Tests Passed Successfully! ===");
            System.out.println("The Student Performance Analysis System is ready to use.");

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close database connection
            DBConnection.closeConnection();
        }
    }
}
