package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

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

    // initialize when the scene is generated

    /**
     * initialize
     * initialize data
     */
    @FXML
    public void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        timeBorrowEndColumn.setCellValueFactory(new PropertyValueFactory<>("formattedBorrowEnd"));
        isReturnColumn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));
        rendreColumn.setCellFactory(param -> new TableCell<>() {
            // generate the return book button
            private final Button btn = new Button("Rendre");

            {
                btn.setOnAction(event -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    returnBook(borrow);
                });
            }

            // update the isReturn value in the Column
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

    /**
     * displayClientMail
     *  set the email client and load all the borrow from the client
     *  loadBorrows() can't be possible in the initialize because he need the set of userMail before
     * @param email mail of client
     */
    //
    //
    public void displayClientMail(String email) {
        userMail.setText(email);
        loadBorrows(email);
    }

    // set all the borrow from the client
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

    // give the local date in the right format
    private String formatDateTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    /**
     * returnBook$
     * update isReturn and the time when return of a borrow
     * @param borrow the concerned borrow
     */
    // update isReturn and the time when return of a borrow
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

    // switch scene to PageAccueil.fxml

    /**
     * switchToHomePage
     *  switch scene to PageAccueil.fxml
     * @param event the button click
     *
     * @throws IOException exception
     *
     *
     */
    @FXML
    private void switchToHomePage(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }
    /**
     * showClientInfo
     *  show information of the client then open the dialog for modify them
     */
    // show information of the client then open the dialog for modify them
    @FXML
    private void showClientInfo() {
        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            String sql = "SELECT * FROM Client WHERE mail = ?";

            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, userMail.getText());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("name");
                String prenom = rs.getString("firstName");
                String adresse = rs.getString("address");
                String telephone = rs.getString("phone");
                String email = rs.getString("mail");

                conn.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("DataClient.fxml"));
                Parent root = loader.load();

                DataClientController controller = loader.getController();

                controller.setClientData(nom, prenom, adresse, telephone, email);

                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Modifier les informations du client");
                DialogPane dialogPane = new DialogPane();
                dialogPane.setContent(root);
                dialog.setDialogPane(dialogPane);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
                loadBorrows(email);
            } else {
                // if there is no Client associate with this email show error (mainly for flag)
                showAlert("Erreur", "Aucun client trouvé avec cet e-mail.");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * showAlert
     *  show an error message
     * @param title the alert title
     * @param message the alert message
     *
     *
     */

    // show an error message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}