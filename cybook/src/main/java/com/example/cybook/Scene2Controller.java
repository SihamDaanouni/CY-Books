package com.example.cybook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;

public class Scene2Controller {

    @FXML
    private TextField mailField; // for the connexion scene
    @FXML
    private PasswordField passwordField; // for the connexion scene
    /**
     * forgotId
     * button when show a dialog if the user lost his password or email name
     *
     */
    // button when show a dialog if the user lost his password or email name
    @FXML
    private void forgotId() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Identifiant ou mot de passe oublié");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez contacter l'administrateur de la bibliothèque pour récupérer vos identifiants et vos mots de passe.");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png")));
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/cybook/warning.png")));
        imageView.setFitHeight(48);
        imageView.setFitWidth(48);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }
    /**
     * Connection
     * is used to be connected to the database
     * @throws  SQLException exception
     * @throws  URISyntaxException exception
     *
     * @return  Connection
     *
     */
    // is used to be connected to the database
    private Connection connect() throws SQLException, URISyntaxException {
        // get the url of the database
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        // convert the url to a path
        File dbFile = new File(resource.toURI());

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        return DriverManager.getConnection(url);
    }
    /**
     * checkCredentials
     * verify the information with the one in the database
     * @param email the librarian email
     * @param password the librarian password

     *
     * @return boolean
     *
     */
    // verify the information with the one in the database
    private boolean checkCredentials(String email, String password) {
        String sql = "SELECT * FROM library WHERE mail = ? AND mot_de_passe = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true; // is correct
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace(); // show the url error
        }
        return false; // is incorrect
    }
    /**
     * handleLogin
     * get the information in the TextField and test them with the one in the database, if correct access to the next scene
     * @param event the button click
     * @throws  IOException exception
     */

    // get the information in the TextField and test them with the one in the database, if correct access to the next scene
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String email = mailField.getText();
        String password = passwordField.getText();

        if (checkCredentials(email, password)) {
            // switch scene to PageAccueil.fxml
            Main.switchScene("/com/example/cybook/PageAccueil.fxml");
        } else {
            // show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Email ou mot de passe incorrect. Veuillez réessayer.");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png")));
            alert.showAndWait();
        }
    }
}