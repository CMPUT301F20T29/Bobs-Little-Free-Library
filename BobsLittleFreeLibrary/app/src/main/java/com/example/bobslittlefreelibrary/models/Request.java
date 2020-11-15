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

    /**
     * This is an empty constructor for a Request object.
     */
    public Request() { }

    /**
     * This is the constructor for a Request object.
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
        this.latitude = Double.NaN;
        this.longitude = Double.NaN;
        this.bookImageURL = bookImageURL;
        this.bookTitle = bookTitle;
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
}
