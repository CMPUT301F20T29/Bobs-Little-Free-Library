package com.example.bobslittlefreelibrary;

import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Notification;
import com.example.bobslittlefreelibrary.models.NotificationType;
import com.example.bobslittlefreelibrary.models.User;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class NotificationTest {

    /**
     *  Creates an instance of a Notification Object
     * */
    private Notification mockNotification() {
        return new Notification(NotificationType.BORROW, "Notification message", "25/11/2020", "BOOKID", "USERID");
    }

    /**
     *
     * */
    @Test
    public void testGetters(){

    }


    /**
     *
     * */
    @Test
    public void testSetters(){

    }
}
