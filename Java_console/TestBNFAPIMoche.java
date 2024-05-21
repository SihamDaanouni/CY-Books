import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class TestBNFAPIMoche {
    public static void main(String[] args) {
        try {
            String query = URLEncoder.encode("bib.title all\"MPSI\"", StandardCharsets.UTF_8.toString());

            String baseUri = "http://catalogue.bnf.fr/api/SRU";
            String requestUri = String.format("%s?version=1.2&operation=searchRetrieve&query=%s", baseUri, query);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Erreur lors de la requête à l'API SRU: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur de construction de l'URL: " + e.getMessage());
        }
    }
}
