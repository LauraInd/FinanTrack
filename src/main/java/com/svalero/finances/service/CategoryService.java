package com.svalero.finances.service;

import com.svalero.finances.repository.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public CategoryService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Error creating categories table: " + e.getMessage());
        }
    }

    /**
     * Returns all category names as List<String>
     */
    public List<String> getAllCategories() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Adds a new category (String)
     */
    public void addCategory(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Category already exists or invalid: " + e.getMessage());
        }
    }

    /**
     * Deletes a category by name (not id)
     */
    public void deleteCategory(String name) {
        String sql = "DELETE FROM categories WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete category: " + e.getMessage());
        }
    }
}
