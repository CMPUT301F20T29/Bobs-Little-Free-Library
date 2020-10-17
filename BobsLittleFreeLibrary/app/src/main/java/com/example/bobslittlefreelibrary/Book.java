package com.example.bobslittlefreelibrary;

import java.io.Serializable;

public class Book implements Serializable
{
    private String title;
    private String status;
    private String description;
    private String author;
    private String picture;
    private User owner;
    private String ISBN;

    Book(String title, String status, String description, String author, String picture, User owner, String ISBN){
        this.title = title;
        this.status = status;
        this.description = description;
        this.author = author;
        this.picture = picture;
        this.owner = owner;
        this.ISBN = ISBN;

    }


    public String getTitle() { return this.title; }

    public String getStatus() { return this.status; }

    public String getDescription() { return this.description; }

    public String getAuthor() { return this.author; }

    public String getPicture() { return this.picture; }

    public User getOwner() { return this.owner; }

    public String getISBN() { return this.ISBN; }



    public void setTitle(String title) { this.title = title; }

    public void setStatus(String status) { this.status = status; }

    public void setDescription(String description) { this.description = description; }

    public void setAuthor(String author) { this.author = author; }

    public void setPicture(String picture) { this.picture = picture; }

    public void setOwner(User owner) { this.owner = owner; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }
}
