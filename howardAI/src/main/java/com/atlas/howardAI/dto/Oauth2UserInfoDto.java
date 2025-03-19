package com.atlas.howardAI.dto;

public class Oauth2UserInfoDto {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String picture;

    // Varsayılan yapıcı
    public Oauth2UserInfoDto() {
    }

    // Parametreli yapıcı
    public Oauth2UserInfoDto(String id, String name, String lastName, String email, String picture) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
    }

    // Getter ve setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    // Builder metodu
    public static Oauth2UserInfoDtoBuilder builder() {
        return new Oauth2UserInfoDtoBuilder();
    }

    // Builder sınıfı
    public static class Oauth2UserInfoDtoBuilder {
        private String id;
        private String name;
        private String lastName;
        private String email;
        private String picture;

        public Oauth2UserInfoDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public Oauth2UserInfoDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Oauth2UserInfoDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Oauth2UserInfoDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public Oauth2UserInfoDtoBuilder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public Oauth2UserInfoDto build() {
            return new Oauth2UserInfoDto(id, name, lastName, email, picture);
        }
    }
}