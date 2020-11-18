package com.example.bobslittlefreelibrary.models;

/**
 * This enum defines different values for the type of a Notification.
 * It can be extended to provide more types of Notifications.
 *
 * Currently there are 3 types of Notifications
 * - Borrow ; A notification received when a User's Book is requested to be borrowed.
 * - Return ; A notification received when a User wants to requests to return a Book.
 * - Delete ; A notification received when a User deletes their request for a Book.
 * - Accept ; A notification received when a User's Request was accepted.
 * - Decline ; A notification received when a User's Request was declined.
 *
 * Better Type names could be used.
 * */
public enum NotificationType {
    BORROW,
    RETURN,
    DELETE,
    ACCEPT,
    DECLINE
}
