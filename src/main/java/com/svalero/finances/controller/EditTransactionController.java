package com.svalero.finances.controller;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditTransactionController {

    @FXML private ComboBox<String> cbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtDescription;

    private final TransactionService transactionService = new TransactionService();
    private Transaction transaction; // La transacción que vamos a editar

    @FXML
    public void initialize() {
        cbType.getItems().addAll("Income", "Expense");
    }

    /**
     * Método para pasar la transacción seleccionada desde MainController
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        // Rellenar los campos con los valores actuales
        cbType.setValue(transaction.getType());
        txtCategory.setText(transaction.getCategory());
        txtAmount.setText(String.valueOf(transaction.getAmount()));
        dpDate.setValue(transaction.getDate());
        txtDescription.setText(transaction.getDescription());
    }

    @FXML
    public void onSave() {
        try {
            transaction.setType(cbType.getValue());
            transaction.setCategory(txtCategory.getText());
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
}

