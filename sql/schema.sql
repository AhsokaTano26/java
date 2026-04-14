CREATE DATABASE IF NOT EXISTS java_web_lab DEFAULT CHARACTER SET utf8mb4;
USE java_web_lab;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    major VARCHAR(100) NOT NULL
);

-- 默认管理员密码是 123456（已使用 PBKDF2 加盐哈希存储）
INSERT INTO users(username, password) VALUES
('admin', '120000$jyxNgKGyPE1eb3CBkqO0xQ==$myTuCZGvDAFpSSipH7vGFSjoT/30MxUQNvVybN3A7I4=')
ON DUPLICATE KEY UPDATE password = VALUES(password);

INSERT INTO student_records(name, age, major) VALUES
('张三', 20, '计算机科学'),
('李四', 21, '软件工程'),
('王五', 22, '信息管理');
