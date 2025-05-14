package org.example.noteapp.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.example.noteapp.BackEnd.BackEnd;
import org.example.noteapp.Model.Note;
import org.example.noteapp.Utility.AlertUtils;
import org.example.noteapp.Utility.KeyBindingManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private KeyBindingManager keyBindingManager;

    // Font size management
    private double currentFontSize = 12.0; // Default font size
    private static final double FONT_SIZE_INCREMENT = 2.0;
    private static final double MIN_FONT_SIZE = 8.0;
    private static final double MAX_FONT_SIZE = 24.0;

    @FXML
    public void initialize() {
        createNewNote();
        // Initialize the key binding manager
        keyBindingManager = new KeyBindingManager();

        // Initialize the ListView and add selection listener
        setupNoteListView();

        // Setup key bindings when scene is available
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupKeyBindings(newScene);

                // Add keyboard handler for global keyboard navigation
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleGlobalKeyEvents);
            }
        });

        // Load notes from the backend
        refreshNoteList();

        // Setup search field
        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            search();
        });

        // Setup custom tab traversal between title and content only
        setupCustomTabTraversal();
    }

    private void setupCustomTabTraversal() {
        // Override default tab behavior for noteTitle
        noteTitle.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_QUOTE) { // using " ` "
                event.consume();
                noteContent.requestFocus();
            }
        });

        // Override default tab behavior for noteContent
        noteContent.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_QUOTE) {
                event.consume();
                noteTitle.requestFocus();
            }
        });
    }

    private void handleGlobalKeyEvents(KeyEvent event) {
        // Handle Ctrl+Q to focus on the ListView
        if (event.getCode() == KeyCode.Q && event.isControlDown()) {
            event.consume();
            focusOnCurrentNoteInList();
        }

        // Handle arrow keys when ListView is focused
        if (noteList.isFocused()) {
            if (event.getCode() == KeyCode.ENTER) {
                // When pressing Enter on a selected item, move focus to the title field
                event.consume();
                noteTitle.requestFocus();
            }
        }
    }

    private void focusOnCurrentNoteInList() {
        // First, ensure the current note is selected in the list
        if (BackEnd.currentNote != null) {
            int index = noteList.getItems().indexOf(BackEnd.currentNote);
            if (index >= 0) {
                noteList.getSelectionModel().select(index);
                // Ensure the selected item is visible
                noteList.scrollTo(index);
            }
        }

        // Give focus to the list
        noteList.requestFocus();
    }

    private void setupNoteListView() {
        // Custom cell factory with proper selection highlighting
        noteList.setCellFactory(lv -> new ListCell<Note>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);

                if (empty || note == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(note.getTitle());

                    // Check if this note is the currently selected one in the backend
                    boolean isCurrentNote = (BackEnd.currentNote != null &&
                            BackEnd.currentNote.equals(note));

                    // Style based on selection status
                    if (isSelected() || isCurrentNote) {
                        setStyle("-fx-background-color: lightblue; -fx-text-fill: black;");
                    } else {
                        setStyle(""); // Reset style for unselected cells
                    }
                }
            }
        });

        // Add listener for selection changes
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Save current note before switching
                if (oldValue != null && BackEnd.currentNote != null) {
                    saveCurrentNoteContent();
                }

                // Set new current note
                BackEnd.currentNote = newValue;
                refreshText();
                noteList.refresh(); // Refresh to update highlights
            }
        });

        // Add key event handler for ListView navigation
        noteList.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // When Enter is pressed, switch focus to the note title
                event.consume();
                noteTitle.requestFocus();
            }
        });
    }

    private void setupKeyBindings(javafx.scene.Scene scene) {
        // Map actions to their handler methods
        Map<String, Runnable> actionMap = new HashMap<>();
        actionMap.put("newNote", this::createNewNote);
        actionMap.put("saveNote", this::saveCurrentNote);
        actionMap.put("deleteNote", this::deleteNote);
        actionMap.put("search", () -> SearchField.requestFocus());
        actionMap.put("summarize", this::askGeminiToSummarize);
        actionMap.put("increaseFontSize", this::increaseFont);
        actionMap.put("decreaseFontSize", this::decreaseFont);
        actionMap.put("focusNoteList", this::focusOnCurrentNoteInList); // Add Ctrl+Q binding

        // Apply key bindings to the scene
        keyBindingManager.applyBindingsToScene(scene, actionMap);
    }

    @FXML
    public void createNewNote() {
        Note cn = BackEnd.currentNote;
        if (cn != null && !cn.getContent().isEmpty() && !cn.getTitle().isEmpty()) {
            saveCurrentNote();
        }

        // Create new note
        BackEnd.currentNote = new Note();
        BackEnd.addNote(BackEnd.currentNote);
        refreshText();
        refreshNoteList();

        // Set focus to title field for immediate editing
        noteTitle.requestFocus();
    }

    @FXML
    public void saveCurrentNote() {
        if (BackEnd.currentNote == null) {
            return;
        }

        saveCurrentNoteContent();
        refreshNoteList();

    }

    private void saveCurrentNoteContent() {
        if (BackEnd.currentNote == null) return;

        // Skip validation for empty notes to allow for drafts
        BackEnd.currentNote.setTitle(noteTitle.getText().isEmpty() ? "Untitled" : noteTitle.getText());
        BackEnd.currentNote.setContent(noteContent.getText());

        BackEnd.updateNote();
    }


    @FXML
    public void deleteNote() {
        Note removeNote = BackEnd.currentNote;
        if (removeNote != null) {
            // Confirm deletion

            BackEnd.removeNote(removeNote);
            BackEnd.currentNote = null;
            refreshNoteList();
            refreshText();

            // Select another note if available
            if (!noteList.getItems().isEmpty()) {
                noteList.getSelectionModel().select(0);

            }
        }
    }

    @FXML
    public void askGeminiToSummarize() {
        if (BackEnd.currentNote == null || noteContent.getText().isEmpty()) {
            AlertUtils.showWarning("Cannot Summarize", "No content to summarize",
                    "Please create or select a note with content first.");
            return;
        }

        String prompt = "Could you please summarize, enhance the note and predict what we should do in the future?";
        String content = noteContent.getText();

        // TODO: Implement Gemini API integration

    }

    @FXML
    public void increaseFont() {
        if (currentFontSize < MAX_FONT_SIZE) {
            currentFontSize += FONT_SIZE_INCREMENT;
            updateFontSize();
        }
    }

    @FXML
    public void decreaseFont() {
        if (currentFontSize > MIN_FONT_SIZE) {
            currentFontSize -= FONT_SIZE_INCREMENT;
            updateFontSize();
        }
    }

    private void updateFontSize() {
        // Apply font size to note content and title
        String fontStyle = String.format("-fx-font-size: %.1fpx;", currentFontSize);
        noteContent.setStyle(fontStyle);
        noteTitle.setStyle(fontStyle);
    }

    @FXML
    public void search() {
        // Just update the ListView items
        String text = SearchField.getText().trim();
        if (text.isEmpty()) {
            refreshNoteList();
            return;
        }

        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : BackEnd.getNotes()) {
            if (note.getTitle().toLowerCase().contains(text.toLowerCase())
                    || note.getContent().toLowerCase().contains(text.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        ObservableList<Note> items = FXCollections.observableArrayList(filteredNotes);
        noteList.setItems(items);


    }

    public void refreshNoteList() {
        // Save current selection
        Note selectedNote = noteList.getSelectionModel().getSelectedItem();

        // Update list
        List<Note> notes = BackEnd.getNotes();
        ObservableList<Note> items = FXCollections.observableArrayList(notes);
        noteList.setItems(items);

        // Restore selection if possible, or select the current note
        if (selectedNote != null && items.contains(selectedNote)) {
            noteList.getSelectionModel().select(selectedNote);
        } else if (BackEnd.currentNote != null && items.contains(BackEnd.currentNote)) {
            noteList.getSelectionModel().select(BackEnd.currentNote);
        }

        // Refresh to update cell styling
        noteList.refresh();
    }

    private void refreshText() {
        if (BackEnd.currentNote == null) {
            noteTitle.setText("");
            noteContent.setText("");
            noteTitle.setPromptText("No note selected");
            noteContent.setPromptText("Create or select a note to begin");
            return;
        }

        noteTitle.setText(BackEnd.currentNote.getTitle());
        noteContent.setText(BackEnd.currentNote.getContent());

        // Clear prompt text when we have a note
        noteTitle.setPromptText("Title");
        noteContent.setPromptText("Content");
    }
}