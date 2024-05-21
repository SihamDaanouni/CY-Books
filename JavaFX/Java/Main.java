package com.example.cybook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Scene1.fxml"));
        //Email: Eva@gmal.com
        //MDP: 2548 
        Scene scene = new Scene(fxmlLoader.load());
        Image icon = new Image(getClass().getResourceAsStream("/com/example/cybooks/logocy.png"));
        stage.getIcons().add(icon);
        stage.setTitle("CYBooks");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
