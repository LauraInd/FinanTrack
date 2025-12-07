package com.svalero.finances;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import static com.svalero.finances.MainApp.springContext;

public class MainApp extends Application {

    public static ConfigurableApplicationContext springContext;

    @Override
    public void init() {
       // springContext = new SpringApplicationBuilder(FinancesApplication.class).run();
    }

  /*  @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        loader.setControllerFactory(springContext::getBean); // Integración con Spring
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Finances Desktop App");
        primaryStage.show();
    } */

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400,400);
        stage.setTitle("FinanTrack - Home");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
