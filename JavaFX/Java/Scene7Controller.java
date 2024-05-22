package com.example.cybooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Scene7Controller {

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
    private TableColumn<Borrow, Boolean> colIsReturn;
    @FXML
    private TableColumn<Borrow, Integer> colNbDelays;
    @FXML
    private Button resetButton;

    private ObservableList<Borrow> masterData = FXCollections.observableArrayList();
    private ObservableList<Borrow> filteredData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colDateStart.setCellValueFactory(new PropertyValueFactory<>("formattedBorrowStart"));
        colDateEnd.setCellValueFactory(new PropertyValueFactory<>("formattedBorrowEnd"));
        colIsReturn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));

        loadBorrow();

        lateButton.setOnAction(event -> onLateButtonPressed());
        resetButton.setOnAction(event -> onResetButtonPressed());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBorrows());
    }

    private void filterBorrows() {
        String searchText = searchField.getText().toLowerCase();
        filteredData.clear();
        for (Borrow borrow : masterData) {
            if (borrow.getFirstname().toLowerCase().contains(searchText) ||
                    borrow.getName().toLowerCase().contains(searchText) ||
                    borrow.getTitle().toLowerCase().contains(searchText) ||
                    borrow.getISBN().toLowerCase().contains(searchText) ||
                    borrow.getMail().toLowerCase().contains(searchText) ||
                    convertLongToLocalDateString(borrow.getTimeBorrowStart()).toLowerCase().contains(searchText) ||
                    convertLongToLocalDateString(borrow.getTimeBorrowEnd()).toLowerCase().contains(searchText) ||
                    Boolean.toString(borrow.getIsReturn()).toLowerCase().contains(searchText)) {
                filteredData.add(borrow);
            }
        }
        tableViewBorrows.setItems(filteredData);
    }

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

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String mail = resultSet.getString("mail");
                String name = resultSet.getString("name");
                String firstname = resultSet.getString("firstname");
                String title = resultSet.getString("title");
                long timeBorrowStart = resultSet.getLong("timeBorrowStart");
                long timeBorrowEnd = resultSet.getLong("timeBorrowEnd");
                boolean isReturn = resultSet.getBoolean("isReturn");
                Borrow borrow = new Borrow(ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn);

                borrow.setFormattedBorrowStart(formatDateTime(timeBorrowStart));
                borrow.setFormattedBorrowEnd(formatDateTime(timeBorrowEnd));

                masterData.add(borrow);
            }

            tableViewBorrows.setItems(masterData);
            filteredData.setAll(masterData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String convertLongToLocalDateString(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @FXML
    private void onLateButtonPressed() {
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

            LocalDateTime currentDateTime = LocalDateTime.now();
            long currentTimeMillis = currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            String query = "SELECT * FROM Borrow WHERE timeBorrowEnd < " + currentTimeMillis + " AND isReturn IS false";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String mail = resultSet.getString("mail");
                String name = resultSet.getString("name");
                String firstname = resultSet.getString("firstname");
                String title = resultSet.getString("title");
                long timeBorrowStart = resultSet.getLong("timeBorrowStart");
                long timeBorrowEnd = resultSet.getLong("timeBorrowEnd");
                boolean isReturn = resultSet.getBoolean("isReturn");
                Borrow borrow = new Borrow(ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn);

                borrow.setFormattedBorrowStart(formatDateTime(timeBorrowStart));
                borrow.setFormattedBorrowEnd(formatDateTime(timeBorrowEnd));

                data.add(borrow);
            }

            tableViewBorrows.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onResetButtonPressed() {
        loadBorrow();
    }

    public void returnMenue(ActionEvent back) throws IOException {
        Main.switchScene("/com/example/cybooks/Scene3.fxml");
    }

    private String formatDateTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
