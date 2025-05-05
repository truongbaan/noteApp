module org.example.noteapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;
    requires org.json;


    opens org.example.noteapp to javafx.fxml;
    opens org.example.noteapp.Controller to javafx.fxml;
    exports org.example.noteapp;
    opens org.example.noteapp.Utility to javafx.fxml;
}