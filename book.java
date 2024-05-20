import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class book {

    private String titre;
    private String auteur;
    private String theme;
    private String isbn;
    private String edition;
    private String lieuPublication;
    private String anneePublication;
    private boolean connexion;

    public book(String titre, String auteur, String theme, String isbn, String edition, String lieuPpublication, String anneePublication) {
        this.titre = titre;
        this.auteur = auteur;
        this.theme = theme;
        this.isbn = isbn;
        this.edition = edition;
        this.lieuPublication = lieuPpublication;
        this.anneePublication = anneePublication;
        this.connexion = false;

    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getLieuPpublication() {
        return lieuPublication;
    }

    public void setLieuPpublication(String lieuPpublication) {
        this.lieuPublication = lieuPpublication;
    }

    public String getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(String anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean getConnexion(){
        return connexion;
    }
    public void setConnexion(boolean connexion){
        this.connexion = connexion;
    }

    public void recup() {
        setTitre("relativité");
        setAuteur("einstein");
    }

    @Override
    public String toString() {
        System.out.println(getTitre() + " " + getAuteur() + " " + getTheme() + " " + getIsbn() + " " + getEdition() + " " + getLieuPpublication() + " " + getAnneePublication() + " " + getConnexion());
        return null;
    }

    public void recherche(String search) throws ParserConfigurationException, IOException, InterruptedException, SAXException, SQLException {
        File myFile = new File("database");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 8);
        chemin = chemin + "src/database";


        int j = 0;
        String query = "";
        String query1 = "";
        String query2 = "";
        int valeur2 = 0;

        while (valeur2 != 3) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrez un critère de recherche (0: anywhere, 1: author, 2: title, 3: subject, 4: isbn, 5: publisher, 6: publication place, 7: publication date): ");
            int valeur = sc.nextInt();

            while (valeur < 0 || valeur > 7) {
                sc = new Scanner(System.in);
                System.out.println("Entrez un critère de recherche (0: anywhere, 1: author, 2: title, 3: subject, 4: isbn, 5: publisher, 6: publication place, 7: publication date): ");
                valeur = sc.nextInt();
            }

            Scanner sc1 = new Scanner(System.in);
            System.out.println("Tape ton mot");
            search = sc1.nextLine();
            query1 = "(";

            switch (valeur) {
                case 0:
                    query1 += "bib.anywhere all \"" + search + "\"";
                    break;
                case 1:
                    query1 += "bib.author all \"" + search + "\"";
                    break;
                case 2:
                    query1 += "bib.title all \"" + search + "\"";
                    break;
                case 3:
                    query1 += "bib.subject all \"" + search + "\"";
                    break;
                case 4:
                    query1 += "bib.isbn all \"" + search + "\"";
                    break;
                case 5:
                    query1 += "bib.publisher all \"" + search + "\"";
                    break;
                case 6:
                    query1 += "bib.publicationplace all \"" + search + "\"";
                    break;
                case 7:
                    query1 += "bib.date all \"" + search + "\"";
                    break;
            }

            query += query1;
            System.out.println("Entrez une option, (1 pour AND et 3 pour terminer la recherche)");

            Scanner sc3 = new Scanner(System.in);
            valeur2 = sc3.nextInt();

            while (valeur2 != 1 && valeur2 != 3) {
                sc3 = new Scanner(System.in);
                valeur2 = sc3.nextInt();
            }

            query2 = (valeur2 == 1) ? ") and " : ")";
            query += query2;
            System.out.println(query);
        }

        String queryfinal = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String baseUri = "http://catalogue.bnf.fr/api/SRU";
        int startRecord = 1;
        int recordsPerRequest = 300;
        HttpClient client = HttpClient.newHttpClient();

        while (true) {
            String requestUri = String.format("%s?version=1.2&operation=searchRetrieve&query=%s&startRecord=%d&maximumRecords=%d",
                    baseUri, queryfinal, startRecord, recordsPerRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));
                doc.getDocumentElement().normalize();
                NodeList records = doc.getElementsByTagName("srw:record");
                int recordsCount = records.getLength();

                if (recordsCount == 0) {

                    break;
                }

                startRecord += recordsCount;

                for (int i = 0; i < recordsCount; i++) {
                    Node node = records.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String isbn = getElementByTag(element, "010", "a");

                        if (!isbn.equals("Non trouvé")) {
                            j++;

                            String title = getTitle(element).replace("'", " ");
                            String author = getAuthor(element).replace("'", " ");
                            String theme = getTheme(element).replace("'", " ");
                            String publisher = getPublisher(element).replace("'", " ");
                            String publicationPlace = getPublicationPlace(element).replace("'", " ");
                            String publicationYear = getPublicationYear(element).replace("'", " ");

                            System.out.print("Titre: " + title);
                            System.out.print(" Auteur: " + author);
                            System.out.print(" Thème: " + theme);
                            System.out.print(" ISBN: " + isbn);
                            System.out.print(" Éditeur: " + publisher);
                            System.out.print(" Lieu de publication: " + publicationPlace);
                            System.out.println(" Année de publication: " + publicationYear);

                            Connection co = DriverManager.getConnection(chemin);
                            Statement stmt = co.createStatement();
                            String SQL = ("INSERT INTO Books VALUES ('" + j + "','" + title + "','" + author + "','" + theme + "','" + isbn + "','" + publisher + "','" + publicationPlace + "','" + publicationYear + "')");
                            stmt.executeUpdate(SQL);
                            co.close();
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch data: HTTP Error " + response.statusCode());
                Connection co = DriverManager.getConnection(chemin);
                Statement stmt = co.createStatement();
                co.close();
                //Modif
                setConnexion(true);
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
        // Essayez plusieurs tags pour trouver le titre
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
        // Essayez plusieurs tags pour trouver l'auteur
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
        // Essayez plusieurs tags pour trouver le thème
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
        // Essayez plusieurs tags pour trouver l'éditeur
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
        // Essayez plusieurs tags pour trouver le lieu de publication
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
        // Essayez plusieurs tags pour trouver l'année de publication
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
    public void reset() throws SQLException {


        File myFile = new File("database");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 8);
        chemin = chemin + "src/database";
        int j = 0;

        Connection co = DriverManager.getConnection(chemin);
        Statement stmt = co.createStatement();
        String SQL = ("DROP table Books ");
        stmt.executeUpdate(SQL);
        SQL = ("CREATE TABLE Books (    compteur int,\n" +
                "                        titre VARCHAR(255),\n" +
                "                        auteur VARCHAR(255),\n" +
                "                        theme VARCHAR(255),\n" +
                "                        isbn VARCHAR(255),\n" +
                "                        edition VARCHAR(255),\n" +
                "                        lieu_publication VARCHAR(255),\n" +
                "                        annee_publication VARCHAR(255)\n" +
                "); ");
        stmt.executeUpdate(SQL);
        SQL = ("INSERT INTO Books VALUES ('" + j + "','Cardinal', 'Tom B. Erichsen','Theme', 'Skagen 21', 'Stavanger', '4006', 'No')");


        stmt.executeUpdate(SQL);


        co.close();
}
}
