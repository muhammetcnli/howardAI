package com.atlas.howardAI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 36, name = "chat_identifier")
    private String chatIdentifier;


    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages  = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // empty constructor, chatIdentifier and createdAt initialization
    public Chat() {
        // make the chatIdentifier UUID for routing
        this.chatIdentifier = UUID.randomUUID().toString();
        // make createdAt now
        this.createdAt = LocalDate.now();
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getChatIdentifier() {
        return chatIdentifier;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public User getUser() {
        return user;
    }

    // setter
    public void setUser(User user) {
        this.user = user;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public void removeMessage(Message message){
        messages.remove(message);
    }



}
