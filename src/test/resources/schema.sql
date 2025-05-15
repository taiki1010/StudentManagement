CREATE TABLE IF NOT EXISTS students
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  kana_name VARCHAR(50) NOT NULL,
  nickname VARCHAR(50),
  email VARCHAR(50),
  area VARCHAR(50),
  age INT,
  gender VARCHAR(10),
  remark TEXT,
  is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  course_name VARCHAR(50) NOT NULL,
  course_start_at TIMESTAMP,
  course_end_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS application_status
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL,
  status VARCHAR(5) NOT NULL
);
