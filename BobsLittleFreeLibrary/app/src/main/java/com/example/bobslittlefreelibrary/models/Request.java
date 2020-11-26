package com.example.bobslittlefreelibrary.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * This class bundles the information for requests.
 */

public class Request implements Serializable {
    private String reqSenderID;
    private String reqReceiverID;
    private String bookRequestedID;
    private double latitude;
    private double longitude;
    private String bookImageURL;
    private String bookTitle;
    private String requestID;
    private boolean alreadySeen;
    private boolean isReturnRequest;

    /**
     * This is an empty constructor for a Request object.
     */
    public Request() { }

    /**
     * This is the constructor for a Request object.
     * Note: Since NaN doesn't work in firebase and Lat/Long have defined ranges,
     * we set Lat/Long out of the ranges to know if it's valid or invalud
     * @param reqSenderUserID The user ID of the request sender
     * @param recReceiverUserID The user ID of the request receiver
     * @param bookRequestedID The ID of the book requested
     * @param bookImageURL The URL of the book's image
     * @param bookTitle The title of the book
     */
    public Request(String reqSenderUserID, String recReceiverUserID, String bookRequestedID, String bookImageURL,
                   String bookTitle) {
        this.reqSenderID = reqSenderUserID;
        this.reqReceiverID = recReceiverUserID;
        this.bookRequestedID = bookRequestedID;
        this.latitude = 1000;
        this.longitude = 1000;
        this.bookImageURL = bookImageURL;
        this.bookTitle = bookTitle;
        this.requestID = null;
        this.alreadySeen = false;
        this.isReturnRequest = false;       // by default the request is a borrow request
    }

    /**
     * Checks to see if it's a borrow request or a return request, default borrow
     * @return false for borrow request, true return request
     */

    public boolean isReturnRequest() {
        return isReturnRequest;
    }

    /**
     * A public setter for returnRequest
     * @param returnRequest true if return request, false if borrow request
     */

    public void setReturnRequest(boolean returnRequest) {
        isReturnRequest = returnRequest;
    }

    /**
     * Changes the borrow request to a return request
     */

    public void changeToReturnRequest() {
        isReturnRequest = true;
    }

    /**
     * A getter for the latitude
     * @return latitude
     */

    public double getLatitude() {
        return latitude;
    }

    /**
     * A setter for latitude
     * @param latitude the latitude
     */

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * A getter for longitude
     * @return longitude
     */

    public double getLongitude() {
        return longitude;
    }

    /**
     * A setter for longitude
     * @param longitude the longitude
     */

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    /**
     * This method returns if the request has been seen by the receiver.
     * @return A boolean value.
     * */
    public boolean isAlreadySeen() {
        return alreadySeen;
    }
    /**
     * This method sets the value of alreadySeen to true.
     * */
    public void setAlreadySeen() {
        this.alreadySeen = true;
    }
    /**
     * This method returns the ID of this request object.
     * @return requestID
     * */
    public String getRequestID() {
        return requestID;
    }
    /**
     * This method sets the ID value of this request object.
     * @param requestID The ID to be set.
     * */
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
