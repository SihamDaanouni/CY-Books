package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Scene7Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button lateButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Borrow> tableViewBorrows;
    @FXML
    private TableColumn<Borrow, String> colFirstName;
    @FXML
    private TableColumn<Borrow, String> colName;
    @FXML
    private TableColumn<Borrow, String> colTitle;
    @FXML
    private TableColumn<Borrow, String> colISBN;
    @FXML
    private TableColumn<Borrow, String> colMail;
    @FXML
    private TableColumn<Borrow, String> colDateStart;
    @FXML
    private TableColumn<Borrow, String> colDateEnd;
    @FXML
    private TableColumn<Borrow, String> colIsReturn;
    @FXML
    private TableColumn<Borrow, Integer> colNbDelays;
    @FXML
    private Button resetButton;

    // 2 stack for the searchbar (one for the back and one to put in the tableView)
    private ObservableList<Borrow> masterData = FXCollections.observableArrayList();
    private ObservableList<Borrow> filteredData = FXCollections.observableArrayList();

    // initialize the scene
    @FXML
    public void initialize() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colDateStart.setCellValueFactory(new PropertyValueFactory<>("timeBorrowStart"));
        colDateEnd.setCellValueFactory(new PropertyValueFactory<>("timeBorrowEnd"));
        colIsReturn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));
        colNbDelays.setCellValueFactory(new PropertyValueFactory<>("nbDelays"));

        loadBorrow();

        lateButton.setOnAction(event -> onLateButtonPressed());
        resetButton.setOnAction(event -> onResetButtonPressed());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBorrows());
    }

    // is used for the searchbar
    private void filterBorrows() {
        String searchText = searchField.getText().toLowerCase();
        filteredData.clear();
        for (Borrow borrow : masterData) {
            if (borrow.getFirstname().toLowerCase().contains(searchText) ||
                    borrow.getName().toLowerCase().contains(searchText) ||
                    borrow.getTitle().toLowerCase().contains(searchText) ||
                    borrow.getISBN().toLowerCase().contains(searchText) ||
                    borrow.getMail().toLowerCase().contains(searchText) ||
                    borrow.getTimeBorrowStart().toLowerCase().contains(searchText) ||
                    borrow.getTimeBorrowEnd().toLowerCase().contains(searchText) ||
                    borrow.getIsReturn().toLowerCase().contains(searchText)) {
                filteredData.add(borrow);
            }
        }
        tableViewBorrows.setItems(filteredData);
    }

    // take all the data from Borrow and put it in the tableView
    private void loadBorrow() {
        masterData.clear();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Borrow");

            // Parcourir les résultats et ajouter les éléments à la liste
            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String mail = resultSet.getString("mail");
                String name = resultSet.getString("name");
                String firstname = resultSet.getString("firstname");
                String title = resultSet.getString("title");
                String timeBorrowStart = resultSet.getString("timeBorrowStart");
                String timeBorrowEnd = resultSet.getString("timeBorrowEnd");
                String isReturn = resultSet.getString("isReturn");
                int nbDelays = resultSet.getInt("nbDelays");
                Borrow borrow = new Borrow(ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn, nbDelays);
                masterData.add(borrow);
            }

            // Mettre à jour le TableView avec les données récupérées
            tableViewBorrows.setItems(masterData);
            filteredData.setAll(masterData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // button to show the people who are late (isReturn = false and dateEnd passed)
    @FXML
    private void onLateButtonPressed() {
        masterData.clear();
        ObservableList<Borrow> data = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();

            // Obtenir la date actuelle
            LocalDate currentDate = LocalDate.now();

            // Requête pour obtenir les emprunts en retard
            String query = "SELECT * FROM Borrow WHERE timeBorrowEnd < '" + currentDate.toString() + "' AND isReturn IS 'no'";

            resultSet = statement.executeQuery(query);

            // Parcourir les résultats et ajouter les éléments à la liste
            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String mail = resultSet.getString("mail");
                String name = resultSet.getString("name");
                String firstname = resultSet.getString("firstname");
                String title = resultSet.getString("title");
                String timeBorrowStart = resultSet.getString("timeBorrowStart");
                String timeBorrowEnd = resultSet.getString("timeBorrowEnd");
                String isReturn = resultSet.getString("isReturn");
                int nbDelays = resultSet.getInt("nbDelays");
                Borrow borrow = new Borrow(ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn, nbDelays);
                data.add(borrow);
                masterData.add(borrow);
            }

            // Mettre à jour le TableView avec les données récupérées
            tableViewBorrows.setItems(masterData);
            filteredData.setAll(masterData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // reload the borrow table to get back to it inital form
    @FXML
    private void onResetButtonPressed() {
        loadBorrow();
    }

    // Button to return to Scene3
    public void returnMenue(ActionEvent back) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Scene3.fxml"));
        stage = (Stage) ((Node) back.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
