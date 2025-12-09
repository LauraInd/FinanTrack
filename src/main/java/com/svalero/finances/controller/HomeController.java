package com.svalero.finances.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Button btnOpenFinances;

    @FXML
    public void onOpenFinances() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Finances");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) btnOpenFinances.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading finances screen:\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onExit() {
        System.exit(0);
    }
}
