package com.svalero.finances;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // Inicializar Spring Boot
        springContext = new SpringApplicationBuilder(FinancesApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        loader.setControllerFactory(springContext::getBean); // Integración con Spring
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Finances Desktop App");
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
