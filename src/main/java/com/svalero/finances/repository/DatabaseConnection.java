package com.svalero.finances.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:finances.db";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("✅ Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.err.println("❌ Error connecting to database: " + e.getMessage());
            throw e;
        }
        return connection;
    }
}
