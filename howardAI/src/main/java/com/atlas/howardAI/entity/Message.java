package com.atlas.howardAI.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "is_user")
    private Boolean isUser;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Message() {
    }

    public Message(String content, Boolean isUser, Chat chat) {
        this.content = content;
        this.isUser = isUser;
        this.chat = chat;
    }


    // Getters
    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Boolean getIsUser() {
        return isUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Chat getChat() {
        return chat;
    }

    // Setters
    public void setContent(String content) {
        this.content = content;
    }

    public void setIsUser(Boolean isUser) {
        this.isUser = isUser;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}