package com.svalero.finances.controller;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.CategoryService;
import com.svalero.finances.service.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditTransactionController {

    @FXML private ComboBox<String> cbType;
    @FXML private ComboBox<String> cbCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtDescription;

    private final TransactionService transactionService = new TransactionService();
    private final CategoryService categoryService = new CategoryService();

    private Transaction transaction;

    @FXML
    public void initialize() {
        cbType.getItems().addAll("Income", "Expense");
        loadCategories();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        cbType.setValue(transaction.getType());
        cbCategory.setValue(transaction.getCategory());
        txtAmount.setText(String.valueOf(transaction.getAmount()));
        dpDate.setValue(transaction.getDate());
        txtDescription.setText(transaction.getDescription());
    }

    @FXML
    public void onSave() {
        try {
            transaction.setType(cbType.getValue());
            transaction.setCategory(cbCategory.getValue());
            transaction.setAmount(Double.parseDouble(txtAmount.getText()));
            transaction.setDate(dpDate.getValue());
            transaction.setDescription(txtDescription.getText());

            transactionService.updateTransaction(transaction);

            Stage stage = (Stage) cbType.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        ((Stage) cbType.getScene().getWindow()).close();
    }

    public void loadCategories() {
        cbCategory.getItems().setAll(categoryService.getAllCategories());
    }
}

