package org.example.noteapp.Database;

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

    public static boolean addNote(Note note) {
        File directory = new File("dataset");
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        //the filename will be id + title file (to make it unique)
        String filename = "dataset/" +  note.getId() + note.getTitle() + ".txt"; // generate the filename base on the time the function is called
        System.out.println(filename);
        System.out.println(directory.getAbsolutePath());
        try (BufferedWriter data = new BufferedWriter(new FileWriter(filename))) {
            data.write(note.getContent());
            System.out.println("Report successfully saved to file: " + filename);
            return true;
        } catch (IOException e) {
            AlertUtils.showError("Add Note unsuccessfully", "Fail to add new Note", "Can not add the note");
        }
        return false;


    }

    public static boolean removeNote(Note note) {
        String filenameToDelete = "dataset/" + note.getId() + note.getTitle() + ".txt"; // current working folder

        File fileToDelete = new File(filenameToDelete);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("File '" + filenameToDelete + "' successfully deleted.");
                return true;
            } else {
                System.err.println("Failed to delete file '" + filenameToDelete + "'.");
            }
        } else {
            AlertUtils.showError("Remove Note unsuccessfully", "Fail to remove Note", "Can not remove the current Note");
        }
        return false;

    }

    public static boolean updateNote(Note note) {
        File folder = new File("dataset");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 1) Delete any existing file(s) for this note ID
        File[] olds = folder.listFiles((dir, name) -> name.startsWith(note.getId()));
        if (olds != null) {
            for (File oldFile : olds) {
                if (!oldFile.delete()) {
                    System.err.println("Could not delete old note file: " + oldFile);
                }
            }
        }


        //just rewrite the whole file
        String filename = "dataset/" +note.getId() + note.getTitle() + ".txt"; // generate the filename base on the time the function is called

        try (BufferedWriter data = new BufferedWriter(new FileWriter(filename))) {
            data.write(note.getContent());
            System.out.println("Report successfully saved to file: " + filename);
            return true;
        } catch (IOException e) {
            AlertUtils.showError("Update Note unsuccessfully", "Fail to update note", "Can not update the note");
        }
        return false;
    }


    public static ArrayList<Note> getAllNotes() {

        ArrayList<Note> notes = new ArrayList<>();
        Path dir = Paths.get("dataset");

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
}
