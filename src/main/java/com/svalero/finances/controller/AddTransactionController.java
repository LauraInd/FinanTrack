package com.svalero.finances.controller;

import com.svalero.finances.domain.Transaction;
import com.svalero.finances.service.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class AddTransactionController {

    @FXML private ComboBox<String> cbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtDescription;

    @Autowired
    private TransactionService transactionService;

    @FXML
    public void onSave() {
        try {
            Transaction t = new Transaction();
            t.setType(cbType.getValue());
            t.setCategory(txtCategory.getText());
            t.setAmount(Double.parseDouble(txtAmount.getText()));
            t.setDate(LocalDate.parse(dpDate.getValue().toString()));
            t.setDescription(txtDescription.getText());

            transactionService.addTransaction(t);

            // cerrar ventana
            Stage stage = (Stage) cbType.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }
}
