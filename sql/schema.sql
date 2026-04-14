CREATE DATABASE IF NOT EXISTS java_web_lab DEFAULT CHARACTER SET utf8mb4;
USE java_web_lab;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS student_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    major VARCHAR(100) NOT NULL
);

INSERT INTO users(username, password) VALUES
('admin', '123456')
ON DUPLICATE KEY UPDATE password = VALUES(password);

INSERT INTO student_records(name, age, major) VALUES
('张三', 20, '计算机科学'),
('李四', 21, '软件工程'),
('王五', 22, '信息管理');
