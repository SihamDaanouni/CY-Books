package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;

public class Emprunt1ClientController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Borrow> tableViewBorrow;
    @FXML
    private TableColumn<Borrow, String> isbnColumn;
    @FXML
    private TableColumn<Borrow, String> titleColumn;
    @FXML
    private TableColumn<Borrow, String> nameColumn;
    @FXML
    private TableColumn<Borrow, String> firstnameColumn;
    @FXML
    private TableColumn<Borrow, Long> timeBorrowEndColumn;
    @FXML
    private TableColumn<Borrow, Boolean> isReturnColumn;
    @FXML
    private TableColumn<Borrow, Void> rendreColumn; // Ajout de la colonne "Rendre"
    @FXML
    private Label userMail;

    private ObservableList<Borrow> borrowList;

    @FXML
    public void initialize() {
        try {
            borrowList = FXCollections.observableArrayList();

            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            timeBorrowEndColumn.setCellValueFactory(new PropertyValueFactory<>("timeBorrowEnd"));
            isReturnColumn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));

            // Configuration de la colonne "Rendre"
            rendreColumn.setCellFactory(new Callback<TableColumn<Borrow, Void>, TableCell<Borrow, Void>>() {
                public TableCell<Borrow, Void> call(final TableColumn<Borrow, Void> param) {
                    final TableCell<Borrow, Void> cell = new TableCell<Borrow, Void>() {
                        private final Button btn = new Button("Rendre");

                        {
                            btn.setOnAction(event -> {
                                Borrow borrow = getTableView().getItems().get(getIndex());
                                // Mettre à jour la valeur de isReturn dans la base de données
                                try {
                                    updateIsReturnInDatabase(borrow);
                                } catch (URISyntaxException e) {
                                    throw new RuntimeException(e);
                                }

                                // Actualiser la table des emprunts
                                try {
                                    loadEmprunts(userMail.getText());
                                } catch (URISyntaxException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                Borrow borrow = getTableView().getItems().get(getIndex());
                                if (!borrow.getIsReturn()) {
                                    setGraphic(btn);
                                } else {
                                    setGraphic(null);
                                }
                            }
                        }
                    };
                    return cell;
                }
            });

            tableViewBorrow.setItems(borrowList);

            // Charger les emprunts
            loadEmprunts(userMail.getText());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void updateIsReturnInDatabase(Borrow borrow) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        String query = "UPDATE Borrow SET isReturn = 1 WHERE ISBN = ? AND mail = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, borrow.getISBN());
            stmt.setString(2, borrow.getMail());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmprunts(String email) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        String query = "SELECT ISBN, name, firstname, title, timeBorrowEnd, isReturn FROM Borrow WHERE mail = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            borrowList.clear(); // Clear the existing list before adding new data
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String title = rs.getString("title");
                String name = rs.getString("name");
                String firstname = rs.getString("firstname");
                long timeBorrowEnd = rs.getLong("timeBorrowEnd");
                boolean isReturn = rs.getBoolean("isReturn");

                Borrow borrow = new Borrow(isbn, email, name, firstname, title, 0, timeBorrowEnd, isReturn);
                borrowList.add(borrow);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayClientMail(String email) {
        userMail.setText(email);
    }

    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }

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
            } else {
                // Si aucun client n'est trouvé avec l'e-mail donné, affichez un message d'erreur
                showAlert("Erreur", "Aucun client trouvé avec cet e-mail.");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
