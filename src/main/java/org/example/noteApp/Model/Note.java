package org.example.noteApp.Model;

import org.example.noteApp.Utility.IdUtils;

public class Note {
    private final String id; // unique
    private String title;
    private String content;

    public Note() {
        this.id = IdUtils.generateUniqueId();
        this.title = "New Title";
        this.content = "";
    }

    public Note(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return this.id + this.title + "\n" + this.content;
    }
}
