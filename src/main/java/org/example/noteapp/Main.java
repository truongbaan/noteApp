package org.example.noteapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the single FXML screen
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/FXML/NoteScreen.fxml"));
        Parent root = fxmlLoader.load();

        // Create and set the scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Note Application");

        // Configure window sizing
        stage.setMinHeight(350);      // Minimum height
        stage.setMinWidth(1200);       // Minimum width
        stage.setMaxWidth(1250);      // Maximum width

        // Show the stage
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}