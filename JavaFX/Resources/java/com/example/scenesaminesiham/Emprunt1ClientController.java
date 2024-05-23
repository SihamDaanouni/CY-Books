package com.example.scenesaminesiham;

import com.example.scenesaminesiham.Borrow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.*;

public class Emprunt1ClientController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private TableView<Borrow> tableView;
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
                            updateIsReturnInDatabase(borrow);

                            // Actualiser la table des emprunts
                            loadEmprunts(userMail.getText());
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

        tableView.setItems(borrowList);

        // Charger les emprunts
        loadEmprunts(userMail.getText());
    }


    private void updateIsReturnInDatabase(Borrow borrow) {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";

        String query = "UPDATE Borrow SET isReturn = 1 WHERE ISBN = ? AND mail = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, borrow.getISBN());
            stmt.setString(2, borrow.getMail());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadEmprunts(String email) {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";

        String query = "SELECT ISBN, name, firstname, title, timeBorrowEnd, isReturn FROM Borrow WHERE mail = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

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

    /**
     * displayClientMail
     *
     * Show the client email on the screen
     *
     * @param email
     * @return
     */
    public void displayClientMail(String email){
        userMail.setText(email);
    }

    /**
     * switchToPageAccueil
     *
     * This method redirect us to the maine menu
     *
     * @param event
     */
    public void switchToPageAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PageAccueil.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
        try{
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

}



