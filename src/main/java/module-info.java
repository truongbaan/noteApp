module org.example.noteapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.noteapp to javafx.fxml;
    opens org.example.noteapp.Controller to javafx.fxml;
    exports org.example.noteapp;
}