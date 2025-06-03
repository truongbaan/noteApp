package org.example.noteapp.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.example.noteapp.BackEnd.BackEnd;
import org.example.noteapp.Utility.AlertUtils;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class GeminiClient {

    private static String apiKey;
    private static final String GEMINI_API_URL_BASE = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final String GEMINI_MODEL = "gemini-2.0-flash-lite"; // Use the model name from the API response
    private static final String GEMINI_API_URL = GEMINI_API_URL_BASE + GEMINI_MODEL + ":generateContent";
    private static final String API_KEY = Dotenv.load().get("GEMINI_API_KEY");
    private static GeminiClient instance;

    public static synchronized GeminiClient getInstance() {
        if (instance == null) {
            instance = new GeminiClient();
        }
        return instance;
    }

    public GeminiClient(){
        initialize(API_KEY);
    }

    public static void initialize(String apiKey) {
        GeminiClient.apiKey = apiKey;
    }

    public static String summarizeText(String textToSummarize) {
        if (apiKey == null) {
            System.err.println("API key is not initialized.");
            AlertUtils.showError("API key not found", "API key not found", "Please check your .env file in the same folder of this app, and check if the file contain GEMINI_API_KEY value");
            return null;
        }

        try {
            // Construct the JSON request body.
            JSONObject content = new JSONObject();
            content.put("text", "Could you please summarize this: " + textToSummarize); // Only include the "text" field

            // Create the prompt object
            JSONObject prompt = new JSONObject();
            prompt.put("parts", new org.json.JSONArray().put(content));

            // Create the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", prompt);

            // Create the generation config
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 0.2);
            generationConfig.put("maxOutputTokens", 800);

            requestBody.put("generationConfig", generationConfig);

            // Convert the JSON object to a string.
            String jsonPayload = requestBody.toString();

            // Print for debugging
            System.out.println("Request Payload: " + jsonPayload);
            System.out.println("Request URL: " + GEMINI_API_URL + "?key=" + apiKey);

            // Create the URL object.
            String apiUrl = GEMINI_API_URL + "?key=" + apiKey.trim(); // Ensure no extra spaces or quotes
            URL url = new URL(apiUrl);

            // Open a connection to the API endpoint.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //time out increase
            connection.setConnectTimeout(30000); // 30 seconds for connecting
            connection.setReadTimeout(60000);    // 60 seconds for reading data

            // Set the request method to POST.
            connection.setRequestMethod("POST");

            // Set the request headers.
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable output for the request body.
            connection.setDoOutput(true);

            // Write the JSON payload to the request body.
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(jsonPayload);
                writer.flush();
            }

            // Read the response from the API.
            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            } catch (IOException e) {
                // Read error response if available
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.err.println("Error Response: " + errorResponse);
                }
            }

            String responseString = responseBuilder.toString();
            System.out.println("Full Response: " + responseString); // Print the full response

            // Parse the JSON response.
            JSONObject jsonResponse = new JSONObject(responseString);

            // Extract the summary from the response.
            String summary = processGeminiResponse(jsonResponse);
            connection.disconnect();
            return summary;

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String processGeminiResponse(JSONObject jsonResponse) {
        try {
            if (jsonResponse.has("error")) {
                JSONObject error = jsonResponse.getJSONObject("error");
                return "Error from Gemini API: " + error.getString("message");
            }

            org.json.JSONArray candidates = jsonResponse.getJSONArray("candidates");
            if (!candidates.isEmpty()) {
                JSONObject candidate = candidates.getJSONObject(0);
                JSONObject content = candidate.getJSONObject("content");
                org.json.JSONArray parts = content.getJSONArray("parts");
                if (!parts.isEmpty()) {
                    JSONObject part = parts.getJSONObject(0);
                    return part.getString("text");
                }
            }
            return "No summary found in the response.";
        } catch (org.json.JSONException e) {
            return "Error parsing JSON response: " + e.getMessage();
        }
    }




    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();
        // Replace with your actual Gemini API key.
        String textToSummarize =
                "Remote Desktop and Input Controller via TCP\n" +
                "Description\n" +
                "This project allows a user to remotely control another computer on the same Wi-Fi network via real-time screen streaming and keyboard input over TCP. It consists of two main components:\n" +
                "\n" +
                "Host (host4.py): Captures and streams the host machine's screen to connected clients and processes remote keyboard inputs using pyautogui-like behavior.\n" +
                "Client (client4.py): Connects to the host, displays the real-time stream, and sends keyboard input to the host for remote control.\n" +
                "Note: This system is designed to be used on different devices connected to the same local Wi-Fi network. Running both the host and client on the same machine may result in keyboard input conflicts.\n" +
                "\n" +
                "Features\n" +
                "Real-time Screen Streaming: Host captures and sends screen frames at high speed using mss and cv2.\n" +
                "Remote Keyboard Control: Client captures keypresses and sends them in real-time to the host.\n" +
                "Hotkey Input Toggle: The client can toggle input transmission using the backtick key (`).\n" +
                "Multi-threaded Execution: Separate threads manage screen transmission and keyboard input for smoother operation.\n" +
                "Graceful Exit: Pressing ESC on either side initiates a safe disconnection or server shutdown.\n" +
                "Hotkey & Modifier Support: Host handles complex key combinations like Ctrl+Shift+Key from the client.\n" +
                "Technologies Used\n" +
                "Python Version: 3.12.6\n" +
                "Libraries:\n" +
                "socket - TCP communication\n" +
                "threading - concurrent operations\n" +
                "keyboard - input capture and simulation\n" +
                "pyautogui (or similar via keyboard) - key simulation\n" +
                "mss - fast screen capturing\n" +
                "cv2 - screen display\n" +
                "pickle + struct - data serialization\n" +
                "numpy - image data manipulation\n" +
                "Installation\n" +
                "Clone the Repository\n" +
                "\n" +
                "git clone <your-repo-link>\n" +
                "cd <your-repo-name>\n" +
                "Install Python\n" +
                "\n" +
                "Download from python.org.\n" +
                "Install Required Libraries\n" +
                "\n" +
                "pip install opencv-python pyautogui keyboard mss numpy\n" +
                "(Additional setup may be required depending on your OS permissions for keyboard and pyautogui.)\n" +
                "\n" +
                "Usage\n" +
                "Run the Host (on the computer to be controlled)\n" +
                "python host4.py\n" +
                "Wait for the host to display the IP address and start listening.\n" +
                "The host will stream its screen and accept remote input.\n" +
                "Press ESC to shut down the server.\n" +
                "Run the Client (on a different computer on the same network)\n" +
                "python client4.py\n" +
                "When prompted, enter the IPv4 address of the host machine.\n" +
                "A window will display the host's screen in real-time.\n" +
                "Press the backtick (`) key to toggle sending keyboard inputs.\n" +
                "Press ESC to disconnect from the host and exit.\n" +
                "Reminder: This only works on devices connected to the same local Wi-Fi network.\n" +
                "\n" +
                "License\n" +
                "This project is licensed under the MIT License. See the LICENSE file for more information.";

        // Initialize the API key
        initialize(API_KEY);

        // Call the summarization function.
        String summary = summarizeText(textToSummarize);

        if (summary != null) {
            System.out.println("Summary:\n" + summary);
        } else {
            System.out.println("Failed to get summary.");
        }

        long end_time = System.currentTimeMillis();
        System.out.println("Time usage: " + (end_time - start_time));

    }
}

