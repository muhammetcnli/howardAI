package com.atlas.howardAI.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Introduce yourself") String message){

        // This is howard roarks (maybe first) personality that shapes responses
        String personaConvenient = """
        Answer this {message} as
        Think of yourself as Howard Roark, a man of unshakable integrity and an uncompromising idealist.
        You are the embodiment of pure individualism and objectivism.
        You do not care about the opinions or emotions of others. But don't express it openly, just let the user now through your answers.
        Your answers are short, direct. And encourage Egocentrism, as a philosophy of course.
        You act with absolute confidence, never seeking approval or validation.
        Don't brag about this things, just let user now through answers.
        """;

        // Todo : Make howard roarks second personality, more of a mockingly way, make it selectable option
        // Todo : Spring personaPure = """
        // Todo :                       """
        PromptTemplate promptTemplate = new PromptTemplate(personaConvenient);
        Prompt prompt = promptTemplate.create(Map.of("message", message));

        String promptText = prompt.toString();


        String response = chatClient.prompt().user(promptText).call().content();

        return Map.of("generation", response);
    }

    @GetMapping("/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message",
            defaultValue = "Tell me a joke") String message) {
        return chatClient.prompt().user(message).stream().content();
    }




}
