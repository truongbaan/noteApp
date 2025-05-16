package org.example.noteapp.BackEnd;

import org.example.noteapp.Database.NoteDAOImplement;
import org.example.noteapp.Model.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BackEnd {//idk just think this should help do something
    private static final ArrayList<Note> notes = NoteDAOImplement.getAllNotes();;
    public static Note currentNote = null;
    private static BackEnd instance;

    public BackEnd() {
    }

    // Synchronized method to control access to the singleton instance
    //remove later since all var is static
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
        if(NoteDAOImplement.addNote(note)){
            notes.add(note);
            currentNote = note;
            return true;
        }
        return false;
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
        if(NoteDAOImplement.removeNote(note)){
            currentNote = null; // Clear the current note
            return true; // Indicate success
        }
        return false;
    }

    public static boolean updateNote() {
        for(Note note1 : notes) {
            if(note1.getId().equals(currentNote.getId())) {
                note1.setTitle(currentNote.getTitle());
                note1.setContent(currentNote.getContent());
            }
        }
        return NoteDAOImplement.updateNote(currentNote);
    }

}
