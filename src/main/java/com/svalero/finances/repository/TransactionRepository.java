package com.svalero.finances.repository;

import com.svalero.finances.domain.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "amount REAL NOT NULL, " +
                "date TEXT NOT NULL, " +
                "description TEXT" +
                ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Table 'transactions' verified/created.");
        } catch (SQLException e) {
            System.err.println("❌ Error creating table: " + e.getMessage());
        }
    }

    public void insert(Transaction t) {
        String sql = "INSERT INTO transactions(type, category, amount, date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getType());
            pstmt.setString(2, t.getCategory());
            pstmt.setDouble(3, t.getAmount());
            pstmt.setString(4, t.getDate().toString());
            pstmt.setString(5, t.getDescription());
            pstmt.executeUpdate();

            System.out.println("✅ Transaction added successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Error inserting transaction: " + e.getMessage());
        }
    }

    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setType(rs.getString("type"));
                t.setCategory(rs.getString("category"));
                t.setAmount(rs.getDouble("amount"));
                t.setDate(LocalDate.parse(rs.getString("date")));
                t.setDescription(rs.getString("description"));
                transactions.add(t);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching transactions: " + e.getMessage());
        }

        return transactions;
    }

    public void update(Transaction t) {
        String sql = "UPDATE transactions SET type = ?, category = ?, amount = ?, date = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getType());
            pstmt.setString(2, t.getCategory());
            pstmt.setDouble(3, t.getAmount());
            pstmt.setString(4, t.getDate().toString());
            pstmt.setString(5, t.getDescription());
            pstmt.setInt(6, t.getId());
            pstmt.executeUpdate();

            System.out.println("✅ Transaction updated successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Error updating transaction: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            System.out.println("✅ Transaction deleted successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Error deleting transaction: " + e.getMessage());
        }
    }
}
