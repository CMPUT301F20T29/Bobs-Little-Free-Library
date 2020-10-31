package com.example.bobslittlefreelibrary;

import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class BookTest {

    /**
    *  Creates an instance of User class/classes to pass to book class
    * */
    private User mockUser() {
        return new User("Albert0", "ThickyChicky69", "Albert0@gmail.com",
                "12345 67st NE");
    }

    /**
     * Creates instance of book class/classes to test upon
     * */
    private Book mockBook() {
        User user = mockUser();
        return new Book("Guide", "Available", "A step by step instructions manual",
                "Jimmy Blake", "Picture1234", user, "1234567890");
    }


    /**
     * Test weather date stored by getDateNow() is indeed the current date
     * */
    @Test
    public void getDateCurrent(){
        assertEquals(java.text.DateFormat.getDateInstance().format(new Date()),
        mockBook().getDateNow());
    }


    /**
     * Creates instances of a User and Book Class and stores they're values
     * then proceeds to check if each of their getters returns the correct values
     * which would be the ones hardcoded in the mockBook() method above
     * */
    @Test
    public void testGetters(){
        Book book = mockBook();
        User userAlbert0 = book.getOwner();

        String title = book.getTitle();
        String status = book.getStatus();
        String description = book.getDescription();
        String author = book.getAuthor();
        String picture = book.getPicture();
        User user = book.getOwner();
        String ISBN = book.getISBN();

        assertEquals("Guide" ,title);
        assertEquals("Available", status);
        assertEquals("A step by step instructions manual" , description);
        assertEquals("Jimmy Blake" , author);
        assertEquals("Picture1234" , picture);
        assertEquals(userAlbert0 , user);
        assertEquals("1234567890" , ISBN);
    }


    /**
     * Tests setters using an instance of a Book() and an of User(), first we use getters
     * to store the values of the initial User() (from the Book() class) to ensure they
     * aren't being changed before hand then on the second instance of an equivalent User()
     * We set the values to arbitrary new values and then assert through getters once again that
     * the instances values have indeed changed to the new specified values
     * */
    @Test
    public void testSetters(){
        Book book = mockBook();
        User userAlbert0 = book.getOwner();
        User user2 = mockUser();


        String title = book.getTitle();
        String status = book.getStatus();
        String description = book.getDescription();
        String author = book.getAuthor();
        String picture = book.getPicture();
        User user = book.getOwner();
        String ISBN = book.getISBN();
        assertEquals("Guide" ,title);
        assertEquals("Available", status);
        assertEquals("A step by step instructions manual" , description);
        assertEquals("Jimmy Blake" , author);
        assertEquals("Picture1234" , picture);
        assertEquals(userAlbert0 , user);
        assertEquals("1234567890" , ISBN);

        book.setTitle("Not A Guide");
        book.setStatus("Unavailable");
        book.setDescription("Definitely Not A step by step instructions manual");
        book.setAuthor("Sandra Bullcrotch");
        book.setPicture("Picture4321");
        book.setOwner(user2);
        book.setISBN("0987654321");

        String title2 = book.getTitle();
        String status2 = book.getStatus();
        String description2 = book.getDescription();
        String author2 = book.getAuthor();
        String picture2 = book.getPicture();
        User userNew = book.getOwner();
        String ISBN2 = book.getISBN();
        assertEquals("Not A Guide" ,title2);
        assertEquals("Unavailable", status2);
        assertEquals("Definitely Not A step by step instructions manual" , description2);
        assertEquals("Sandra Bullcrotch" , author2);
        assertEquals("Picture4321" , picture2);
        assertEquals(user2 , userNew);
        assertEquals("0987654321" , ISBN2);
    }






}
