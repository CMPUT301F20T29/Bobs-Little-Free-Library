package com.example.bobslittlefreelibrary.models;

/**
 * This class represents a Notification object. Notification objects contain information about events
 * that affect a User, i.e. receiving a request for one of their Books, or one of their requests getting accepted.
 * */
public class Notification {
    // Class variables
    String type; // Determines the Icon shown ; Types: Request, Accepted, Declined, ...
    String message;
    String timestamp;



}
