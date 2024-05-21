package com.example.cybook;

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

public class Scene2Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField mailField; // Pour la scène connexion
    @FXML
    private PasswordField passwordField; // Pour la scène connexion
    @FXML
    private void oublieIdentifiant() {
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

    // Méthode pour vérifier les identifiants
    private boolean checkCredentials(String email, String password) {
        String sql = "SELECT * FROM library WHERE mail = ? AND mot_de_passe = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true; // Les identifiants sont corrects
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace(); // Pour afficher les détails de l'erreur SQL
        }
        return false; // Les identifiants sont incorrects
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String email = mailField.getText();
        String password = passwordField.getText();

        if (checkCredentials(email, password)) {
            // Charger la scène suivante
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/cybook/Scene3.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // Afficher un message d'erreur
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
