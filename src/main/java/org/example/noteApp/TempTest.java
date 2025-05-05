package org.example.noteapp;

import org.example.noteapp.Database.NoteDAOImplement;
import org.example.noteapp.Model.Note;

import java.util.*;

public class TempTest {
    public static void main(String[] args) {
        List<Note> notes = NoteDAOImplement.getAllNotes();
        System.out.println(notes.size());

        Note note = new Note();
        System.out.println(note.toString());


    }

}
