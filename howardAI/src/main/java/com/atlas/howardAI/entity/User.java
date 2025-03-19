package com.atlas.howardAI.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(name = "message_limit")
    private Integer messageLimit;

    //  private Integer messagesUsed;
    @Column(name = "limit_reset_date")
    private Integer limitResetDate;

    @Column(name = "picture")
    private String picture;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Chat> chats;

    // empty constructor
    public User() {
    }

    // constructor excluding Id, chats list
    public User(Integer messageLimit, String picture, String firstName, String lastName, String email, String password, String role) {
        this.picture = picture;
        this.messageLimit = messageLimit;
        this.limitResetDate = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // getters
    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getName() {
        return firstName;
    }

    public String getPicture() {
        return picture;
    }

    public UUID getId() {
        return id;
    }

    public Integer getMessageLimit() {
        return messageLimit;
    }

    public Integer getLimitResetDate() {
        return limitResetDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Chat> getChats() {
        return chats;
    }

    // setters
    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setName(String name) {
        this.firstName = name; // use firstName as name
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setPicture(String profilePhotoUrl) {
        this.picture = profilePhotoUrl;
    }

    public void setMessageLimit(Integer messageLimit) {
        this.messageLimit = messageLimit;
    }

    public void setLimitResetDate(Integer limitResetDate) {
        this.limitResetDate = limitResetDate;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String surname) {
        this.lastName = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // helper method for adding chat to user's list
    public void addChat(Chat chat) {
        // check if chats is null, if yes, create new chats list
        if (chats == null) {
            chats = new ArrayList<>();
        }

        // add chat to chats
        chats.add(chat);

        // set chat's user to this user
        chat.setUser(this);
    }

    public void setUsername(String email) {
        this.email = email; // email'i username olarak kullanacağız
    }

    // helper method for removing chat from user's list
    public void removeChat(Chat chat) {
        // remove chat from chats
        chats.remove(chat);

        // set user to null
        chat.setUser(null);
    }

    // helper method for locating default profile picture URL
    public String defaultPicture() {
        return "/images/default_pic.jpg";
    }
}
