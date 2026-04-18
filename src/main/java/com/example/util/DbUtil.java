package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbUtil {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DbUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found");
            }
            PROPS.load(in);
            Class.forName(readConfig("db.driver", "DB_DRIVER"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DbUtil() {
    }

    public static Connection getConnection() throws SQLException {
        String url = readConfig("db.url", "DB_URL");
        String username = readConfig("db.username", "DB_USERNAME");
        String password = readConfig("db.password", "DB_PASSWORD");

        if (isBlank(username) && isBlank(password)) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, username, password);
    }

    private static String readConfig(String propertyKey, String envKey) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return PROPS.getProperty(propertyKey);
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
