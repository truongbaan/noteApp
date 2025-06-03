package org.example.noteapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.noteapp.Controller.NoteController;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/FXML/NoteScreen.fxml"));
        Parent root = fxmlLoader.load();
        NoteController noteController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Note Application");

        //size limit
        stage.setMinHeight(350);      // min height
        stage.setMinWidth(1200);     // min width
        stage.setMaxWidth(1250);      // max width

        // auto save when close
        stage.setOnCloseRequest(event -> {
            noteController.saveCurrentNote();
        });


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
