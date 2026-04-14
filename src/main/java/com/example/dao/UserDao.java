package com.example.dao;

import com.example.util.DbUtil;
import com.example.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static final String LOGIN_SQL = "SELECT password FROM users WHERE username = ?";

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
}
