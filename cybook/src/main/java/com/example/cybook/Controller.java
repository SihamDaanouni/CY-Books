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
    /*
    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }
    */
    /**
     * switchToScene6
     * @throws IOException
     * verify if the mail exist and then set the label with the mail for the next scene with the mail that you put
     */
    // switch scene to Scene6.fxml
    public void switchToScene6(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene6.fxml");
    }
    /*
    public void switchToNouveauClient(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/NouveauClient.fxml");
    }
    */

    // switch scene to ConnexionClient.fxml
    public void switchToConnectedClients(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/ConnexionClient.fxml");
    }

    // We have to ADD : verify that the client exists already in the database
    /*
    public void switchToEmprunt1Client(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Emprunt.fxml");
    }
     */

    // switch scene to Scene7.fxml
    public void switchToBorrow(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene7.fxml");
    }


    // switch scene to Scene8.fxml
    public void switchToTop10(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene8.fxml");
    }

    // switch scene to Scene2.fxml
    public void disconnect(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene2.fxml");
    }
}