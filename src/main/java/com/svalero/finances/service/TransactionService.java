package com.svalero.finances.service;


import com.svalero.finances.domain.Transaction;
import com.svalero.finances.repository.TransactionRepository;
import com.svalero.finances.repository.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

        private final TransactionRepository repository;

        public TransactionService() {
            this.repository = new TransactionRepository();
            repository.createTable(); // Ensures table exists when app starts
        }


        public void addTransaction(Transaction transaction) {
            if (transaction.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
            if (transaction.getType() == null || transaction.getType().isBlank()) {
                throw new IllegalArgumentException("Transaction type is required.");
            }
            if (transaction.getCategory() == null || transaction.getCategory().isBlank()) {
                throw new IllegalArgumentException("Category is required.");
            }

            repository.insert(transaction);
        }


        public void updateTransaction(Transaction transaction) {
            if (transaction.getId() <= 0) {
                throw new IllegalArgumentException("Invalid transaction ID.");
            }
            repository.update(transaction);
        }


        public void deleteTransaction(int id) {
            repository.delete(id);
        }

        public List<Transaction> getAllTransactions() {
            return repository.getAll();
        }

        /**
         * Calculate total balance (Income - Expense).
         */
        public double calculateBalance() {
            List<Transaction> transactions = repository.getAll();
            double balance = 0;

            for (Transaction t : transactions) {
                if (t.getType().equalsIgnoreCase("Income")) {
                    balance += t.getAmount();
                } else if (t.getType().equalsIgnoreCase("Expense")) {
                    balance -= t.getAmount();
                }
            }
            return balance;
        }
    public double getTotalIncome() {
        return repository.getAll().stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return repository.getAll().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


}

