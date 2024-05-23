package com.example.scenesaminesiham;


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
        Parent root = FXMLLoader.load(getClass().getResource("PageAccueil.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToNouveauClient(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NouveauClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClientConnecte(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ConnexionClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // We have to ADD : verify that the client exists already in the database

    public void switchToEmprunt1Client(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Emprunt.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToEmprunts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Scene7.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToTop10(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Scene8.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
