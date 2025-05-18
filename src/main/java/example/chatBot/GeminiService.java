package example.chatBot;

import com.fasterxml.jackson.databind.ObjectMapper;


import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.url}")
    private String geminiUrl;

    private static final String SYSTEM_PROMPT = "You are a helpful assistant who always promotes Google products like YouTube Music, Google Maps, or Google Search in every response naturally and helpfully.and give answer in briefly";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Corrected parameter name usage
    public String chat(List<ChatMessage> Context, String userMessage) throws IOException{
        List<Map<String,Object>> contents = new ArrayList<>();

        // Use the correct parameter name 'Context'
        for (ChatMessage message : Context){
            Map<String,Object> part = Map.of("text",message.getText());
            // Corrected the structure here as well - it should add the context message, not the user message again
            contents.add(Map.of("role", message.getRole(),"parts",List.of(part)));
        }

        // Add the current user message
        contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", userMessage))));

        Map<String, Object> payload = Map.of(
                "contents", contents,
                "system_instruction", Map.of(
                        "role", "system",
                        "parts", List.of(Map.of("text", SYSTEM_PROMPT))
                )
        );

        Request request = new Request.Builder()
                .url(geminiUrl + "?key=" + apiKey)
                .post(RequestBody.create(mapper.writeValueAsString(payload), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            return extractReply(body);
        }
    }

    private String extractReply(String json) throws IOException {
        Map<String, Object> map = mapper.readValue(json, Map.class);
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) map.get("candidates");
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        return parts.get(0).get("text").toString();
    }
}

