package com.atlas.howardAI.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "message_limit")
    private Integer messageLimit;

    //  private Integer messagesUsed;
    @Column(name = "limit_reset_date")
    private Integer limitResetDate;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Chat> chats;

    // empty constructor
    public User() {
    }

    // constructor excluding Id, chats list,
    public User(Integer messageLimit,String profilePhotoUrl, String firstName, String lastName, String email, String password, String role) {
        this.profilePhotoUrl = profilePhotoUrl;
        this.messageLimit = messageLimit;
        this.limitResetDate = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }



    // getters
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public Long getId() {
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

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
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
        if(chats==null){
            chats = new ArrayList<>();
        }

        // add chat to chats
        chats.add(chat);

        // set chat's user to this user
        chat.setUser(this);
    }

    // helper method for removing chat from user's list
    public void removeChat(Chat chat){
        // remove chat from chats
        chats.remove(chat);

        // set user to null
        chat.setUser(null);
    }

    // helper method for locating default profile picture URL
    public String defaultPhotoUrl(){
        return "/images/default_pic.jpg";
    }
}
