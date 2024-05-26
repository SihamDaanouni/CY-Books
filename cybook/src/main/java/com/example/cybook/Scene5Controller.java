package com.example.cybook;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import javafx.scene.Parent;

public class Scene5Controller {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField nom; // for the lastname

    @FXML
    private TextField prenom; // for the name

    @FXML
    private TextField email; // for the email address

    @FXML
    private TextField numtel; // for the phone number

    @FXML
    private TextField address; // for the address


    /**
     * switchToConnectedClient
     * switch to ConnectedClient Scene
     *
     * @throws IOException
     */
    public void switchToConnectedClient(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/ConnexionClient.fxml");
    }

    /**
     * isEmailPresent
     * Return true if the email is in the database, and false if it is not
     * @param email
     * @return boolean for the email uniqueness
     */
    private boolean isEmailPresent(String email) {
        String sql = "SELECT * FROM Client WHERE mail = ?";

        try {
            // get the url from resources
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            // convert the url to a path
            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next(); // true if email is found
                }
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false; // false if email is not found or an exception occurred
    }

    /**
     * addUser
     * Add a new user to the client database
     * @param name the name
     * @param firstName the first name
     * @param phone the phone number
     * @param email the email adress
     */
    public void addUser(String name, String firstName, String address, String phone, String email) {
        String sql = "INSERT INTO Client (name, firstName, address, phone, mail) VALUES (?, ?, ?, ?, ?)";

        try {
            // get the url from resources
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            // convert the url to a path
            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, firstName);
                pstmt.setString(3, address);
                pstmt.setString(4, phone);
                pstmt.setString(5, email);

                pstmt.executeUpdate();
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * clientCreation
     * Method that manage the client creation, ok if the email is not used already in the database
     * @param event
     * @throws IOException
     */
    @FXML
    private void ClientCreation(ActionEvent event) throws IOException {
        String mail = email.getText();
        String lastName = nom.getText();
        String addressText = address.getText();
        String name = prenom.getText();
        String phoneNumber = numtel.getText();

        if(mail.isEmpty() || lastName.isEmpty() || name.isEmpty() || phoneNumber.isEmpty() ){
            // not all the information are specified
            showAlert("Erreur", "Un ou plusieurs champs sont vides. Veuillez renseigner tous les champs." );
        }
        else if (!isEmailPresent(mail)) {
            // switch to main menu and register the client in the database
            addUser(name,lastName,addressText,phoneNumber,mail);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La création a été un succès.");
            successAlert.showAndWait();
            Main.switchScene("/com/example/cybook/PageAccueil.fxml");
        } else {
            // the email is already in the database
            showAlert("Erreur de creation", "Email déjà enregistré. Veuillez réessayer avec un email différent ou de connecter le client.");
        }
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