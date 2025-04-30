package org.example.noteApp.BackEnd;

import org.example.noteApp.Database.NoteDAOImplement;
import org.example.noteApp.Model.Note;

import java.util.ArrayList;
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
        return true;
    }

    public static boolean removeNote(Note note) {

        return true; // Indicate success
    }

    public static boolean updateNote() {

        return true;
    }

}
