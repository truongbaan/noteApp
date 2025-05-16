package org.example.noteapp.Database;

import org.example.noteapp.Model.Note;

import java.util.ArrayList;
import java.util.List;

public interface NoteDAO {

    boolean addNote(String id);
    boolean removeNote(String id);
    boolean updateNote(String id);
    List<Note> getAllNotes();
    List<Note> getNoteByTitleAndConText(String title);

}
