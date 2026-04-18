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
    public List<StudentRecord> findAllByTimeline() throws SQLException {
        String sql = "SELECT id, content, mood, owner_username, created_at FROM mood_notes ORDER BY datetime(created_at) DESC, id DESC";
        List<StudentRecord> notes = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notes.add(mapRecord(rs));
            }
        }
        return notes;
    }

    public List<StudentRecord> findAllRandom() throws SQLException {
        String sql = "SELECT id, content, mood, owner_username, created_at FROM mood_notes ORDER BY RANDOM()";
        List<StudentRecord> notes = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notes.add(mapRecord(rs));
            }
        }
        return notes;
    }

    public void insert(StudentRecord record) throws SQLException {
        String sql = "INSERT INTO mood_notes(content, mood, owner_username) VALUES(?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getContent());
            ps.setString(2, record.getMood());
            ps.setString(3, record.getOwnerUsername());
            ps.executeUpdate();
        }
    }

    public int deleteByIdAndOwner(int id, String ownerUsername) throws SQLException {
        String sql = "DELETE FROM mood_notes WHERE id = ? AND owner_username = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, ownerUsername);
            return ps.executeUpdate();
        }
    }

    private StudentRecord mapRecord(ResultSet rs) throws SQLException {
        StudentRecord record = new StudentRecord();
        record.setId(rs.getInt("id"));
        record.setContent(rs.getString("content"));
        record.setMood(rs.getString("mood"));
        record.setOwnerUsername(rs.getString("owner_username"));
        record.setCreatedAt(rs.getString("created_at"));
        return record;
    }
}
