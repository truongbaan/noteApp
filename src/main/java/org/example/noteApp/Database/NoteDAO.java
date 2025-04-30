package org.example.noteApp.Database;

import org.example.noteApp.Model.Note;

import java.util.List;

public interface NoteDAO {

    boolean addNote(String id);
    boolean removeNote(String id);
    boolean updateNote(String id);
    List<Note> getAllNotes();
    List<Note> getNoteByTitleAndConText(String search);

}
