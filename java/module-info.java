module com.example.scenesaminesiham {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.scenesaminesiham to javafx.fxml;
    exports com.example.scenesaminesiham;
}