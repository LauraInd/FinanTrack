package com.svalero.finances.service;


import com.svalero.finances.domain.Transaction;
import com.svalero.finances.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

        private final TransactionRepository repository;

        public TransactionService() {
            this.repository = new TransactionRepository();
            repository.createTable(); // Ensures table exists when app starts
        }

        /**
         * Add a new transaction after validation.
         */
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

        /**
         * Update an existing transaction.
         */
        public void updateTransaction(Transaction transaction) {
            if (transaction.getId() <= 0) {
                throw new IllegalArgumentException("Invalid transaction ID.");
            }
            repository.update(transaction);
        }

        /**
         * Delete a transaction by ID.
         */
        public void deleteTransaction(int id) {
            repository.delete(id);
        }

        /**
         * Get all transactions from the database.
         */
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
}

