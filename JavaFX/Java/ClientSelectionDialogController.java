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

public class ClientSelectionDialogController {

    @FXML
    private ListView<String> clientListView;
    @FXML
    private DatePicker dateRetourPicker;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minuteSpinner;
    @FXML
    private TextField searchClientField;

    private Stage dialogStage;
    private Book book;
    private boolean confirmed = false;

    private ObservableList<String> clientList = FXCollections.observableArrayList();
    private FilteredList<String> filteredClients;


    @FXML
    public void initialize() {
        loadClients();
        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        hourSpinner.setValueFactory(hourValueFactory);
        minuteSpinner.setValueFactory(minuteValueFactory);

        filteredClients = new FilteredList<>(clientList, p -> true);
        clientListView.setItems(filteredClients);

        searchClientField.textProperty().addListener((observable, oldValue, newValue) -> {
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
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png")));
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

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

                // Vérification du nombre de livres empruntés non retournés pour le client sélectionné
                String selectedClient = clientListView.getSelectionModel().getSelectedItem();
                String[] clientDetails = selectedClient.split(" ");
                String clientMail = clientDetails[2];

                String queryClientBorrows = "SELECT COUNT(*) FROM Borrow WHERE mail = ? AND isReturn = false";
                PreparedStatement statementClientBorrows = connection.prepareStatement(queryClientBorrows);
                statementClientBorrows.setString(1, clientMail);
                ResultSet resultSetClientBorrows = statementClientBorrows.executeQuery();

                if (resultSetClientBorrows.next() && resultSetClientBorrows.getInt(1) >= 5) {
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

                // Vérification du nombre de copies empruntées non retournées pour le livre sélectionné
                String queryBookCopies = "SELECT COUNT(*) FROM Borrow WHERE ISBN = ? AND isReturn = false";
                PreparedStatement statementBookCopies = connection.prepareStatement(queryBookCopies);
                statementBookCopies.setString(1, book.getIsbn());
                ResultSet resultSetBookCopies = statementBookCopies.executeQuery();

                if (resultSetBookCopies.next() && resultSetBookCopies.getInt(1) >= 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Validation Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Le livre n'est pas disponible. Toutes les copies sont actuellement empruntées.");
                    alert.showAndWait();
                } else {
                    confirmed = true;
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("L'emprunt a été un succès.");
                    successAlert.showAndWait();
                    dialogStage.close();
                }

                resultSetBookCopies.close();
                statementBookCopies.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public String getSelectedClient() {
        return clientListView.getSelectionModel().getSelectedItem();
    }

    public LocalDateTime getReturnDate() {
        return LocalDateTime.of(dateRetourPicker.getValue(), LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));
    }
}