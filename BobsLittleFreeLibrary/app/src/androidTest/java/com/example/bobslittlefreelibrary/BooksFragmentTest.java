package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooksFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.enterText(0, "kvnguyen@ualberta.ca");
        solo.enterText(1, "chickennuggets");
        solo.clickOnButton("Login");
        solo.clickOnView(solo.getView(R.id.books_page));
        assert(solo.searchText("Bookshelf"));
    }

    @Test
    public void listObjectTest(){
        solo.clickInList(0, 0);
        assert(solo.searchText("Author"));
        solo.clickOnButton(0);
        assert(solo.searchText("Bookshelf"));

    }

    @Test
    public void clickFAB(){
        solo.clickOnView(solo.getView(R.id.add_Item));
        assert(solo.searchText("Add Book"));
        solo.clickOnButton(0);
        assert(solo.searchText("Bookshelf"));

    }



    @Test
    public void filterTest(){
        solo.clickOnButton(0);

        assertTrue(solo.searchText("HIDE"));
        assertTrue(solo.searchText("All"));
        assertTrue(solo.searchText("Available"));
        assertTrue(solo.searchText("Requested"));
        assertTrue(solo.searchText("Accepted"));
        assertTrue(solo.searchText("Borrowed"));


        // chip for Available filter
        solo.clickOnButton("Available");
        assertTrue(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));

        // chip for Requested filter
        solo.clickOnButton("Requested");
        assertTrue(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));
        // click on Requested Again
        solo.clickOnButton("Requested");
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));

        // chip for Accepted filter
        solo.clickOnButton("Accepted");
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertTrue(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));

        // chip for Borrowed filter
        solo.clickOnButton("Borrowed");
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertTrue(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));

        // chip for All filter
        solo.clickOnButton("All");
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertTrue(solo.isTextChecked("All"));
        // chip for All filter again
        solo.clickOnButton("All");
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));




        // click on Hide filter
        solo.clickOnButton(0);
        assertTrue(solo.searchText("FILTER"));

    }





}
