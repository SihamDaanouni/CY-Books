package com.example.cybook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static FXMLLoader fxmlLoader;

    /**
     * start
     * start function, set up the root
     * @param stage
     * @throws IOException
     *
     *
     */

    // start function, set up the root
    @Override
    public void start(Stage stage) throws IOException {


        primaryStage = stage;
        fxmlLoader = new FXMLLoader(Main.class.getResource("Scene2.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("CYBooks");

        // add the CSS file
        String css = this.getClass().getResource("Cybook.css").toExternalForm();
        scene.getStylesheets().add(css);

        // adjust the windows size by fullscreen
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * switchScene
     * the main function to switch scene
     * @param  fxml
     * @throws IOException
     *
     *
     */
    // the main function to switch scene
    public static void switchScene(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
        // Maintenir la fenêtre maximisée lorsque la scène est changée
        primaryStage.setMaximized(true);
    }

    // get the controller
    public static Object getController() {
        return fxmlLoader.getController();
    }



    // lunch the app with arguments
    public static void main(String[] args) {
        launch(args);
    }
}