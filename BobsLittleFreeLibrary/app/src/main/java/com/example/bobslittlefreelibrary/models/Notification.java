package com.example.bobslittlefreelibrary.models;

/**
 * This class represents a Notification object. Notification objects contain information about events
 * that affect a User, i.e. receiving a request for one of their Books, or one of their requests getting accepted.
 * */
public class Notification {
    // Class variables
    NotificationType type;
    String message;
    String timestamp;
    String bookID;
    String userID; // The userID is the ID of the User the notification belongs to


    public Notification() {}

    public Notification(NotificationType type, String message, String timestamp, String bookID, String userID) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
        this.bookID = bookID;
        this.userID = userID;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
