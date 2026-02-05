package com.studentanalysis.model;

public class Subject {
    private int subjectId;
    private String subjectName;
    private String subjectCode;
    private int credits;

    // Default constructor
    public Subject() {}

    // Constructor with parameters
    public Subject(int subjectId, String subjectName, String subjectCode, int credits) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
    }

    // Constructor without ID (for new subjects)
    public Subject(String subjectName, String subjectCode, int credits) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectName='" + subjectName + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", credits=" + credits +
                '}';
    }
}
