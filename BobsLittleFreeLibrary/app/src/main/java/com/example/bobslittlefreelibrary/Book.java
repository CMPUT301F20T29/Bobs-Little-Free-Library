package com.example.bobslittlefreelibrary;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable
{
    private String title;
    private String status;
    private String description;
    private String author;
    private String pictureURL;
    private String owner;
    private String ISBN;
    private String dateAdded;


    //Constructor where picture is specified
    public Book(String title, String status, String description, String author, String pictureURL,
                String owner, String ISBN){
        this.title = title;
        this.status = status;
        this.description = description;
        this.author = author;
        this.pictureURL = pictureURL;
        this.owner = owner;
        this.ISBN = ISBN;
        this.dateAdded = getDateNow();



    }

    //Constructor if picture is left out
    public Book(String title, String status, String description, String author, String owner,
                String ISBN){
        this.title = title;
        this.status = status;
        this.description = description;
        this.author = author;
        this.pictureURL = pictureURL;
        this.owner = owner;
        this.ISBN = ISBN;
        this.dateAdded = getDateNow();

    }

    public Book() {
    }


    //Getters
    public String getTitle() { return this.title; }

    public String getStatus() { return this.status; }

    public String getDescription() { return this.description; }

    public String getAuthor() { return this.author; }

    public String getPictureURL() { return this.pictureURL; }

    public String getOwner() { return this.owner; }

    public String getISBN() { return this.ISBN; }

    public String getDateAdded() { return dateAdded; }

    //Gets date at the time object is created
    private String getDateNow() { return java.text.DateFormat.getDateInstance().format(new Date()); }



    //Setters
    public void setTitle(String title) { this.title = title; }

    public void setStatus(String status) { this.status = status; }

    public void setDescription(String description) { this.description = description; }

    public void setAuthor(String author) { this.author = author; }

    public void setPictureURL(String picture) { this.pictureURL = picture; }

    public void setOwner(String owner) { this.owner = owner; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

}
