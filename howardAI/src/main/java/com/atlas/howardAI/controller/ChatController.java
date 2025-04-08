package com.atlas.howardAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api") // api base path for all the api requests
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/debug/token")
    public Map<String, Object> debugToken(OAuth2AuthenticationToken token) {
        Map<String, Object> tokenInfo = new HashMap<>();

        // Main OAuth2 information
        tokenInfo.put("name", token.getName());
        tokenInfo.put("authorities", token.getAuthorities());
        tokenInfo.put("authenticated", token.isAuthenticated());
        tokenInfo.put("details", token.getDetails());
        tokenInfo.put("authorizedClientRegistrationId", token.getAuthorizedClientRegistrationId());

        // Principal (user) information
        Map<String, Object> attributes = new HashMap<>(token.getPrincipal().getAttributes());
        tokenInfo.put("principal_attributes", attributes);

        // OAuth2 user information
        tokenInfo.put("principal_name", token.getPrincipal().getName());

        return tokenInfo;
    }

    @GetMapping("ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Introduce yourself") String message) {

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

    // ToDo: add personality to generateStream
    @GetMapping("/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message",
            defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt().user(message).stream().content();
    }


}
