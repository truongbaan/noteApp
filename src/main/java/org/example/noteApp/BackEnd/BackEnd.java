package org.example.noteapp.BackEnd;

import org.example.noteapp.Database.NoteDAOImplement;
import org.example.noteapp.Model.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BackEnd {//idk just think this should help do something
    private static final ArrayList<Note> notes = NoteDAOImplement.getAllNotes();;
    public static Note currentNote = new Note();
    private static BackEnd instance;

    public BackEnd() {
    }

    // Synchronized method to control access to the singleton instance
    public static synchronized BackEnd getInstance() {
        if (instance == null) {
            instance = new BackEnd();
        }
        return instance;
    }


    public static List<Note> getNotes() {
        return notes;
    }

    public static boolean addNote(Note note) {
        notes.add(note);
        NoteDAOImplement.addNote(note);
        currentNote = note;
        return true;
    }

    public static boolean removeNote(Note note) {
        // Use an iterator to safely remove items from the list while iterating
        Iterator<Note> iterator = notes.iterator();
        while (iterator.hasNext()) {
            Note note1 = iterator.next();
            if (note1.getId().equals(note.getId())) {
                iterator.remove(); // Remove the note using the iterator
                break; // Exit the loop after removing the note
            }
        }

        // Remove the note from the data access object (DAO)
        NoteDAOImplement.removeNote(note);
        currentNote = null; // Clear the current note
        return true; // Indicate success
    }

    public static boolean updateNote() {
        for(Note note1 : notes) {
            if(note1.getId().equals(currentNote.getId())) {
                note1.setTitle(currentNote.getTitle());
                note1.setContent(currentNote.getContent());
            }
        }
        NoteDAOImplement.updateNote(currentNote);
        return true;
    }

}
