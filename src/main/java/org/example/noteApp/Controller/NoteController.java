package org.example.noteapp.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import org.example.noteapp.BackEnd.BackEnd;
import org.example.noteapp.Model.Note;
import org.example.noteapp.Utility.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class NoteController {

    @FXML
    private ListView<Note> noteList;

    @FXML
    private TextField noteTitle;

    @FXML
    private TextArea noteContent;

    @FXML
    private TextField SearchField;

    @FXML
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        // 1) Ctrl+T (or ⌘+T on macOS) → new document
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;

            // 1) Ctrl+T → createNewNote()
            KeyCombination newCombo = new KeyCodeCombination(
                    KeyCode.T,
                    KeyCombination.SHORTCUT_DOWN
            );
            newScene.getAccelerators().put(newCombo, this::createNewNote);

            // 2) Ctrl+S → saveCurrentNote()
            KeyCombination saveCombo = new KeyCodeCombination(
                    KeyCode.S,
                    KeyCombination.SHORTCUT_DOWN
            );
            newScene.getAccelerators().put(saveCombo, this::saveCurrentNote);
        });


        // Load notes from the backend
        refreshNoteList();
        createNewNote();

        // Show only the title in each cell
        noteList.setCellFactory(lv -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                    setStyle(""); // Reset style
                } else {
                    setText(note.getTitle());
                    if (note.equals(noteList.getSelectionModel().getSelectedItem())) {
                        setStyle("-fx-background-color: lightblue;"); // Highlight selected cell
                    } else {
                        setStyle(""); // Reset style for unselected cells
                    }
                }
            }
        });

        // Add listener for selection changes
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                BackEnd.currentNote = newValue; // Update current note
                refreshText();
                noteList.refresh(); // Refresh the ListView to apply styles
            }
        });
    }

    @FXML
    public void createNewNote(){
        saveCurrentNote();//save the current
        //then create the new Note
        BackEnd.currentNote = new Note();
        BackEnd.addNote(BackEnd.currentNote);
        refreshText();
        refreshNoteList();
    }

    @FXML
    public void saveCurrentNote() {
        if(BackEnd.currentNote == null) {
            return;
        }
        if(noteTitle.getText() == null || noteContent.getText() == null) {
            AlertUtils.showError("Eror", "Something is missing", "Your current note doesnt have title or content");
        }
        BackEnd.currentNote.setTitle(noteTitle.getText());
        BackEnd.currentNote.setContent(noteContent.getText());
        BackEnd.updateNote();
        refreshNoteList();

    }

    @FXML
    public void deleteNote() {
        Note removeNote = BackEnd.currentNote;
        if(removeNote != null) {
            BackEnd.removeNote(removeNote);
            refreshNoteList(); // Refresh the ListView after deletion
        }
    }

    @FXML
    public void askGeminiToSummarize(){
        //call API and provide the context of the Note for gemini to summarize
        String prompt = "Could you please summarize, enhance the note and predict what we should do in the future?";

        //thoughts: copy to clipboard or create a pdf file of gemini answer?
    }

    @FXML
    public void increaseFont(){

    }

    @FXML
    public void decreaseFont(){

    }

    @FXML
    public void search(){
        String text = SearchField.getText().trim();
        if(text.isEmpty()) {
            refreshNoteList();
            return;
        }
        List<Note> notes = new ArrayList<>();
        for(Note note : BackEnd.getNotes()){
            if(note.getTitle().toLowerCase().contains(text.toLowerCase())
                    || note.getContent().toLowerCase().contains(text.toLowerCase())) {
                notes.add(note);
            }
        }
        ObservableList<Note> items = FXCollections.observableArrayList(notes);
        noteList.setItems(items); // Update the ListView with the new list
    }

    public void refreshNoteList() {
        List<Note> notes = BackEnd.getNotes(); // Get the updated list of notes from the backend
        ObservableList<Note> items = FXCollections.observableArrayList(notes);
        noteList.setItems(items); // Update the ListView with the new list
    }

    private void refreshText(){
        if(BackEnd.currentNote == null) {
            noteTitle.setPromptText("No current note detected, please create a new one");
            noteContent.setPromptText("No current note detected, please create a new one");
            return;
        }
        noteTitle.setText(BackEnd.currentNote.getTitle());
        noteContent.setText(BackEnd.currentNote.getContent());
    }
}
