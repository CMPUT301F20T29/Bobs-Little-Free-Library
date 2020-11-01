package com.example.bobslittlefreelibrary;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class UserTest {

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
    *  Uses 2 Book instances, First array size is confirmed to be 0 then both book classes
    *  are added to the array asserting that the array increases appropriately between each one
     *  and addBook() works as it should then one is removed from the array testing that
     *  Remove() works appropriately and the getBooks() method is constantly tested throughout this
     *  process
    * */
    @Test
    public void testArrayAddDelete(){
        Book book1 = mockBook();
        Book book2 = mockBook();


        User userTest = mockUser();
        assertEquals(0, userTest.getBooks().size());

        userTest.addBook(book1);
        assertEquals(1, userTest.getBooks().size());

        userTest.addBook(book2);
        assertEquals(2, userTest.getBooks().size());

        userTest.removeBook(book1);
        assertEquals(1, userTest.getBooks().size());

    }

    /**
     * Tests getters by creating an instance of User() then storing all it's values and asserting
     * they are as expected
     * */
    @Test
    public void testGetterExcludeArray(){
        User userTest = mockUser();

        String username = userTest.getUsername();
        String password = userTest.getPassword();
        String email = userTest.getEmail();
        String address = userTest.getAddress();

        assertEquals("Albert0",username);
        assertEquals("ThickyChicky69", password);
        assertEquals("Albert0@gmail.com", email);
        assertEquals("12345 67st NE", address);

    }

    /**
     * Tests setters by creating an instance of a user and first checking that the values are
     * initially as should be then the values are changed using setter methods and stored again and
     * checked to see if the new values have been properly set
     * */
    @Test
    public void testSetterExcludeArray(){
        User userTest = mockUser();

        String username = userTest.getUsername();
        String password = userTest.getPassword();
        String email = userTest.getEmail();
        String address = userTest.getAddress();
        assertEquals("Albert0",username);
        assertEquals("ThickyChicky69", password);
        assertEquals("Albert0@gmail.com", email);
        assertEquals("12345 67st NE", address);

        userTest.setUsername("Roberts80085");
        userTest.setPassword("JuicyFruity28");
        userTest.setEmail("Roberts80085@gmail.com");
        userTest.setAddress("54321 76st NW");

        String username2 = userTest.getUsername();
        String password2 = userTest.getPassword();
        String email2 = userTest.getEmail();
        String address2 = userTest.getAddress();
        assertEquals("Roberts80085",username2);
        assertEquals("JuicyFruity28", password2);
        assertEquals("Roberts80085@gmail.com", email2);
        assertEquals("54321 76st NW", address2);

    }






}
