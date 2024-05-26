package com.example.cybook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;

public class Main extends Application {

    private static Stage primaryStage;
    private static FXMLLoader fxmlLoader;

    /**
     * start
     * start function, set up the root
     * @param stage
     * @throws IOException
     *
     *
     */

    // start function, set up the root
    @Override
    public void start(Stage stage) throws IOException {

        // try connecting to the database, if needed generate it
        String url = "jdbc:sqlite:src/main/resources/database";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connexion à la base de données SQLite établie !");
                checkAndInitializeDatabase(conn);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données SQLite : " + e.getMessage());
        }


        primaryStage = stage;
        fxmlLoader = new FXMLLoader(Main.class.getResource("Scene2.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/com/example/cybook/logocy.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("CYBooks");

        // add the CSS file
        String css = this.getClass().getResource("Cybook.css").toExternalForm();
        scene.getStylesheets().add(css);

        // adjust the windows size by fullscreen
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * switchScene
     * the main function to switch scene
     * @param  fxml
     * @throws IOException
     *
     *
     */
    // the main function to switch scene
    public static void switchScene(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
        // Maintenir la fenêtre maximisée lorsque la scène est changée
        primaryStage.setMaximized(true);
    }

    // get the controller
    public static Object getController() {
        return fxmlLoader.getController();
    }



    // lunch the app with arguments
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * checkAndInitializeDatabase
     * verify if the database is empty, if so fill it 
     * @param conn
     * @throws SQLException
     */
    private static void checkAndInitializeDatabase(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");

        // if the database is empty, execute the SQL scripts to create tables and insert initial data
        if (!resultSet.next()) {
            executeInitializationScripts(statement);
        }
    }

    // the SQL request
    private static void executeInitializationScripts(Statement statement) throws SQLException {
        String[] scripts = {
                "CREATE TABLE IF NOT EXISTS Books (" +
                        "id INT PRIMARY KEY," +
                        "titre VARCHAR(255)," +
                        "auteur VARCHAR(255)," +
                        "theme VARCHAR(255)," +
                        "isbn VARCHAR(20)," +
                        "editeur VARCHAR(255)," +
                        "lieu VARCHAR(255)," +
                        "annee VARCHAR(50)" +
                        ");",
                "CREATE TABLE IF NOT EXISTS Borrow (" +
                        "ISBN VARCHAR(20)," +
                        "mail VARCHAR(255)," +
                        "name VARCHAR(255)," +
                        "firstname VARCHAR(255)," +
                        "title VARCHAR(255)," +
                        "timeBorrowStart TIMESTAMP," +
                        "timeBorrowEnd TIMESTAMP," +
                        "isReturn BOOLEAN," +
                        "PRIMARY KEY (ISBN, mail, timeBorrowStart)" +
                        ");",
                "CREATE TABLE IF NOT EXISTS Client (" +
                        "name VARCHAR(255)," +
                        "firstName VARCHAR(255)," +
                        "address VARCHAR(255)," +
                        "mail VARCHAR(255) PRIMARY KEY," +
                        "phone VARCHAR(50)" +
                        ");",
                "CREATE TABLE IF NOT EXISTS library (" +
                        "identifiant VARCHAR(255)," +
                        "firstname VARCHAR(255)," +
                        "address VARCHAR(255)," +
                        "mail VARCHAR(255) PRIMARY KEY," +
                        "phone VARCHAR(50)," +
                        "mot_de_passe VARCHAR(255)" +
                        ");",
                "INSERT INTO Client (name, firstName, address, mail, phone) " +
                        "VALUES ('Haution', 'Thomas', '1 rue de la Paix', 'thomas@example.com', '0123456789'), " +
                        "('Le', 'Pascal', '5 avenue des Fleurs', 'pascal@example.com', '0234567890'), " +
                        "('Provent', 'Amaury', '10 rue de la Libération', 'amaury@example.com', '0345678901'), " +
                        "('Ait moussa', 'Amine', '15 boulevard Gambetta', 'amine@example.com', '0456789012'), " +
                        "('Daanouni', 'Siham', '20 rue Victor Hugo', 'siham@example.com', '0567890123');",
                "INSERT INTO library (identifiant, firstname, address, mail, phone, mot_de_passe) " +
                        "VALUES ('Eva','Eva','Cergy','Eva@gmail.com','10101010101','0000');"
        };

        for (String script : scripts) {
            statement.executeUpdate(script);
        }
    }
}