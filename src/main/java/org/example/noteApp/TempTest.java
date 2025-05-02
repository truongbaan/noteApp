package org.example.noteapp;

import org.example.noteapp.Database.NoteDAOImplement;
import org.example.noteapp.Model.Note;

import java.util.*;

public class TempTest {
    public static void main(String[] args) {
        List<Note> notes = NoteDAOImplement.getAllNotes();
        System.out.println(notes.size());

        //test if note create correctly (too lazy to write test file with junit)
        Note note = new Note();
        System.out.println(note.toString());


    }

}
