package com.example.scenesaminesiham;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;
    private Scene scene;
    private Stage stage;

    // Setter for the client's Data
    public void setClientData(String name, String firstName, String address, String phone, String email) {
        nameField.setText(name);
        firstNameField.setText(firstName);
        addressField.setText(address);
        phoneField.setText(phone);
        emailField.setText(email);
    }

    @FXML
    private void handleSave() {
        // Recuperate the new data
        String newName = nameField.getText();
        String newFirstName = firstNameField.getText();
        String newAddress = addressField.getText();
        String newPhone = phoneField.getText();

        // Update the database
        String clientEmail;
        clientEmail = null;
        updateClientData(newName, newFirstName, newAddress, newPhone, clientEmail);

        // Close the dialogField after updating the data
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * updateClientData
     * Change a client data and update it in the database, It's not possible to change the email
     *
     * @param name
     * @param firstName
     * @param address
     * @param phone
     * @param email
     */
    private void updateClientData(String name, String firstName, String address, String phone, String email) {
        String url = "jdbc:sqlite:database.db"; // THE URL TO ACESS THE DATABASE

        String sql = "UPDATE Client SET name = ?, firstName = ?, address = ?, phone = ? WHERE mail = ?";

        try (Connection conn = this.connect(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the different fields
            pstmt.setString(1, name);
            pstmt.setString(2, firstName);
            pstmt.setString(3, address);
            pstmt.setString(4, phone);
            //pstmt.setString(5, email);

            pstmt.executeUpdate();
            System.out.println("Les données du client ont été modifiées avec succès");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect(String url) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    @FXML
    private void showClientInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientData.fxml"));
            Parent root = loader.load();

            DataClientController controller = loader.getController();
            controller.setClientData("NomExemple", "PrenomExemple", "AdresseExemple", "0123456789", "exemple@mail.com");

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Modifier les informations du client");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(root);
            dialog.setDialogPane(dialogPane);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PageAccueil.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}


