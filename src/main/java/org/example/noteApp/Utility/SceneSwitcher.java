package org.example.noteapp.Utility;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {
    public static void switchScene(MouseEvent mouseEvent, String fxmlFileName) {
        try {
            String filepath = "/FXML";
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(filepath + fxmlFileName));
            Pane root = loader.load();
            root.setPrefWidth(ScreenSize.width);
            root.setPrefHeight(ScreenSize.height);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error or use a logger
            throw new RuntimeException("Failed to load the scene: " + fxmlFileName, e);
        }
    }


}
