package com.example.dao;

import com.example.util.DbUtil;
import com.example.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    // SQLite: SQLITE_CONSTRAINT
    private static final int SQLITE_CONSTRAINT = 19;
    private static final String UNIQUE_CONSTRAINT_MESSAGE = "UNIQUE constraint failed";
    private static final String LOGIN_SQL = "SELECT password FROM users WHERE username = ?";
    private static final String REGISTER_SQL = "INSERT INTO users(username, password) VALUES(?, ?)";

    public boolean authenticate(String username, String password) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(LOGIN_SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
                String storedPassword = rs.getString("password");
                return PasswordUtil.verifyPassword(password, storedPassword);
            }
        }
    }

    public boolean register(String username, String rawPassword) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(REGISTER_SQL)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (isUniqueConstraintViolation(e)) {
                return false;
            }
            throw e;
        }
    }

    private boolean isUniqueConstraintViolation(SQLException e) {
        return e.getErrorCode() == SQLITE_CONSTRAINT
                || (e.getMessage() != null && e.getMessage().contains(UNIQUE_CONSTRAINT_MESSAGE));
    }
}
