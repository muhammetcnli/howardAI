package com.atlas.howardAI.controller;

import com.atlas.howardAI.service.HtmlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatViewController {

    private final ChatClient chatClient;
    private final HtmlService htmlService;

    @Autowired
    public ChatViewController(ChatClient.Builder builder, HtmlService htmlService) {
        this.chatClient = builder.build();
        this.htmlService = htmlService;
    }

    @GetMapping("/chat")
    public String chat(Model model){
        return  "chat";
    }

    @GetMapping("/askAI")
    public String askAI(@RequestParam(value = "question") String question, Model model) {

        // Todo: every chat has its own ID, and routed through that id, and its randomly created
        String CHAT_ID = "";

        try {
            // Use Spring's file reading
            ObjectMapper objectMapper = new ObjectMapper();

            // Use ClassPathResource
            org.springframework.core.io.Resource resource =
                    new org.springframework.core.io.ClassPathResource("personalities.json");

            if (!resource.exists()) {
                model.addAttribute("error", "cannot find personalities.json.");
                return "error";
            }

            try {
                // Read file as InputStream
                Map<String, Map<String, String>> personalities =
                        objectMapper.readValue(resource.getInputStream(), Map.class);

                if (!personalities.containsKey("personaConvenient")) {
                    model.addAttribute("error", "Cannot find persona personaConvenient.");
                    return "error";
                }

                // Get the personaConvenient from personalities map
                String personaConvenient = personalities.get("personaConvenient").get("persona");

                if (personaConvenient == null || personaConvenient.isEmpty()) {
                    model.addAttribute("error", "personaConvenient is empty of invalid.");
                    return "error";
                }

                // Create the promptTemplate
                PromptTemplate promptTemplate = new PromptTemplate(personaConvenient);

                // Create the prompt
                Prompt prompt = promptTemplate.create(Map.of("message", question));
                String promptText = prompt.toString();

                // Get the response from groq api and format it
                String response = chatClient.prompt().user(promptText).call().content();
                response = response.replaceAll("(?s)<think>.*?</think>", "").trim();

                // Convert response to html
                String htmlResponse = htmlService.markdownToHtml(response);

                // Add the attributes question and response to the model
                model.addAttribute("question", question);
                model.addAttribute("htmlResponse", htmlResponse);

                return "chat";

            } catch (com.fasterxml.jackson.core.JsonParseException e) {
                model.addAttribute("error", "JSON format error: " + e.getMessage());
                return "error";
            } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
                model.addAttribute("error", "JSON value error: " + e.getMessage());
                return "error";
            }

        } catch (java.io.IOException e) {
            model.addAttribute("error", "File read error: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "AI response error: " + e.getMessage());
            return "error";
        }
    }



}
