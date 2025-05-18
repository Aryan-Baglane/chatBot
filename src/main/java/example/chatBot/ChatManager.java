package example.chatBot;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private final List<ChatMessage> history = new ArrayList<>();

    public void addUserMessage(String message){
        history.add(new ChatMessage("user",message));
    }

    public void addBotMessage(String message){
        history.add(new ChatMessage("model",message));
    }

    public List<ChatMessage> getHistory(){
        return history;
    }

    public void reset(){
        history.clear();
    }
}

