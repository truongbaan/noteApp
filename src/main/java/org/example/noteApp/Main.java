package org.example.noteapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private boolean isSmallerScreen = false; // Track the current screen state
    private final Map<String, Scene> scenes = new HashMap<>(); // Store preloaded scenes

    @Override
    public void start(Stage stage) throws IOException {
        // Preload the scenes
        preloadScenes();

        // Set the initial scene
        stage.setTitle("Hello!");
        stage.setScene(scenes.get("NoteScreen")); // Start with the larger screen

        stage.setMinHeight(350);  // Minimum height
        stage.setMaxWidth(1250);
        stage.setMinWidth(600);

        // Add listener to the stage for resizing
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1100 && !isSmallerScreen) { // Check if we need to switch to smaller screen
                stage.setScene(scenes.get("NoteSmallerScreen"));
                isSmallerScreen = true; // Update the state
            } else if (newVal.doubleValue() >= 1100 && isSmallerScreen) { // Check if we need to switch back to larger screen
                stage.setScene(scenes.get("NoteScreen"));
                isSmallerScreen = false; // Update the state
            }
        });

        stage.show();
    }

    private void preloadScenes() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/FXML/NoteScreen.fxml"));
        scenes.put("NoteScreen", new Scene(fxmlLoader.load()));

        fxmlLoader = new FXMLLoader(Main.class.getResource("/FXML/NoteSmallerScreen.fxml"));
        scenes.put("NoteSmallerScreen", new Scene(fxmlLoader.load()));
    }

    public static void main(String[] args) {
        launch();
    }
}