package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class Scene8Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Book> tableViewBooks;
    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colTitre;
    @FXML
    private TableColumn<Book, String> colAuteur;
    @FXML
    private TableColumn<Book, String> colTheme;
    @FXML
    private TableColumn<Book, String> colIsbn;
    @FXML
    private TableColumn<Book, String> colEditeur;
    @FXML
    private TableColumn<Book, String> colLieu;
    @FXML
    private TableColumn<Book, String> colAnnee;

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("author"));
        colTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
        colEditeur.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Charger les données
        List<Book> books = databaseUtils.bestBooksLastMonth();
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
        tableViewBooks.setItems(observableBooks);
    }
}
