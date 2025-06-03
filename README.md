# NoteApp

## Description
NoteApp is a note-taking application built with Java and JavaFX, designed for text editing (txt file). It mimics the simplicity of Notepad and introducing keyboard shortcuts that eliminate the need for constant mouse interaction.
It also adds a way to connect to gemini to ask for question (requires api_key, still free tho)

## Features
- Create, save, delete, and search notes
- Summarize note content
- Copy note text to clipboard
- Adjust font size on the fly
- Navigate between notes using only the keyboard

## Technologies Used
- **Programming Language:** Java 22
- **UI Framework:** JavaFX 23
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

## Installation
To run this project on your local machine, follow these steps:

1. **Install Java and JavaFX:**
    - Make sure Java 22 is installed and configured in your system.
    - Install JavaFX 23.
2. **Clone the Repository:**
    - Use Git to clone the project:
      ```bash
      git clone https://github.com/truongbaan/noteApp.git
      ```
    - Locate the Main.java file
   

3. **Build and Run the Application:**
    - Compile and launch the project using Maven:
      ```bash
      mvn clean install
      mvn javafx:run
      ```

## Usage
Once the application is running, you can use the following keyboard shortcuts: (Ctrl + shortcut_key)

| Action              | Shortcut Key |
|---------------------|--------------|
| New Note            | `T`          |
| Save Note           | `S`          |
| Delete Note         | `D`          |
| Search              | `F`          |
| Summarize Note      | `G`          |
| Increase Font Size  | `=`          |
| Decrease Font Size  | `-`          |
| Focus Note List     | `Q`          |
| Copy to Clipboard   | `B`          |
 | Switch between title and content of a specific node | `TAB`         |
No mouse? No problem, all major functions are keystrokes anyway.

## Contributing
This project was developed for personal learning and utility. Feel free to fork it and enhance it further.
