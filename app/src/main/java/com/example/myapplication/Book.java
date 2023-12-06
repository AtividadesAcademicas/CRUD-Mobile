package com.example.myapplication;

import java.time.LocalDateTime;

public class Book {
    private String id;
    private Long registry;
    private String title;
    private String author;
    private String field;
    private String language;
    private String edition;
    private String description;
    private String publishingCompany;
    private String state;
    private String userEmail;
    private LocalDateTime createdAt;

    public Book(Long registry, String title, String author, String field, String language, String edition, String description,
                String publishingCompany, String state, String userEmail, LocalDateTime createdAt) {
        this.registry = registry;
        this.title = title;
        this.author = author;
        this.field = field;
        this.language = language;
        this.edition = edition;
        this.description = description;
        this.publishingCompany = publishingCompany;
        this.state = state;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }

    public Book() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRegistry() {
        return registry;
    }

    public void setRegistry(Long registry) {
        this.registry = registry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
