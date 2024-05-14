import java.io.File;
//import java.io.IOException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient; //Pour envoyer des requêtes HTTP
import java.net.http.HttpRequest; //Pour construire des requêtes HTTP
import java.net.http.HttpResponse; //Pour recevoir des requêtes HTTP
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;

import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder; //Créer un objet pour analyser le XML
import javax.xml.parsers.DocumentBuilderFactory; //Créer un objet qui construit un DocumentBuilder pour le XML
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class book {

    private String titre;
    private String auteur;
    private String isbn;
    private String edition;
    private String lieuPpublication;
    private String  anneePublication;

    public book ( String titre,String auteur,String isbn,String edition , String lieuPpublication , String  anneePublication){
        this.titre=titre;
        this.auteur=auteur;
        this.isbn=isbn;
        this.edition=edition;
        this.lieuPpublication=lieuPpublication;
        this.anneePublication=anneePublication;
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
        return lieuPpublication;
    }

    public void setLieuPpublication(String lieuPpublication) {
        this.lieuPpublication = lieuPpublication;
    }

    public String getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(String anneePublication) {
        this.anneePublication = anneePublication;
    }
    public void recup(){
        setTitre("relativité");
        setAuteur("eistein");

    }
    @Override
    public String toString(){
        System.out.println(getTitre()+""+getAuteur()+""+getIsbn()+""+getEdition()+""+getLieuPpublication()+""+getAnneePublication());
        return null;
    }


    public void recherche (String search) throws ParserConfigurationException, IOException, InterruptedException, SAXException, SQLException {
        search= "bib.author all /"+search+"/";

        // search="\"bib.author all \\\""+search+"\\\"\"";

        File myFile = new File("Books");
        String chemin = "jdbc:sqlite:" + myFile.getAbsolutePath();
        chemin = chemin.substring(0, chemin.length() - 5);
        chemin = chemin + "src/Books";
        // String titre;
        // String auteur;
        // String varchar;
        int j=0;

        //Ce qu'on cherche + s'assure que les caractères spéciaux sont bien formatés pour une URL
        //Lire le PDF2 au sujet des critères de recherches
        //Il faudra modifier ces deux lignes suivantes en fonction de ce qu'on veut chercher (titre, auteur, annee...)

        //METTRE EN COMMENTAIRE LES AUTRES LIGNES CAR ON A UN SEUL QUERY, DANS CE CODE JE CHERCHE PAR LE TITRE
        //AUTEUR
        String query = URLEncoder.encode("bib.author all \"Arkady Martine\"", StandardCharsets.UTF_8);
        //TITRE
        //String query = URLEncoder.encode("bib.title all \"MPSI\"", StandardCharsets.UTF_8.toString());

        //C'est l'URL de base, toutes les requêtes commence par ça, pour faire les requêtes dans l'API de la BNF
        String baseUri = "http://catalogue.bnf.fr/api/SRU";
        //On commence à compter le nombre de documents
        int startRecord = 1;
        //On affichera les documents 10 par 10
        int recordsPerRequest = 300;

        //Pour envoyer les requêtes HTTP
        HttpClient client = HttpClient.newHttpClient();

        //On continue tant qu'il reste des documents à trouver
        while (true) {
            //Création de l'URL pour faire les requêtes (voir le pdf de la BNF)
            //
            String requestUri = String.format("%s?version=1.2&operation=searchRetrieve&query=%s&startRecord=%d&maximumRecords=%d",
                    baseUri, query, startRecord, recordsPerRequest);

            //Création d'une requête HTTP GET avec l'URL de la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUri))
                    .GET()
                    .build();

            //Envoie de la requête et récupération de la réponse
            //Attention les réponse qu'on reçoit sont écrites en XML il faut les traduire dans notre langage grâce à des fonctions des bibliothèques importés
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                //Création d'une instance pour stocker les documents trouvés
                //On lit la réponse XML et on transforme cette réponse en un format plus facile comprendre et à chercher(tags...)
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                //On met les valeurs obtenu dans le document au-dessus
                Document doc = dBuilder.parse(new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));
                //Normalise le document
                doc.getDocumentElement().normalize();
                //Récupère tous les noeuds enfants du docuement ayant la balise "srw:record" chaque livre en a un
                NodeList records = doc.getElementsByTagName("srw:record");
                int recordsCount = records.getLength();

                if (recordsCount == 0) {
                    break; // No more records, exit the loop
                }

                startRecord += recordsCount;

                for (int i = 0; i < recordsCount; i++) {
                    Node node = records.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String isbn = getElementByTag(element, "010", "a");
                        //Ne prend que les documents ayant un ISBN sinon il le passe.
                        if (!isbn.equals("Non trouvé")) {
                            j+=1;

                            String title = getElementByTag(element, "200", "a");
                            title=title.replace("'"," ");
                            String author = getElementByTag(element, "700", "a");
                            author=author.replace("'"," ");
                            String publisher = getElementByTag(element, "210", "c");
                            publisher=publisher.replace("'"," ");
                            String publicationPlace = getElementByTag(element, "210", "a");
                            publicationPlace=publicationPlace.replace("'"," ");
                            String publicationYear = getElementByTag(element, "210", "d");
                            publicationYear=publicationYear.replace("'"," ");

                            System.out.print("Titre: " + title);
                            System.out.print(" Auteur: " + author);
                            System.out.print(" ISBN: " + isbn);
                            System.out.print(" Éditeur: " + publisher);
                            System.out.print(" Lieu de publication: " + publicationPlace);
                            System.out.println(" Année de publication: " + publicationYear);

                            Connection co = DriverManager.getConnection(chemin);
                            Statement stmt = co.createStatement();
                            String SQL = ("INSERT INTO Books VALUES ('"+j+"','"+title+"','"+author+"','"+isbn+"','"+publisher+"','"+publicationPlace+"','"+publicationYear+"')");
                            stmt.executeUpdate(SQL);
                            co.close();
                        }
                    }

                }
            } else {
                System.out.println("Failed to fetch data: HTTP Error " + response.statusCode());
                Connection co = DriverManager.getConnection(chemin);
                Statement stmt = co.createStatement();
                // String SQL = ("DROP table Books ");
                // stmt.executeUpdate(SQL);
                  /*  SQL = ("CREATE TABLE Books (    compteur int,\n" +
                            "                        titre VARCHAR(255),\n" +
                            "                        auteur VARCHAR(255),\n" +
                            "                        isbn VARCHAR(255),\n" +
                            "                        edition VARCHAR(255),\n" +
                            "                        lieu_publication VARCHAR(255),\n" +
                            "                        annee_publication VARCHAR(255)\n" +
                            "); ");
                    stmt.executeUpdate(SQL);
                    SQL = ("INSERT INTO Books VALUES ('"+j+"','Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'No')");


                    stmt.executeUpdate(SQL);
                    */

                co.close();

                break; // Exit the loop on HTTP errors other than 200
            }
        }


    }


    //Méthode pour extraire les données XML en un truc compréhensible
    //Un élément XML contient un tag puis un code dans le tag

    private static String getElementByTag(Element element, String tag, String code) {
        //Fonction qui cherche les tag commençant par "mxc:datafield"
        //J'ai fait ça pour que la sortie soit compréhensible
        //La sortie brut en XML est difficile à comprendre
        //LA METHODE getElementByTag est très importante pour la lisibilité
        //J'ai fait un fichier nommée TestBNFAPI.java pour voir ce que ça donne
        NodeList fields = element.getElementsByTagName("mxc:datafield");
        //On parcourt tous les éléments dans la liste
        for (int i = 0; i < fields.getLength(); i++) {
            //On sélectionne le premier arrivé
            Element field = (Element) fields.item(i);
            //On cherche la ligne avec le mot tag pour continuer, ensuite on cherche à l'intérieur de tag
            if (field.getAttribute("tag").equals(tag)) {
                //Fonction qui cherche un sous élément de tag qui se nomme mxc:subfield
                //Comment je sais que c'est ça, comme dit ci-dessus met la méthode getElementByTag en commentaire et exécute
                //Tu verras une sortie difficile à comprendre, il faut extraire les bonnes parties dans la sortie
                NodeList subFields = field.getElementsByTagName("mxc:subfield");
                //On refait une boucle dans le subfield, pour trouver le code
                for (int j = 0; j < subFields.getLength(); j++) {
                    //On prend le premier élément
                    Element subField = (Element) subFields.item(j);
                    //On vérifie qu'il y a bien le code
                    if (subField.getAttribute("code").equals(code)) {
                        return subField.getTextContent();
                    }
                }
            }
        }
        //Pour résumer il faut le tag et le code qui se trouve dans une liste qui contient des sous listes
        //Dans notre cas, on cherche que jusqu'à la 2ème sous liste
        //200a c'est pour le titre
        //700a c'est pour le nom de l'auteur
        //210c c'est le nom de l'éditeur
        //210a c'est le lieu de la publication
        //210d c'est l'année de publication
        //SI VOUS VOULEZ EN SAVOIR PLUS CHERCHER "catalogage MARC", langage utilisé pour les bibliothèques pour trier et classer les livres.
        return "Non trouvé";
    }
}




