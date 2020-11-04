package com.example.bobslittlefreelibrary;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private String username;
    private String email;
    private String address;
    private ArrayList<String> bookArrayList;

    //Constructor
    public User(String username, String email, String address) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.bookArrayList = new ArrayList<>();
    }


    //getters

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getAddress() { return address; }

    public ArrayList<String> getBooks() { return bookArrayList; }

    //setters
    public void setUsername(String username) { this.username = username; }


    public void setEmail(String email) { this.email = email; }

    public void setAddress(String address) { this.address = address; }



    // Adds books to users array list of books
    public void addBook(String someBookid) { bookArrayList.add(someBookid); }

    // Removes books from users array list of books
    public void removeBook(Book someBook) { bookArrayList.remove(someBook); }

}
