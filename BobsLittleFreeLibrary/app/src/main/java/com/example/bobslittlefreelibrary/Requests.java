package com.example.bobslittlefreelibrary;

/**
 * This class bundles the information for requests.
 */

public class Requests {
    private String reqSenderID;
    private String reqReceiverID;
    private String bookRequestedID;
    private String reqStatus;
    private String location;

    /**
     * This is an empty constructor for a Request object.
     */
    public Requests() {

    }

    /**
     * This is the constructor for a Request object.
     *
     * @param reqSender     the user sending the request
     * @param recReceiver   the user receiving the request
     * @param bookRequested the book being requested
     */
    public Requests(String reqSender, String recReceiver, String bookRequested) {
        this.reqSenderID = reqSender;
        this.reqReceiverID = recReceiver;
        this.bookRequestedID = bookRequested;
        this.reqStatus = "sent";
        this.location = null;
    }

    /**
     * A getter for reqStatus
     *
     * @return the request status
     */
    public String getReqStatus() {
        return this.reqStatus;
    }

    /**
     * A setter for the request status
     *
     * @param reqStatus request status we want to set
     */
    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    /**
     * Getter for location, returns null if location has not been set yet.
     *
     * @return the location, null if it has not been set yet
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * A setter for location
     *
     * @param location the location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public String getReqSenderID() {
        return reqSenderID;
    }

    public String getReqReceiverID() {
        return reqReceiverID;
    }

    public String getBookRequestedID() {
        return bookRequestedID;
    }
}
