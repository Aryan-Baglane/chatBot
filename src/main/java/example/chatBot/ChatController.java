package example.chatBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList; // Import ArrayList if ChatMessage is used directly in request/response

// Import necessary classes for request body if you define a separate class
// import example.ChatBot.ChatRequest; // Example if you create a ChatRequest class

// Change @Component to @RestController to expose this class as a REST controller
@RestController
@RequestMapping("/api/chat") // Base path for all endpoints in this controller
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    // In a real application, you might manage chat sessions per user
    // For simplicity here, we'll use a single ChatManager instance.
    // Consider how you would handle multiple concurrent users and their chat histories.
    private final ChatManager chatManager = new ChatManager();

    // Define a simple class for the chat request body
    // This allows you to send the user message and potentially context in the POST request
    public static class ChatRequest {
        private String userMessage;
        // You could add a field here for conversation history if needed,
        // e.g., private List<ChatMessage> history;

        // Getters and setters are required for Spring to bind the request body
        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }
    }


    @GetMapping
    public String welcomeMessage() {
        return "👋 Welcome to the Gemini Chatbot API!";
    }


    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        String userMessage = request.getUserMessage();

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("User message cannot be empty.");
        }

        // In a real application, you'd manage history per session/user.
        // For this example, we'll add to the single chatManager instance's history.
        chatManager.addUserMessage(userMessage);

        try {
            // Get the current history from the chat manager to send to the Gemini service
            List<ChatMessage> currentHistory = new ArrayList<>(chatManager.getHistory()); // Create a mutable copy if getHistory returns immutable

            String reply = geminiService.chat(currentHistory, userMessage);

            // Add the bot's reply to the history as well
            chatManager.addBotMessage(reply);

            // Return the bot's reply in the response body
            return ResponseEntity.ok(reply);

        } catch (Exception e) {
            // Log the error on the server side
            System.err.println("Error during chat processing: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging

            // Return an internal server error status and message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing your request: " + e.getMessage());
        }
    }

    // The previous 'run' method for CLI is no longer needed in a web controller
    // If you need both CLI and Web, consider separating them into different classes
}

