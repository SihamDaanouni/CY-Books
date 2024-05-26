package com.example.cybook;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * switchToScene6
     * @param event the action event that triggers this method
     * @throws IOException
     * verify if the mail exist and then set the label with the mail for the next scene with the mail that you put
     */
    // switch scene to Scene6.fxml
    public void switchToScene6(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene6.fxml");
    }


    /**
     * switchToConnectedClients
     * When the concerned button is clicked, we switch to the client connection scene
     * @param event the button click
     * @throws IOException exception
     */
    public void switchToConnectedClients(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/ConnexionClient.fxml");
    }

    // We have to ADD : verify that the client exists already in the database


    /**
     * switchToBorrow
     * When the concerned button is clicked, we switch to the borrow scene
     * @param event the button click
     * @throws IOException exception
     */
    public void switchToBorrow(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene7.fxml");
    }
    /**
     * switchToTop10
     * When the concerned button is clicked, we switch to the top 10 borrowed books scene
     * @param event the button click
     * @throws IOException exception
     */
    // switch scene to Scene8.fxml
    public void switchToTop10(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene8.fxml");
    }
    /**
     * disconnect
     * When the concerned button is clicked, we switch to the first scene
     * @param event the button click
     * @throws IOException exception
     */
    // switch scene to Scene2.fxml
    public void disconnect(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene2.fxml");
    }
}