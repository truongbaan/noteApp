package org.example.noteapp.Utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class KeyBindingManager {

    private static final String DEFAULT_KEYBINDINGS_FILE = "/keybindings.json";
    private final Map<String, KeyCodeCombination> keyBindings = new HashMap<>();

    public KeyBindingManager() {
        loadDefaultBindings();
    }
//
    public KeyBindingManager(String customBindingsPath) {
        try {
            loadBindingsFromFile(new File(customBindingsPath));
        } catch (Exception e) {
            System.err.println("Failed to load custom keybindings, falling back to defaults: " + e.getMessage());
            loadDefaultBindings();
        }
    }
//
private void loadDefaultBindings() {
    try (InputStream is = getClass().getResourceAsStream(DEFAULT_KEYBINDINGS_FILE)) {
        if (is != null) {
            System.out.println("Found keybindings.json file, loading customizations...");
            loadBindingsFromStream(is);
        } else {
            System.out.println("No keybindings.json file found, using defaults.");
            setDefaultHardcodedBindings();
        }
    } catch (IOException e) {
        System.err.println("Error loading default keybindings: " + e.getMessage());
        setDefaultHardcodedBindings();
    }
}
//
    private void setDefaultHardcodedBindings() {
        // Default key bindings if JSON file can't be loaded
        keyBindings.put("newNote", new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("saveNote", new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("deleteNote", new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("search", new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("summarize", new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("increaseFontSize", new KeyCodeCombination(KeyCode.PLUS, KeyCombination.SHORTCUT_DOWN));
        keyBindings.put("decreaseFontSize", new KeyCodeCombination(KeyCode.MINUS, KeyCombination.SHORTCUT_DOWN));
    }
//
    private void loadBindingsFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);
        parseBindings(rootNode);
    }
//
    private void loadBindingsFromStream(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);
        parseBindings(rootNode);
    }
//
    private void parseBindings(JsonNode rootNode) {
        //for multiple keys, but in this app, I just use ctrl + key only :)
        rootNode.fields().forEachRemaining(entry -> {
            String actionName = entry.getKey();
            JsonNode bindingNode = entry.getValue();

            String keyName = bindingNode.get("key").asText();
            KeyCode keyCode = KeyCode.valueOf(keyName);

            boolean isShortcutDown = bindingNode.has("shortcut") && bindingNode.get("shortcut").asBoolean();
            boolean isShiftDown = bindingNode.has("shift") && bindingNode.get("shift").asBoolean();
            boolean isAltDown = bindingNode.has("alt") && bindingNode.get("alt").asBoolean();

            KeyCodeCombination combination = new KeyCodeCombination(keyCode,
                    isShortcutDown ? KeyCombination.SHORTCUT_DOWN : KeyCombination.SHORTCUT_ANY,
                    isShiftDown ? KeyCombination.SHIFT_DOWN : KeyCombination.SHIFT_ANY,
                    isAltDown ? KeyCombination.ALT_DOWN : KeyCombination.ALT_ANY);

            keyBindings.put(actionName, combination);
        });
    }
//
//    public KeyCodeCombination getBinding(String actionName) {
//        return keyBindings.get(actionName);
//    }

    public void applyBindingsToScene(Scene scene, Map<String, Runnable> actions) {
        keyBindings.forEach((action, keyCombination) -> {
            if (actions.containsKey(action)) {
                scene.getAccelerators().put(keyCombination, actions.get(action));
            }
        });
    }
}
