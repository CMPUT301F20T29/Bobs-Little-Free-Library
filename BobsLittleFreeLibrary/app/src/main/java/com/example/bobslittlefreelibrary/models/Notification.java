package com.example.bobslittlefreelibrary.models;

import java.io.Serializable;

/**
 * This class represents a Notification object. Notification objects contain information about events
 * that affect a User, i.e. receiving a request for one of their Books, or one of their requests getting accepted.
 * */
public class Notification implements Serializable {
    // Class variables
    NotificationType type;
    String message;
    String timestamp;
    String bookID; // Used to find the book the notification will lead to when pressed
    String userID; // The userID is the ID of the User the notification belongs to

    /**
     * An empty constructor
     * */
    public Notification() {}

    /**
     * A constructor for a Notification object.
     * @param type The type of notification
     * @param message The message to be displayed
     * @param timestamp The time the notification was created
     * @param bookID The ID of the Book involved in the notification
     * @param userID The ID of the User that this notification belongs to
     * */
    public Notification(NotificationType type, String message, String timestamp, String bookID, String userID) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
        this.bookID = bookID;
        this.userID = userID;
    }

    // Getters
    /**
     * This method returns the type of a Notification object.
     * @return Returns type
     * */
    public NotificationType getType() {
        return type;
    }
    /**
     * This method returns the messagetimestampbookID of a Notification object.
     * @return Returns messagetimestampbookID
     * */
    public String getMessage() {
        return message;
    }
    /**
     * This method returns the timestampbookID of a Notification object.
     * @return Returns timestampbookID
     * */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * This method returns the bookID of a Notification object.
     * @return Returns bookID
     * */
    public String getBookID() {
        return bookID;
    }
    /**
     * This method returns the userID of a Notification object.
     * @return Returns userID
     * */
    public String getUserID() {
        return userID;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
