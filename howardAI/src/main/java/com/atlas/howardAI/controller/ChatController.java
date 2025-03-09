package com.atlas.howardAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api") // api base path for all the api requests
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Introduce yourself") String message){

        try {
            // Getting the json personalities
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Map<String, String>> personalities = objectMapper.readValue(new File("src/main/resources/personalities.json"), Map.class);

            // Getting the personaConvenient
            String personaConvenient = personalities.get("personaConvenient").get("persona");


            PromptTemplate promptTemplate = new PromptTemplate(personaConvenient);
            Prompt prompt = promptTemplate.create(Map.of("message", message));

            String promptText = prompt.toString();


            String response = chatClient.prompt().user(promptText).call().content();

            return Map.of("generation", response);
        } catch (Exception e) {

            return Map.of("error", e.getMessage());
        }
    }

    @GetMapping("/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message",
            defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt().user(message).stream().content();
    }




}
