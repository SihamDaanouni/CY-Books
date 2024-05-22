package com.example.cybooks;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Scene3Controller {

    public void switchToScene6(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybooks/Scene6.fxml");
    }

    public void switchToScene7(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybooks/Scene7.fxml");
    }

    public void switchToScene8(ActionEvent event) throws IOException {
        Main.switchScene("/com/example/cybooks/Scene8.fxml");
    }
}
