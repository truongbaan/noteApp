module org.example.noteApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.noteApp to javafx.fxml;
    opens org.example.noteApp.Controller to javafx.fxml;
    exports org.example.noteApp;
}