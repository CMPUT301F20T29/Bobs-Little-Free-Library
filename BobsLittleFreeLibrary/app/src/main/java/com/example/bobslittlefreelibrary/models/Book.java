package com.example.bobslittlefreelibrary.models;

import java.io.Serializable;
import java.util.Date;

/**
 * This is an entity class that represents a Book object. It contains information about a book.
 *
 * */
public class Book implements Serializable {
    // Class variables
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String ownerID;
    private String status;
    private String pictureURL;
    private String dateAdded;

    /**
     * This is an empty constructor for a Book object
     * */
    public Book() { }

    /**
     * This is a constructor for a Book object with a picture url.
     * @param title
     *      This is the title of the Book.
     * @param author
     *      This is the author of the Book.
     * @param ISBN
     *      This is the ISBN of the Book.
     * @param description
     *      This is the description of the Book.
     * @param ownerID
     *      This is the ID of the Owner of the Book as a String.
     * @param status
     *      This is the status of the Book.
     * @param pictureURL
     *      This is the url of the picture/cover of the Book.
     * */
    public Book(String title, String author, String ISBN, String description, String ownerID, String status, String pictureURL){
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.ownerID = ownerID;
        this.status = status;
        this.pictureURL = pictureURL;
        this.dateAdded = getDateNow();
    }

    /**
     * This is a constructor for a Book object without a picture url.
     * @param title
     *      This is the title of the Book.
     * @param author
     *      This is the author of the Book.
     * @param ISBN
     *      This is the ISBN of the Book.
     * @param description
     *      This is the description of the Book.
     * @param ownerID
     *      This is the ID of the Owner of the Book as a String.
     * @param status
     *      This is the status of the Book.
     * */
    public Book(String title, String author, String ISBN, String description, String ownerID, String status){
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.ownerID = ownerID;
        this.status = status;
        this.dateAdded = getDateNow();
    }

    // Methods
    //Gets date at the time object is created
    private String getDateNow() { return java.text.DateFormat.getDateInstance().format(new Date()); }

    // Getters
    /**
     * This method returns the title of a Book object.
     * @return
     *      Returns title
     * */
    public String getTitle() { return this.title; }
    /**
     * This method returns the status of a Book object.
     * @return
     *      Returns status
     * */
    public String getStatus() { return this.status; }
    /**
     * This method returns the description of a Book object.
     * @return
     *      Returns description
     * */
    public String getDescription() { return this.description; }
    /**
     * This method returns the author of a Book object.
     * @return
     *      Returns author
     * */
    public String getAuthor() { return this.author; }
    /**
     * This method returns the url of the picture of a Book object.
     * @return
     *      Returns pictureURL
     * */
    public String getPictureURL() { return this.pictureURL; }
    /**
     * This method returns the ID of the Owner of a Book object.
     * @return
     *      Returns ownerID
     * */
    public String getOwnerID() { return this.ownerID; }
    /**
     * This method returns the ISBN of a Book object.
     * @return
     *      Returns ISBN
     * */
    public String getISBN() { return this.ISBN; }
    /**
     * This method returns the date a Book object was created.
     * @return
     *      Returns the dateAdded
     * */
    public String getDateAdded() { return dateAdded; }

    // Setters - We can probably remove these later
    public void setTitle(String title) { this.title = title; }

    public void setStatus(String status) { this.status = status; }

    public void setDescription(String description) { this.description = description; }

    public void setAuthor(String author) { this.author = author; }

    public void setPictureURL(String picture) { this.pictureURL = picture; }

    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }
}
