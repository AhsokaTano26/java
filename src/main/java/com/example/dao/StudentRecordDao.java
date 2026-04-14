package com.example.dao;

import com.example.model.StudentRecord;
import com.example.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRecordDao {
    public List<StudentRecord> findAll() throws SQLException {
        String sql = "SELECT id, name, age, major FROM student_records ORDER BY id";
        List<StudentRecord> records = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                StudentRecord record = mapRecord(rs);
                records.add(record);
            }
        }
        return records;
    }

    public StudentRecord findById(int id) throws SQLException {
        String sql = "SELECT id, name, age, major FROM student_records WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRecord(rs);
                }
                return null;
            }
        }
    }

    public void insert(StudentRecord record) throws SQLException {
        String sql = "INSERT INTO student_records(name, age, major) VALUES(?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getName());
            ps.setInt(2, record.getAge());
            ps.setString(3, record.getMajor());
            ps.executeUpdate();
        }
    }

    public void update(StudentRecord record) throws SQLException {
        String sql = "UPDATE student_records SET name = ?, age = ?, major = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getName());
            ps.setInt(2, record.getAge());
            ps.setString(3, record.getMajor());
            ps.setInt(4, record.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM student_records WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private StudentRecord mapRecord(ResultSet rs) throws SQLException {
        StudentRecord record = new StudentRecord();
        record.setId(rs.getInt("id"));
        record.setName(rs.getString("name"));
        record.setAge(rs.getInt("age"));
        record.setMajor(rs.getString("major"));
        return record;
    }
}
