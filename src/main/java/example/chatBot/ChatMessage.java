package example.chatBot;

public class ChatMessage {
    private String role;
    private String text;

    public ChatMessage(String role, String text){
        this.role = role;
        this.text = text;
    }

    public String getRole(){
        return role;
    }
    public String getText(){
        return text;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setText(String text) {
        this.text = text;
    }
}
