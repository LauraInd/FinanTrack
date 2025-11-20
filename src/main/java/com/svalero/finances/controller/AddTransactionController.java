package com.svalero.finances.controller;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddTransactionController {

    @FXML private ComboBox<String> cbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtDescription;

    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
       // cbType.getItems().addAll("Income", "Expense");
        dpDate.setValue(LocalDate.now());
    }

    @FXML
    public void onSave() {
        try {
            Transaction t = new Transaction();
            t.setType(cbType.getValue());
            t.setCategory(txtCategory.getText());
            t.setAmount(Double.parseDouble(txtAmount.getText()));
            t.setDate(dpDate.getValue());
            t.setDescription(txtDescription.getText());

            transactionService.addTransaction(t);

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
