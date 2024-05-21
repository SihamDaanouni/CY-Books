package com.example.cybook;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class databaseUtils {
    public static List<Book> bestBooksLastMonth() {
        List<Book> books = new ArrayList<>();
        Connection co = null;
        try {
            URL resource = databaseUtils.class.getClassLoader().getResource("database");
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
            String selectSQL = "SELECT isbn, COUNT(*) AS borrowCount " +
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
                String isbn = rs.getString("isbn");
                isbnList.add(isbn);
            }

            // Requête SQL pour obtenir les détails des livres à partir des ISBN
            if (!isbnList.isEmpty()) {
                String placeholders = String.join(",", isbnList).replaceAll("[^,]", "?");
                String bookDetailsSQL = "SELECT * FROM Books WHERE isbn IN (" + placeholders + ")";
                pstmt = co.prepareStatement(bookDetailsSQL);

                for (int i = 0; i < isbnList.size(); i++) {
                    pstmt.setString(i + 1, isbnList.get(i));
                }

                rs = pstmt.executeQuery();

                // Ajouter les livres à la liste
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("titre");
                    String author = rs.getString("auteur");
                    String theme = rs.getString("theme");
                    String isbn = rs.getString("isbn");
                    String publisher = rs.getString("editeur");
                    String location = rs.getString("lieu");
                    String year = rs.getString("annee");
                    books.add(new Book(id, title, author, theme, isbn, publisher, location, year));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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
}

