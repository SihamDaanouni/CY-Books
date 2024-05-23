package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Emprunt1ClientController {

    @FXML
    private TableView<Borrow> tableViewBorrow;
    @FXML
    private TableColumn<Borrow, String> isbnColumn;
    @FXML
    private TableColumn<Borrow, String> nameColumn;
    @FXML
    private TableColumn<Borrow, String> firstnameColumn;
    @FXML
    private TableColumn<Borrow, String> titleColumn;
    @FXML
    private TableColumn<Borrow, String> timeBorrowEndColumn;
    @FXML
    private TableColumn<Borrow, Boolean> isReturnColumn;
    @FXML
    private TableColumn<Borrow, Void> rendreColumn;
    @FXML
    private Label userMail;

    @FXML
    private Label clientMailLabel;

    @FXML
    private Label pageTitleLabel;

    @FXML
    public void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        timeBorrowEndColumn.setCellValueFactory(new PropertyValueFactory<>("formattedBorrowEnd"));
        isReturnColumn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));
        rendreColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Rendre");

            {
                btn.setOnAction(event -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    returnBook(borrow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    public void displayClientMail(String email) {
        userMail.setText("Client : " + email);
        loadBorrows(email);
    }

    private void loadBorrows(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);

            String query = "SELECT * FROM Borrow WHERE mail = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            ObservableList<Borrow> borrowList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Borrow borrow = new Borrow(
                        resultSet.getString("ISBN"),
                        resultSet.getString("mail"),
                        resultSet.getString("name"),
                        resultSet.getString("firstname"),
                        resultSet.getString("title"),
                        resultSet.getLong("timeBorrowStart"),
                        resultSet.getLong("timeBorrowEnd"),
                        resultSet.getBoolean("isReturn")
                );
                borrow.setFormattedBorrowEnd(formatDateTime(resultSet.getLong("timeBorrowEnd")));
                if(borrow.getIsReturn() == false) {borrowList.add(borrow);}
            }
            tableViewBorrow.setItems(borrowList);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatDateTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    private void returnBook(Borrow borrow) {
        // Update the database to mark the book as returned
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);

            String updateQuery = "UPDATE Borrow SET isReturn = ?, timeBorrowEnd = ? WHERE ISBN = ? AND mail = ?";
            statement = connection.prepareStatement(updateQuery);
            statement.setBoolean(1, true);
            long currentTimeMillis = System.currentTimeMillis();
            statement.setLong(2, currentTimeMillis);
            statement.setString(3, borrow.getISBN());
            statement.setString(4, borrow.getMail());
            statement.executeUpdate();

            borrow.setReturn(true);
            borrow.setTimeBorrowEnd(currentTimeMillis);
            borrow.setFormattedBorrowEnd(formatDateTime(currentTimeMillis));
            tableViewBorrow.refresh();
        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void switchToPageAccueil(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }

    @FXML
    private void showClientInfo(ActionEvent event) throws IOException {
        // Logic to show client info
    }
}
