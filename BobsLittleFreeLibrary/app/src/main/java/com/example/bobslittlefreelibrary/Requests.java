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
    private String bookImageURL;
    private String bookTitle;

    /**
     * This is an empty constructor for a Request object.
     */
    public Requests() {

    }

    /**
     * This is the constructor for a Request object.
     * @param reqSenderUser The user ID of the request sender
     * @param recReceiverUser The user ID of the request receiver
     * @param bookRequestedID The ID of the book requested
     * @param bookImageURL The URL of the book's image
     * @param bookTitle The title of the book
     */
    public Requests(String reqSenderUser, String recReceiverUser, String bookRequestedID, String bookImageURL,
                    String bookTitle) {
        this.reqSenderID = reqSenderUser;
        this.reqReceiverID = recReceiverUser;
        this.bookRequestedID = bookRequestedID;
        this.reqStatus = "sent";
        this.location = null;
        this.bookImageURL = bookImageURL;
        this.bookTitle = bookTitle;
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

    /**
     * A getter for the request sender's ID
     * @return request sender ID
     */

    public String getReqSenderID() {
        return reqSenderID;
    }

    /**
     * A getter for the request receiver's ID
     * @return request receiver ID
     */

    public String getReqReceiverID() {
        return reqReceiverID;
    }

    /**
     * A getter for the requested book's ID
     * @return requested book ID
     */

    public String getBookRequestedID() {
        return bookRequestedID;
    }

    /**
     * A getter for the requested book's image url
     * @return requested book image URL
     */

    public String getBookImageURL() {
        return bookImageURL;
    }

    /**
     * A getter for the book's title
     * @return requested book's title
     */

    public String getBookTitle() {
        return bookTitle;
    }
}
