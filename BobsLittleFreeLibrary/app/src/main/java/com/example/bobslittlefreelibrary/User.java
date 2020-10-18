package com.example.bobslittlefreelibrary;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private String username;
    private String password;
    private String email;
    private String address;
    private ArrayList<Book> bookArrayList;

    //Constructor
    public User(String username, String password, String email, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.bookArrayList = new ArrayList<Book>();
    }


    //getters
    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getAddress() { return address; }

    public ArrayList<Book> getBooks() { return bookArrayList; }


    //setters
    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(String address) { this.address = address; }



    public void addBook(Book someBook) {
        bookArrayList.add(someBook);

    }
    public void removeBook(Book someBook) {
        bookArrayList.remove(someBook);

    }

}
