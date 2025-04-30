package org.example.noteApp;

import org.example.noteApp.Database.NoteDAOImplement;
import org.example.noteApp.Model.Note;

import java.util.*;

public class TempTest {
    public static void main(String[] args) {
        List<Note> notes = NoteDAOImplement.getAllNotes();
        System.out.println(notes.size());

        Note note = new Note();
        System.out.println(note.toString());


    }

}
