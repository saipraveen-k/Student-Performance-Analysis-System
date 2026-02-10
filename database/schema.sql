-- Student Performance Analysis System Database Schema
-- PostgreSQL Database: student_performance_db

-- Note: Create the database manually in PostgreSQL before running this script.
-- psql -U postgres -c "CREATE DATABASE student_performance_db;"

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    enrollment_date DATE
);

-- Subjects table
CREATE TABLE IF NOT EXISTS subjects (
    subject_id SERIAL PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(10) UNIQUE NOT NULL,
    credits INT
);

-- Marks table
CREATE TABLE IF NOT EXISTS marks (
    mark_id SERIAL PRIMARY KEY,
    student_id INT,
    subject_id INT,
    marks_obtained DECIMAL(5,2),
    total_marks DECIMAL(5,2),
    exam_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

-- Insert sample data for testing
INSERT INTO students (name, email, phone, enrollment_date) VALUES
('John Doe', 'john.doe@example.com', '1234567890', '2023-09-01'),
('Jane Smith', 'jane.smith@example.com', '0987654321', '2023-09-01'),
('Bob Johnson', 'bob.johnson@example.com', '1122334455', '2023-09-01');

INSERT INTO subjects (subject_name, subject_code, credits) VALUES
('Mathematics', 'MATH101', 4),
('Physics', 'PHYS101', 3),
('Chemistry', 'CHEM101', 3),
('Computer Science', 'CS101', 4);

INSERT INTO marks (student_id, subject_id, marks_obtained, total_marks, exam_date) VALUES
(1, 1, 85.5, 100, '2024-01-15'),
(1, 2, 78.0, 100, '2024-01-16'),
(1, 3, 92.5, 100, '2024-01-17'),
(1, 4, 88.0, 100, '2024-01-18'),
(2, 1, 76.5, 100, '2024-01-15'),
(2, 2, 89.0, 100, '2024-01-16'),
(2, 3, 81.5, 100, '2024-01-17'),
(2, 4, 94.0, 100, '2024-01-18'),
(3, 1, 65.0, 100, '2024-01-15'),
(3, 2, 72.5, 100, '2024-01-16'),
(3, 3, 68.0, 100, '2024-01-17'),
(3, 4, 75.5, 100, '2024-01-18');
