package com.example.cybooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Scene8Controller {

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
    private int currentId = 1;

    @FXML
    public void initialize() {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colEditeur.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));

        // Charger les données
        List<Book> books = bestBooksLastMonth();
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
        tableViewBooks.setItems(observableBooks);
    }

    public List<Book> bestBooksLastMonth() {
        List<Book> books = new ArrayList<>();
        Connection co = null;
        try {
            URL resource = getClass().getClassLoader().getResource("database");
            if (resource == null) {
                throw new IllegalArgumentException("Base de données non trouvée!");
            }

            File dbFile = new File(resource.toURI());
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            co = DriverManager.getConnection(url);

            // Récupérer la date d'il y a un mois
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date oneMonthAgo = cal.getTime();

            // Requête SQL pour sélectionner les ISBN les plus empruntés durant le dernier mois
            String selectSQL = "SELECT ISBN, COUNT(*) AS borrowCount " +
                    "FROM Borrow " +
                    "WHERE timeBorrowStart >= ? " +
                    "GROUP BY isbn " +
                    "ORDER BY borrowCount DESC " +
                    "LIMIT 10"; // Sélectionne seulement les 10 premiers.

            PreparedStatement pstmt = co.prepareStatement(selectSQL);
            pstmt.setTimestamp(1, new Timestamp(oneMonthAgo.getTime()));
            ResultSet rs = pstmt.executeQuery();

            // Construire la liste des ISBN
            List<String> isbnList = new ArrayList<>();
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                isbnList.add(isbn);
            }

            // Pour chaque ISBN, appeler l'API BNF pour obtenir les détails du livre
            for (String isbn : isbnList) {
                Book book = searchBNF(isbn);
                if (book != null) {
                    books.add(book);
                }
            }

        } catch (SQLException | URISyntaxException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getMessage());
        } finally {
            // Déconnexion
            try {
                if (co != null) {
                    co.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return books;
    }

    public void returnMenue(ActionEvent back) throws IOException {
        Main.switchScene("/com/example/cybooks/Scene3.fxml");
    }

    // BNF search
    private Book searchBNF(String isbn) throws IOException, InterruptedException, ParserConfigurationException, SAXException, SQLException {
        String query = "bib.isbn any \"" + isbn + "\"";
        String queryfinal = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String baseUri = "http://catalogue.bnf.fr/api/SRU";
        String requestUri = String.format("%s?version=1.2&operation=searchRetrieve&query=%s&startRecord=1&maximumRecords=1",
                baseUri, queryfinal);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUri))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();
            NodeList records = doc.getElementsByTagName("srw:record");

            if (records.getLength() > 0) {
                Element element = (Element) records.item(0);
                String title = getTitle(element).replace("'", " ");
                String author = getAuthor(element).replace("'", " ");
                String theme = getTheme(element).replace("'", " ");
                String publisher = getPublisher(element).replace("'", " ");
                String lieu = getPublicationPlace(element).replace("'", " ");
                String annee = getPublicationYear(element).replace("'", " ");

                String insertSQL = String.format("INSERT INTO Books (id, titre, auteur, theme, isbn, editeur, lieu, annee) VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        currentId++, title, author, theme, isbn, publisher, lieu, annee);
                System.out.println("Livre inséré: " + title + " par " + author);

                return new Book(currentId - 1, title, author, theme, isbn, publisher, lieu, annee);
            }
        } else {
            System.out.println("Failed to fetch data: HTTP Error " + response.statusCode());
        }

        return null;
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
