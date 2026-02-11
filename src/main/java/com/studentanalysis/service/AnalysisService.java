package com.studentanalysis.service;

import com.studentanalysis.dao.MarksDAO;
import com.studentanalysis.dao.StudentDAO;
import com.studentanalysis.dao.SubjectDAO;
import com.studentanalysis.model.Marks;
import com.studentanalysis.model.Student;
import com.studentanalysis.model.Subject;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AnalysisService {
    private MarksDAO marksDAO;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;

    public AnalysisService() {
        this.marksDAO = new MarksDAO();
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
    }

    // Get overall class average
    public double getClassAverage() throws SQLException {
        List<Marks> allMarks = marksDAO.getAllMarks();
        if (allMarks.isEmpty()) return 0.0;

        double totalPercentage = 0.0;
        for (Marks mark : allMarks) {
            totalPercentage += mark.getPercentage();
        }
        return totalPercentage / allMarks.size();
    }

    // Get top performing students
    public List<Student> getTopPerformingStudents(int limit) throws SQLException {
        List<Student> allStudents = studentDAO.getAllStudents();
        Map<Integer, Double> studentAverages = new HashMap<>();

        for (Student student : allStudents) {
            double average = marksDAO.getAverageMarksForStudent(student.getStudentId());
            studentAverages.put(student.getStudentId(), average);
        }

        return allStudents.stream()
                .sorted((s1, s2) -> Double.compare(studentAverages.get(s2.getStudentId()), studentAverages.get(s1.getStudentId())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get weak performing subjects
    public List<Subject> getWeakPerformingSubjects(int limit) throws SQLException {
        List<Subject> allSubjects = subjectDAO.getAllSubjects();
        Map<Integer, Double> subjectAverages = new HashMap<>();

        for (Subject subject : allSubjects) {
            double average = marksDAO.getAverageMarksForSubject(subject.getSubjectId());
            subjectAverages.put(subject.getSubjectId(), average);
        }

        return allSubjects.stream()
                .sorted(Comparator.comparingDouble(s -> subjectAverages.get(s.getSubjectId())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get pass/fail statistics
    public Map<String, Integer> getPassFailStatistics(double passThreshold) throws SQLException {
        List<Marks> allMarks = marksDAO.getAllMarks();
        Map<String, Integer> stats = new HashMap<>();
        stats.put("passed", 0);
        stats.put("failed", 0);
        stats.put("total", allMarks.size());

        for (Marks mark : allMarks) {
            if (mark.getPercentage() >= passThreshold) {
                stats.put("passed", stats.get("passed") + 1);
            } else {
                stats.put("failed", stats.get("failed") + 1);
            }
        }

        return stats;
    }

    // Get subject-wise performance
    public Map<String, Double> getSubjectWisePerformance() throws SQLException {
        List<Subject> subjects = subjectDAO.getAllSubjects();
        Map<String, Double> performance = new HashMap<>();

        for (Subject subject : subjects) {
            double average = marksDAO.getAverageMarksForSubject(subject.getSubjectId());
            performance.put(subject.getSubjectName(), average);
        }

        return performance;
    }

    // Get student performance ranking
    public List<Map<String, Object>> getStudentPerformanceRanking() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        List<Map<String, Object>> rankings = new ArrayList<>();

        for (Student student : students) {
            double average = marksDAO.getAverageMarksForStudent(student.getStudentId());
            Map<String, Object> ranking = new HashMap<>();
            ranking.put("student", student);
            ranking.put("average", average);
            rankings.add(ranking);
        }

        // Sort by average descending
        rankings.sort((r1, r2) -> Double.compare((Double) r2.get("average"), (Double) r1.get("average")));

        return rankings;
    }

    // Predict rank for a student (simple implementation based on current average)
    public int predictStudentRank(int studentId) throws SQLException {
        List<Map<String, Object>> rankings = getStudentPerformanceRanking();

        for (int i = 0; i < rankings.size(); i++) {
            Student student = (Student) rankings.get(i).get("student");
            if (student.getStudentId() == studentId) {
                return i + 1; // 1-based ranking
            }
        }

        return -1; // Student not found
    }

    // Get performance trends (simplified - just current averages)
    public Map<String, Double> getPerformanceTrends() throws SQLException {
        Map<String, Double> trends = new HashMap<>();
        trends.put("classAverage", getClassAverage());
        trends.put("highestScore", getHighestScore());
        trends.put("lowestScore", getLowestScore());

        return trends;
    }

    private double getHighestScore() throws SQLException {
        List<Marks> allMarks = marksDAO.getAllMarks();
        return allMarks.stream()
                .mapToDouble(Marks::getPercentage)
                .max()
                .orElse(0.0);
    }

    private double getLowestScore() throws SQLException {
        List<Marks> allMarks = marksDAO.getAllMarks();
        return allMarks.stream()
                .mapToDouble(Marks::getPercentage)
                .min()
                .orElse(0.0);
    }

    // Public method to get average marks for a student
    public double getAverageMarksForStudent(int studentId) throws SQLException {
        return marksDAO.getAverageMarksForStudent(studentId);
    }

    // Public method to get average marks for a subject
    public double getAverageMarksForSubject(int subjectId) throws SQLException {
        return marksDAO.getAverageMarksForSubject(subjectId);
    }
}
