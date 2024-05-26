package com.example.cybook;

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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    /**
     * getEmail
     * A method that returns the email that has been informed
     * @return the email
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * setClientData
     * Setter for the client's Data
     * @param name the client name
     * @param firstName the client firstname
     * @param address the client address
     * @param phone the client phone number
     * @param email the client email
     */
    public void setClientData(String name, String firstName, String address, String phone, String email) {
        nameField.setText(name);
        firstNameField.setText(firstName);
        addressField.setText(address);
        phoneField.setText(phone);
        emailField.setText(email);
    }

    /**
     * hadleSave
     * update the database with updateClientData(); with the data set in the different textField
     * @throws URISyntaxException exception
     */
    @FXML
    private void handleSave() throws URISyntaxException {
        // Recuperate the new data
        String newName = nameField.getText();
        String newFirstName = firstNameField.getText();
        String newAddress = addressField.getText();
        String newPhone = phoneField.getText();

        updateClientData(newName, newFirstName, newAddress, newPhone, getEmail());

        // close the dialogField after updating the data
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * updateClientData
     * Change a client data and update it in the database, It's not possible to change the email
     *
     * @param name the client name
     * @param firstName the client firstname
     * @param address the client address
     * @param phone the client phone number
     * @param email the client email
     * @throws URISyntaxException exception
     */
    private void updateClientData(String name, String firstName, String address, String phone, String email) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        String sql = "UPDATE Client SET name = ?, firstName = ?, address = ?, phone = ? WHERE mail = ?";
        String sql2 = "UPDATE Borrow SET name = ?, firstName = ? WHERE mail = ?";
        Connection conn = this.connect(url);

        try {
            // first prepared statement for the Client update
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Set the different fields
            pstmt.setString(1, name);
            pstmt.setString(2, firstName);
            pstmt.setString(3, address);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);

            pstmt.executeUpdate();

            // second prepared statement for the Borrow update
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // Set the different fields
            pstmt2.setString(1, name);
            pstmt2.setString(2, firstName);
            pstmt2.setString(3, email);

            pstmt2.executeUpdate();

            setClientData(name,firstName,address,phone,email);
            System.out.println("Les données du client ont été modifiées avec succès");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // close the driver connection
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Connection
     * allow connection to the driver
     * @param url url
     */
    private Connection connect(String url) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}