package com.studentanalysis.model;

import java.sql.Date;

public class Marks {
    private int markId;
    private int studentId;
    private int subjectId;
    private double marksObtained;
    private double totalMarks;
    private Date examDate;

    // Default constructor
    public Marks() {}

    // Constructor with parameters
    public Marks(int markId, int studentId, int subjectId, double marksObtained, double totalMarks, Date examDate) {
        this.markId = markId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.examDate = examDate;
    }

    // Constructor without ID (for new marks)
    public Marks(int studentId, int subjectId, double marksObtained, double totalMarks, Date examDate) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.examDate = examDate;
    }

    // Getters and Setters
    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    // Utility method to calculate percentage
    public double getPercentage() {
        if (totalMarks == 0) return 0.0;
        return (marksObtained / totalMarks) * 100.0;
    }

    @Override
    public String toString() {
        return "Marks{" +
                "markId=" + markId +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", marksObtained=" + marksObtained +
                ", totalMarks=" + totalMarks +
                ", percentage=" + String.format("%.2f", getPercentage()) + "%" +
                ", examDate=" + examDate +
                '}';
    }
}
