package org.example.noteapp.Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
    // Show a success alert (Information)
    public static void showSuccess(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content);
    }

    // Show an error alert
    public static void showError(String title, String header, String content) {
        showAlert(AlertType.ERROR, title, header, content);
    }

    // Show a warning alert
    public static void showWarning(String title, String header, String content) {
        showAlert(AlertType.WARNING, title, header, content);
    }

    // Helper method to create and display an alert
    private static void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
