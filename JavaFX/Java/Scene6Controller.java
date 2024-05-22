package com.example.cybook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class Scene6Controller {

    private Stage stage;
    private Scene scene;

    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private TextField searchFieldBNF;
    @FXML
    private Button resetButton;
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
    private TableColumn<Book, Void> colAction;

    private ObservableList<Book> masterData = FXCollections.observableArrayList();
    private ObservableList<Book> filteredData = FXCollections.observableArrayList();
    private int currentId = 1;  // Variable pour gérer l'incrémentation des ID
    private Task<Void> currentTask; // Tâche en cours

    @FXML
    public void initialize() {
        filterComboBox.getItems().addAll("Titre", "Auteur", "Thème", "ISBN", "Éditeur", "Lieu", "Année");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colEditeur.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Emprunter");

            {
                btn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showClientSelectionDialog(book);
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

        tableViewBooks.setItems(filteredData);
        loadBooks();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks());
    }

    private void loadBooks() {
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
            resultSet = statement.executeQuery("SELECT * FROM Books");

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("auteur"),
                        resultSet.getString("theme"),
                        resultSet.getString("isbn"),
                        resultSet.getString("editeur"),
                        resultSet.getString("lieu"),
                        resultSet.getString("annee")
                );
                masterData.add(book);
            }
            filteredData.addAll(masterData);

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

    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase();
        filteredData.clear();
        for (Book book : masterData) {
            if (book.getTitre().toLowerCase().contains(searchText) ||
                    book.getAuteur().toLowerCase().contains(searchText) ||
                    book.getTheme().toLowerCase().contains(searchText) ||
                    book.getIsbn().toLowerCase().contains(searchText) ||
                    book.getEditeur().toLowerCase().contains(searchText) ||
                    book.getLieu().toLowerCase().contains(searchText) ||
                    book.getAnnee().toLowerCase().contains(searchText)) {
                filteredData.add(book);
            }
        }
    }

    public void handleFilter(ActionEvent event) {
        String selectedFilter = filterComboBox.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filtre");
        dialog.setHeaderText("Recherche par " + selectedFilter);
        dialog.setContentText("Veuillez entrer " + selectedFilter + ":");

        dialog.showAndWait().ifPresent(value -> {
            filteredData.clear();
            for (Book book : masterData) {
                switch (selectedFilter) {
                    case "Titre":
                        if (book.getTitre().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "Auteur":
                        if (book.getAuteur().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "Thème":
                        if (book.getTheme().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "ISBN":
                        if (book.getIsbn().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "Éditeur":
                        if (book.getEditeur().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "Lieu":
                        if (book.getLieu().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                    case "Année":
                        if (book.getAnnee().toLowerCase().contains(value.toLowerCase())) {
                            filteredData.add(book);
                        }
                        break;
                }
            }
        });
    }

    public void handleReset(ActionEvent event) {
        searchField.clear();
        filteredData.clear();
        filteredData.addAll(masterData);
    }

    @FXML
    private void showClientSelectionDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientSelectionDialog.fxml"));
            Parent root = loader.load();

            ClientSelectionDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sélectionnez un client et une date de retour");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            controller.setDialogStage(dialogStage);
            controller.setBook(book);
            dialogStage.showAndWait();

            if (controller.isConfirmed()) {
                String selectedClient = controller.getSelectedClient();
                LocalDateTime returnDate = controller.getReturnDate();
                String[] clientDetails = selectedClient.split(" ");
                String clientName = clientDetails[0];
                String clientFirstName = clientDetails[1];
                String clientMail = clientDetails[2];

                insertBorrowRecord(book, clientMail, clientName, clientFirstName, returnDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertBorrowRecord(Book book, String mail, String name, String firstName, LocalDateTime returnDate) {
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

            String query = "INSERT INTO Borrow (ISBN, mail, name, firstname, title, timeBorrowStart, timeBorrowEnd, isReturn) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, book.getIsbn());
            statement.setString(2, mail);
            statement.setString(3, name);
            statement.setString(4, firstName);
            statement.setString(5, book.getTitre());
            long borrowStartTime = System.currentTimeMillis();
            statement.setLong(6, borrowStartTime);
            statement.setLong(7, returnDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            statement.setBoolean(8, false);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void returnMenue(ActionEvent back) throws IOException {
        Main.switchScene("/com/example/cybook/PageAccueil.fxml");
    }

    private String getClientMail(String name, String firstName) {
        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement statement = connection.prepareStatement("SELECT mail FROM Client WHERE name = ? AND firstName = ?")) {

                statement.setString(1, name);
                statement.setString(2, firstName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString("mail");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @FXML
    private void handleSearchBNF(ActionEvent event) {
        String searchQuery = searchFieldBNF.getText();
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            if (currentTask != null && currentTask.isRunning()) {
                currentTask.cancel();  // Annuler la tâche en cours
                System.out.println("Annulation de la recherche BNF en cours...");
            }

            try {
                System.out.println("Début de la recherche BNF avec le terme: " + searchQuery);
                currentTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        currentId = 1; // Reset the currentId to 1 for new search
                        resetTable();
                        searchBNF(searchQuery);
                        return null;
                    }
                };

                currentTask.setOnSucceeded(e -> {
                    System.out.println("Recherche BNF terminée");
                });

                new Thread(currentTask).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateTable(ActionEvent event) {
        masterData.clear();
        filteredData.clear();
        loadBooks(); // Recharge les livres après la recherche
        System.out.println("Table mise à jour");
    }

    private void resetTable() throws SQLException, URISyntaxException {
        System.out.println("Réinitialisation de la table Books");
        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            String dropTable = "DROP TABLE IF EXISTS Books";
            String createTable = "CREATE TABLE Books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titre TEXT," +
                    "auteur TEXT," +
                    "theme TEXT," +
                    "isbn TEXT," +
                    "editeur TEXT," +
                    "lieu TEXT," +
                    "annee TEXT)";
            statement.execute(dropTable);
            statement.execute(createTable);
            System.out.println("Table Books réinitialisée");
        }
    }

    private void searchBNF(String search) throws IOException, InterruptedException, ParserConfigurationException, SAXException, SQLException, URISyntaxException {
        String query = "bib.anywhere all \"" + search + "\"";
        String queryfinal = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String baseUri = "http://catalogue.bnf.fr/api/SRU";
        int startRecord = 1;
        int recordsPerRequest = 20;
        HttpClient client = HttpClient.newHttpClient();

        URL resource = getClass().getClassLoader().getResource("database");
        if (resource == null) {
            throw new IllegalArgumentException("Base de données non trouvée!");
        }

        File dbFile = new File(resource.toURI());
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        while (true) {
            if (currentTask.isCancelled()) {
                System.out.println("Recherche BNF annulée");
                break;
            }

            String requestUri = String.format("%s?version=1.2&operation=searchRetrieve&query=%s&startRecord=%d&maximumRecords=%d",
                    baseUri, queryfinal, startRecord, recordsPerRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUri))
                    .GET()
                    .build();

            System.out.println("Envoi de la requête à l'API BNF: " + requestUri);

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Réponse reçue de l'API BNF");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));
                doc.getDocumentElement().normalize();
                NodeList records = doc.getElementsByTagName("srw:record");
                int recordsCount = records.getLength();

                if (recordsCount == 0) {
                    System.out.println("Aucun enregistrement trouvé");
                    break;
                }

                startRecord += recordsCount;

                try (Connection connection = DriverManager.getConnection(url);
                     Statement statement = connection.createStatement()) {

                    for (int i = 0; i < recordsCount; i++) {
                        if (currentTask.isCancelled()) {
                            System.out.println("Recherche BNF annulée pendant le traitement des enregistrements");
                            break;
                        }

                        org.w3c.dom.Node node = records.item(i);
                        if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            String isbn = getElementByTag(element, "010", "a");

                            if (!isbn.equals("Non trouvé")) {
                                String title = getTitle(element).replace("'", " ");
                                String author = getAuthor(element).replace("'", " ");
                                String theme = getTheme(element).replace("'", " ");
                                String publisher = getPublisher(element).replace("'", " ");
                                String lieu = getPublicationPlace(element).replace("'", " ");
                                String annee = getPublicationYear(element).replace("'", " ");

                                String insertSQL = String.format("INSERT INTO Books (id, titre, auteur, theme, isbn, editeur, lieu, annee) VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                                        currentId++, title, author, theme, isbn, publisher, lieu, annee);
                                statement.execute(insertSQL);
                                System.out.println("Livre inséré: " + title + " par " + author);

                                // Ajouter le livre à l'ObservableList et mettre à jour l'interface utilisateur
                                Book book = new Book(currentId - 1, title, author, theme, isbn, publisher, lieu, annee);
                                javafx.application.Platform.runLater(() -> {
                                    masterData.add(book);
                                    filteredData.add(book);
                                });
                            }
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch data: HTTP Error " + response.statusCode());
                break;
            }
        }
    }

    private static String getElementByTag(Element element, String tag, String code) {
        NodeList fields = element.getElementsByTagName("mxc:datafield");
        for (int i = 0; i < fields.getLength(); i++) {
            Element field = (Element) fields.item(i);
            if (field.getAttribute("tag").equals(tag)) {
                NodeList subFields = field.getElementsByTagName("mxc:subfield");
                for (int j = 0; j < subFields.getLength(); j++) {
                    Element subField = (Element) subFields.item(j);
                    if (subField.getAttribute("code").equals(code)) {
                        return subField.getTextContent();
                    }
                }
            }
        }
        return "Non trouvé";
    }

    private static String getTitle(Element element) {
        String[] tags = {"200", "245"};
        String title = "Non trouvé";

        for (String tag : tags) {
            title = getElementByTag(element, tag, "a");
            if (!title.equals("Non trouvé")) {
                break;
            }
        }

        return title;
    }

    private static String getAuthor(Element element) {
        String[] tags = {"700", "100"};
        String author = "Non trouvé";

        for (String tag : tags) {
            author = getElementByTag(element, tag, "a");
            if (!author.equals("Non trouvé")) {
                break;
            }
        }

        return author;
    }

    private static String getTheme(Element element) {
        String[] tags = {"600", "606", "607", "610"};
        String theme = "Non trouvé";

        for (String tag : tags) {
            theme = getElementByTag(element, tag, "a");
            if (!theme.equals("Non trouvé")) {
                break;
            }
        }

        return theme;
    }

    private static String getPublisher(Element element) {
        String[] tags = {"210", "260"};
        String publisher = "Non trouvé";

        for (String tag : tags) {
            publisher = getElementByTag(element, tag, "c");
            if (!publisher.equals("Non trouvé")) {
                break;
            }
        }

        return publisher;
    }

    private static String getPublicationPlace(Element element) {
        String[] tags = {"210", "260"};
        String publicationPlace = "Non trouvé";

        for (String tag : tags) {
            publicationPlace = getElementByTag(element, tag, "a");
            if (!publicationPlace.equals("Non trouvé")) {
                break;
            }
        }

        return publicationPlace;
    }

    private static String getPublicationYear(Element element) {
        String[] tags = {"210", "260"};
        String publicationYear = "Non trouvé";

        for (String tag : tags) {
            publicationYear = getElementByTag(element, tag, "d");
            if (!publicationYear.equals("Non trouvé")) {
                break;
            }
        }

        return publicationYear;
    }
}