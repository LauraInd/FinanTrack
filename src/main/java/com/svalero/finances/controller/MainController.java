package com.svalero.finances.controller;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.TransactionService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;

import static com.svalero.finances.MainApp.springContext;

@Component
public class MainController {
    @FXML
    private Label label;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, Double> colAmount;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private Label lblBalance;

    private final TransactionService service = new TransactionService();

    @FXML
    public void initialize() {
        // Configure table columns
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        colAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        loadTransactions();
    }

    private void loadTransactions() {
        List<Transaction> transactions = service.getAllTransactions();
        transactionTable.getItems().setAll(transactions);
        lblBalance.setText(String.format("%.2f €", service.calculateBalance()));
    }


   /* public void onAddTransaction() {
        // Temporary example for testing database connection
        Transaction t = new Transaction();
        t.setType("Income");
        t.setCategory("Test");
        t.setAmount(100.0);
        t.setDate(LocalDate.now());
        t.setDescription("Example transaction");

        service.addTransaction(t);
        loadTransactions();
        showAlert("Transaction added successfully.");
    }*/

    @FXML
    public void onEditTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a transaction to edit.");
            return;
        }

        selected.setDescription(selected.getDescription() + " (edited)");
        service.updateTransaction(selected);
        loadTransactions();
        showAlert("Transaction updated successfully.");
    }

    @FXML
    public void onDeleteTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a transaction to delete.");
            return;
        }

        service.deleteTransaction(selected.getId());
        loadTransactions();
        showAlert("Transaction deleted successfully.");
    }

    @FXML
    public void onAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/add-transaction.fxml")
            );
            loader.setControllerFactory(springContext::getBean);

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(scene);
            stage.showAndWait();

            // recargar tabla después de añadir
            loadTransactions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FinanTrack");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
