package com.example.cybook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * ConnexionClientController
 */
public class ConnexionClientController {

    @FXML
    private TextField mailField;


    /**
     * handleLogin
     * @param event push a button
     * @throws URISyntaxException
     * verify if the mail exist and then set the label with the mail for the next scene with the mail that you put
     */

    // verify if the mail exist and then set the label with the mail for the next scene with the mail that you put
    @FXML
    private void handleLogin(ActionEvent event) throws IOException, URISyntaxException {
        String email = mailField.getText();

        if (isClientExists(email)) {
            Main.switchScene("/com/example/cybook/Emprunt.fxml");
            Emprunt1ClientController empruntController = (Emprunt1ClientController) Main.getController();
            empruntController.displayClientMail(email);
        } else {
            showAlert("Erreur de connexion", "Email incorrect, l'utilisateur n'existe pas dans la base de données");
        }
    }

    /**
     * isClientExists
     * @param email
     * verify in the database if the email in parameter exist
     */


    // verify in the database if the email in parameter exist
    private boolean isClientExists(String email) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        String query = "SELECT * FROM Client WHERE mail = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // switch scene to NouveauClient.fxml
    @FXML
    private void switchToNewClients(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/NouveauClient.fxml");
    }

    // switch scene to PageAccueil.fxml
    @FXML
    private void switchToHomePage(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }

    // show an alert message

    /**
     * showAlert
     * allows you to generate specific errors
     * @param title title of the alert
     * @param message alert message
     *
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

