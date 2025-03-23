package com.atlas.howardAI.service;

import com.atlas.howardAI.entity.Chat;
import com.atlas.howardAI.entity.Message;
import com.atlas.howardAI.entity.User;
import com.atlas.howardAI.repository.ChatRepository;
import com.atlas.howardAI.repository.MessageRepository;
import com.atlas.howardAI.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final AIService aiService;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository, AIService aiService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    public Chat createChat(){
        User currentUser = getCurrentUser();

        // Create new chat
        Chat chat = new Chat();
        chat.setUser(currentUser);

        // Save chat and return
        return chatRepository.save(chat);
    }

    public Chat findChatById(UUID id) {
        return chatRepository.findById(id).orElseThrow(()-> new RuntimeException("Chat not found"+ id));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            String email = oauth2User.getAttribute("email");
            if (email != null) {
                return userRepository.findByEmail(email)
                        .orElseThrow(()-> new RuntimeException("User not found"));
            }
        }

        throw new RuntimeException("Not authenticated or invalid user.");
    }

    public void addMessageToChat(UUID chatId, String content, boolean isUser) {
        Chat chat = findChatById(chatId);

        Message message = new Message();
        message.setContent(content);
        message.setChat(chat);
        message.setIsUser(isUser);

        if (isUser && chat.getMessages().isEmpty()) {
            try {

                String title = aiService.generateChatTitle(content);
                chat.setTitle(title);
            } catch (Exception e) {
                chat.setTitle("Chat " + LocalDate.now());
            }
        }

        chat.getMessages().add(message);
        chatRepository.save(chat);
    }

    public Chat findChatByIdWithOrderedMessages(UUID chatId) {
        Chat chat = findChatById(chatId);

        List<Message> orderedMessages = chat.getMessages().stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());

        chat.setMessages(new ArrayList<>(orderedMessages));
        return chat;
    }
}
