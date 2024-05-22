package com.example.scenesaminesiham;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class ConnexionClientController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField mailField;

    // Méthode de connexion à la base de données
    private Connection connect() throws SQLException, URISyntaxException {
        // Obtenir l'URL du fichier de base de données dans le répertoire resources
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        // Convertir l'URL en chemin de fichier
        File dbFile = new File(resource.toURI());

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        return DriverManager.getConnection(url);
    }


    /**
     * checkCredentials : To verify the validity of the client email
     * @param email
     * @return true if the email has been recognized
     *         False if not existant in the database
     */
    private boolean checkCredentials(String email) {
        String sql = "SELECT * FROM library WHERE mail = ? ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true; // The email adress has been found by the sql request
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace(); // display more details about the SQL Exception
        }
        return false; // the email adress doesn't exist in the database
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String email = mailField.getText();

        if(email.isEmpty()){
            // the email field is empty
            showAlert("Erreur", "Veuillez indiquer le mail" );
        }

        else if (checkCredentials(email)) {
            // If the email is correct, pass to the next scene
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/scenesaminesiham/Emprunt.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // Display an alert with error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Email incorrect, l'utilisateur n'existe pas dans la base de données");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/scenesaminesiham/logocy.png")));
            alert.showAndWait();
        }
    }

    public void switchToNouveauClient(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NouveauClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PageAccueil.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * ShowAlert
     * Display a specific message as an alert
     * @param title
     * @param message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}