package com.example.bobslittlefreelibrary;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is an entity class that represents a User object. It contains information about a user.
 *
 * */
public class User implements Serializable  {
    private String username;
    private String email;
    private String address;
    private ArrayList<String> bookIDs;

    /**
     * This is an empty constructor for a User object
     * */
    public User() { }

    /**
     * This is a constructor for a User object.
     * @param username
     *      This is the username of the User.
     * @param email
     *      This is the email of the User.
     * @param address
     *      This is the address of the User.
     * */
    public User(String username, String email, String address) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.bookIDs = new ArrayList<>();
    }

    // Methods
    // Maybe rename methods to addBookID and removeBookID later.
    /**
     * This methods assigns a book to a User. It adds the Book's ID to bookIDs.
     * @param bookID
     *      This is the ID of the Book to be added.
     * */
    public void addBook(String bookID) { bookIDs.add(bookID); }

    // Removes books from users array list of books
    /**
     * This methods unassigns a book to a User. It removes the Book's ID to bookIDs.
     * @param bookID
     *      This is the ID of the Book to be removed.
     * */
    public void removeBook(String bookID) { bookIDs.remove(bookID); }

    // Getters
    /**
     * This method returns the username of a User object.
     * @return
     *      Returns username
     * */
    public String getUsername() { return username; }
    /**
     * This method returns the email of a User object.
     * @return
     *      Returns email
     * */
    public String getEmail() { return email; }
    /**
     * This method returns the address of a User object.
     * @return
     *      Returns address
     * */
    public String getAddress() { return address; }
    /**
     * This method returns the BookIDs of the books owned by a User object.
     * @return
     *      Returns bookIDs
     * */
    public ArrayList<String> getBookIDs() { return bookIDs; }

    // Setters - We can probably remove these later since we will be changing values directly in db
    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(String address) { this.address = address; }
}
