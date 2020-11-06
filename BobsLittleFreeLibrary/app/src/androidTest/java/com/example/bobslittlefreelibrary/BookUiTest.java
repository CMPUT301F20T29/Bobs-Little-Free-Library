package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.books.AddBookActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookUiTest {


    private Solo solo;

    /**
     *
     * */
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);


    /**
     * Runs before all tests and creates the solo instance
     * @throws Exception
     * */
    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.enterText(0, "Lebron@lakers.ca");
        solo.enterText(1, "password");
        solo.clickOnButton("Login");
        assertTrue(solo.searchText("Latest Books"));
        solo.clickOnView(solo.getView(R.id.books_page));
    }

    /**
     * This test checks if fragment_container is initialized properly.
     * */
    @Test
    public void bookTest() {
        // Add book
        solo.clickOnView(solo.getView(R.id.add_Item));
        solo.assertCurrentActivity("Failed enter AddBookActivity", AddBookActivity.class);

        solo.enterText(0, "1594205116");
        solo.enterText(1, "Title TEST");
        solo.enterText(2, "Author TEST");
        solo.enterText(3, "desc TEST");
        solo.drag(0, 0, 1600, 0, 40);

        solo.clickOnButton("Add");

        solo.assertCurrentActivity("Failed to exit AddBookActivity", MainActivity.class);
        assertTrue(solo.searchText("Title TEST"));
        assertTrue(solo.searchText("Author TEST"));

        // Edit Book
        solo.clickInList(1);
        solo.clickOnButton("Edit Info");
        solo.enterText(0, " EDITS");
        solo.drag(0, 0, 1600, 0, 40);
        solo.clickOnButton("Edit Book");
        assertTrue(solo.searchText("desc TEST EDITS"));
        solo.assertCurrentActivity("Failed to exit EditBookActivity", MyBookViewActivity.class);
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Failed to exit MyBookViewActivity", MainActivity.class);

        // Delete Book
        solo.clickInList(1);
        solo.clickOnButton("Remove");

        solo.assertCurrentActivity("Failed to exit MyBookViewActivity", MainActivity.class);
        assertFalse(solo.searchText("Title TEST", true));

    }


}
