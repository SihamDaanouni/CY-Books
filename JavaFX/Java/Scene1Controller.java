package com.example.cybook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class Scene1Controller {

    @FXML
    public void switchToConnexion(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybook/Scene2.fxml");
    }
}