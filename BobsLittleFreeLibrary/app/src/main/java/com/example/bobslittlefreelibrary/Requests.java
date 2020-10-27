package com.example.bobslittlefreelibrary;

/**
 * This class bundles the information for requests.
 */

public class Requests {
    private User reqSender;
    private User reqReceiver;
    private Book bookRequested;
    private String reqStatus;
    private String location;

    /**
     * This is the (only) constructor for a request.
     * @param reqSender the user sending the request
     * @param recReceiver the user receiving the request
     * @param bookRequested the book being requested
     */
    public Requests(User reqSender, User recReceiver, Book bookRequested) {
        this.reqSender = reqSender;
        this.reqReceiver = recReceiver;
        this.bookRequested = bookRequested;
        this.reqStatus = "sent";
        this.location = null;
    }

    /**
     * A getter for reqStatus
     * @return the request status
     */
    public String getReqStatus() {
        return this.reqStatus;
    }

    /**
     * A setter for the request status
     * @param reqStatus request status we want to set
     */
    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    /**
     * Getter for location, returns null if location has not been set yet.
     * @return the location, null if it has not been set yet
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * A setter for location
     * @param location the location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * A getter for the book that's being requested
     * @return the book being requested
     */
    public Book getBookRequested() {
        return this.bookRequested;
    }
}
