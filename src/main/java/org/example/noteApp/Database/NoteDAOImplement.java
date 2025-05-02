package org.example.noteapp.Database;

import org.example.noteapp.BackEnd.BackEnd;
import org.example.noteapp.Model.Note;
import org.example.noteapp.Utility.AlertUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//dup code (temp)
public class NoteDAOImplement {

    public static void addNote(Note note) {
        File directory = new File("dataset");
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        //the filename will be id + title file (to make it unique)
        String filename = "dataset/" +  note.getId() + note.getTitle() + ".txt"; // generate the filename base on the time the function is called
        System.out.println(filename);
        try (BufferedWriter data = new BufferedWriter(new FileWriter(filename))) {
            data.write(note.getContent());
            System.out.println("Report successfully saved to file: " + filename);
        } catch (IOException e) {
            AlertUtils.showError("Add Note unsuccessfully", "Fail to add new Note", "Can not add the note");
        }


    }

    public static void removeNote(Note note) {
        String filenameToDelete = "dataset/" + note.getId() + note.getTitle() + ".txt"; // current working folder

        File fileToDelete = new File(filenameToDelete);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("File '" + filenameToDelete + "' successfully deleted.");
            } else {
                System.err.println("Failed to delete file '" + filenameToDelete + "'.");
                // Consider logging the reason for failure (e.g., permissions issue)
            }
        } else {
            AlertUtils.showError("Remove Note unsuccessfully", "Fail to remove Note", "Can not remove the current Note");
        }

    }

    public static void updateNote(Note note) {
        //base on file name
        //just rewrite the whole file

        String filename = "dataset/" +note.getId() + note.getTitle() + ".txt"; // generate the filename base on the time the function is called

        try (BufferedWriter data = new BufferedWriter(new FileWriter(filename))) {
            data.write(note.getContent());
            System.out.println("Report successfully saved to file: " + filename);
        } catch (IOException e) {
            AlertUtils.showError("Update Note unsuccessfully", "Fail to update note", "Can not update the note");
        }
    }


    public static ArrayList<Note> getAllNotes() {

        ArrayList<Note> notes = new ArrayList<>();
        Path dir = Paths.get("dataset"); // it run from the root file so the path quite long(temporary storage)

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(dir, "*.txt")) {

            for (Path entry : stream) {
                String filename = entry.getFileName().toString();
                if(filename.length() < 15) {
                    continue;
                }
                String id = filename.substring(0, 14); // 14 first is the formatter (time created)
                String titleWithExtension = filename.substring(14); // Get the title with the .txt extension
                String title = titleWithExtension.substring(0, titleWithExtension.length() - 4); // Remove the .txt extension


                List<String> lines = Files.readAllLines(entry);
                System.out.println("Loaded " + entry.getFileName() +
                        " (" + lines.size() + " lines)");
                // … process lines …
                StringBuilder builder = new StringBuilder();
                for(String line : lines) {
                    builder.append(line).append("\n");
                }
                Note note = new Note(id, title, builder.toString());
                notes.add(note);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public static ArrayList<Note> getNoteByTitleAndConText(String search) {
        ArrayList<Note> notes = new ArrayList<>();
        for( Note note : BackEnd.getNotes() ) {
            if( note.getTitle().toLowerCase().contains(search.toLowerCase()) || note.getContent().toLowerCase().contains(search.toLowerCase()) ) {
                notes.add(note);
            }
        }
        return notes;
    }
}
