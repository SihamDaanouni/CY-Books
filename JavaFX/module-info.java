module com.example.cybooks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Ajout pour JDBC
    requires mysql.connector.java; // Ajout pour le connecteur MySQL
    requires java.net.http;
    //requires sqlite.jdbc; // Ajout pour le connecteur SQLite

    opens com.example.cybook to javafx.fxml;
    exports com.example.cybook;
}