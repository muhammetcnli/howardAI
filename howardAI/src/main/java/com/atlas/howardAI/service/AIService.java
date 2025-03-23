package com.atlas.howardAI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class AIService {

    public final ChatClient chatClient;
    public final HtmlService htmlService;

    public AIService(ChatClient.Builder builder, HtmlService htmlService) {
        this.chatClient = builder.build();
        this.htmlService = htmlService;
    }

    public String getAIResponse(String question) throws IOException {
        // Get personalities file
        ObjectMapper objectMapper = new ObjectMapper();
        Resource resource = new ClassPathResource("personalities.json");

        // Check if resource exist
        if (!resource.exists()) {
            throw new IOException("File not found");
        }

        Map<String, Map<String, String>> personalities = objectMapper.readValue(resource.getInputStream(), Map.class);

        if (!personalities.containsKey("personaConvenient")) {
            throw new IOException("Personal convenient not found");
        }

        String personaConvenient = personalities.get("personaConvenient").get("persona");

        if (personaConvenient == null || personaConvenient.isEmpty()) {
            throw new IOException("Personal convenient not found");
        }

        // Give the prompt
        PromptTemplate promptTemplate = new PromptTemplate(personaConvenient);
        Prompt prompt = promptTemplate.create(Map.of("message", question));
        String promptText = prompt.toString();

        // Get AI response and format it
        String response = chatClient.prompt().user(promptText).call().content().replaceAll("(?s)<think>.*?</think>", "")
                .replaceAll("^\"(.*)\"$", "$1") // Remove " from front to back
                .trim();
        return response;
    }

    // This method gets AI response and converts it to html
    public String getHtmlResponse(String question) throws IOException {
        // get AI response
        String response = getAIResponse(question);

        // Convert response to html
        return htmlService.markdownToHtml(response);
    }

    // This method generates chat title
    public String generateChatTitle(String question) throws IOException {

        return getAIResponse("Create a short and convenient title (max 30 characters) based on this message: " + question);
    }

}


