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

    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }
    public void switchToScene6(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene6.fxml");
    }
    public void switchToNouveauClient(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/NouveauClient.fxml");
    }

    public void switchToClientConnecte(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/ConnexionClient.fxml");
    }

    // We have to ADD : verify that the client exists already in the database

    public void switchToEmprunt1Client(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Emprunt.fxml");
    }


    public void switchToEmprunts(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene7.fxml");
    }


    public void switchToTop10(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene8.fxml");
    }
    public void deconnexion(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene2.fxml");
    }
}