package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * class ClientSelectionDialogController
 */

public class ClientSelectionDialogController {

    @FXML
    private ListView<String> clientListView;     //  display the list of users
    @FXML
    private DatePicker dateRetourPicker; // the date we choose for the return
    @FXML
    private Spinner<Integer> hourSpinner;   // select the hour of the borrow
    @FXML
    private Spinner<Integer> minuteSpinner; //  select the minutes of the borrow
    @FXML
    private TextField searchClientField;    // Search bar

    private Stage dialogStage; //  declaration dialog stage
    private Book book;
    private boolean confirmed = false; // Book state set as False by default
    // List to show the clients
    private ObservableList<String> clientList = FXCollections.observableArrayList(); // display the list of all the clients
    private FilteredList<String> filteredClients; // generate a filter

    @FXML




    public void initialize() {
/*
  Initializes the controller and loads all customers into the search database.
 */
        loadClients(); // loads all customers into the search database

        //  Initialise spinners with default values
        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        //
        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        hourSpinner.setValueFactory(hourValueFactory);
        minuteSpinner.setValueFactory(minuteValueFactory);

        filteredClients = new FilteredList<>(clientList, p -> true);
        //
        clientListView.setItems(filteredClients);
        // customer search filter

        searchClientField.textProperty().addListener((observable, oldValue, newValue) -> {
            // as soon as you start putting words in the search table
            //  check that the client is not empty
            filteredClients.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return client.toLowerCase().contains(lowerCaseFilter);
            });
            // Show filtered items in the list view
            clientListView.setVisible(true);
        });
    }

    /**
     * setDialogStage
     * Sets up the dialog stage like a pop-up.
     * @param dialogStage the stage that we need for the dialog
     */

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png")));
    }

    /**
     * setBook
     * setter for book
     * @param book the book that will be borrowed
     */

    public void setBook(Book book) {
        this.book = book;
    }

    // return confirmed

    /**
     * isConfirmed
     * returns if the borrowing is confirmed
     * @return true if borrowing okay, false otherwise
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * loadClients
     * search in the database all the clients, used to choose the one who will borrow the book selected
     */
    private void loadClients() {
        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Client");

            while (resultSet.next()) {
                String client = resultSet.getString("name") + " " + resultSet.getString("firstName") + " " + resultSet.getString("mail");
                clientList.add(client);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handleConfirm
     * for save the borrow and verify if all the value have been set, who and when
     */

    @FXML
    private void handleConfirm() {
        if (clientListView.getSelectionModel().isEmpty() || dateRetourPicker.getValue() == null || hourSpinner.getValue() == null || minuteSpinner.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Warning");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs requis.");
            alert.showAndWait();
        } else {
            LocalDateTime selectedDateTime = LocalDateTime.of(dateRetourPicker.getValue(), LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));
            if (selectedDateTime.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Warning");
                alert.setHeaderText(null);
                alert.setContentText("La date de retour ne peut pas être avant l'heure actuelle.");
                alert.showAndWait();
                return;
            }

            try {
                URL resource = getClass().getClassLoader().getResource("database");
                if (resource == null) {
                    throw new IllegalArgumentException("Base de données non trouvée!");
                }

                File dbFile = new File(resource.toURI());
                String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                Connection connection = DriverManager.getConnection(url);

                // check how many book the client had already borrow
                String selectedClient = clientListView.getSelectionModel().getSelectedItem();
                String[] clientDetails = selectedClient.split(" ");
                String clientMail = clientDetails[2];

                String queryClientBorrows = "SELECT COUNT(*) FROM Borrow WHERE mail = ? AND isReturn = false";
                PreparedStatement statementClientBorrows = connection.prepareStatement(queryClientBorrows);
                statementClientBorrows.setString(1, clientMail);
                ResultSet resultSetClientBorrows = statementClientBorrows.executeQuery();

                if (resultSetClientBorrows.next() && resultSetClientBorrows.getInt(1) >= 5) {
                    // warning the client already borrow too many books
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Validation Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Le client a déjà emprunté 5 œuvres. Veuillez retourner des œuvres avant d'emprunter à nouveau.");
                    alert.showAndWait();
                    resultSetClientBorrows.close();
                    statementClientBorrows.close();
                    connection.close();
                    return;
                }

                // check how many borrowed copies not returned for the book
                String queryBookCopies = "SELECT COUNT(*) FROM Borrow WHERE ISBN = ? AND isReturn = false";
                PreparedStatement statementBookCopies = connection.prepareStatement(queryBookCopies);
                statementBookCopies.setString(1, book.getIsbn());
                ResultSet resultSetBookCopies = statementBookCopies.executeQuery();

                if (resultSetBookCopies.next() && resultSetBookCopies.getInt(1) >= 5) {
                    // warning all the book have been borrowed
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Validation Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Le livre n'est pas disponible. Toutes les copies sont actuellement empruntées.");
                    alert.showAndWait();
                } else {
                    // confirmation
                    confirmed = true;
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("L'emprunt a été un succès.");
                    successAlert.showAndWait();
                    dialogStage.close();
                }
                // close the driver
                resultSetBookCopies.close();
                statementBookCopies.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * handleCancel
     * Closes the dialog stage
     */

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * getSelectedClient
     * returns the selected client
     * @return the client that has been selected
     */
    public String getSelectedClient() {
        return clientListView.getSelectionModel().getSelectedItem();
    }

    /**
     * LocalDateTime
     * returns the date and time when the client should return the book
     * @return the return date and time
     */
    public LocalDateTime getReturnDate() {
        return LocalDateTime.of(dateRetourPicker.getValue(), LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));
    }
}