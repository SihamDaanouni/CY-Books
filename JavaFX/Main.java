package com.example.cybook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("PageAccueil.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("CYBooks");

        // Ajuster la taille de la fenêtre pour qu'elle prenne tout l'écran sans être en plein écran
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void switchScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
        // Maintenir la fenêtre maximisée lorsque la scène est changée
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
