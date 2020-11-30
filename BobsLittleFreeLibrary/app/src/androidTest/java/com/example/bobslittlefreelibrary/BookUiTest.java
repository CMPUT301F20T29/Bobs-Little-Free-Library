package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.books.AddBookActivity;
import com.example.bobslittlefreelibrary.views.books.EditBookActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.google.android.gms.location.ActivityTransition;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
        solo.enterText(0, "mrl@ualberta.ca");
        solo.enterText(1, "password");
        solo.clickOnButton("Login");
        assertTrue(solo.searchText("Latest Books"));
        solo.clickOnView(solo.getView(R.id.books_page));
    }

    /**
     * This test checks if adds, edits and then deletes a book. tests to see if books are correctly
     * added to BooksFragment.
     * */
    @Test
    public void bookTest() throws InterruptedException {
        // Add book
        solo.clickOnView(solo.getView(R.id.add_Item));
        solo.assertCurrentActivity("Failed enter AddBookActivity", AddBookActivity.class);

        solo.enterText(0, "1594205116");
        solo.enterText(1, "Title TEST");
        solo.enterText(2, "Author TEST");
        solo.enterText(3, "desc TEST");
        scrollDown();
        solo.clickOnButton("Add");

        solo.assertCurrentActivity("Failed to exit AddBookActivity", MainActivity.class);
        assertTrue(solo.searchText("Title TEST"));

        // Edit Book
        solo.clickOnText("Title TEST");
        solo.clickOnButton("Edit Info");
        solo.enterText(0, " Title EDITS");
        solo.enterText(1, " Author EDITS");
        solo.enterText(2, " desc EDITS");
        solo.drag(0, 0, 1600, 0, 10);
        solo.clickOnButton("Edit Book");
        solo.assertCurrentActivity("Failed to exit EditBookActivity", MyBookViewActivity.class);
        assertTrue(solo.searchText("Title EDITS"));
        assertTrue(solo.searchText("Author EDITS"));
        assertTrue(solo.searchText("desc EDITS"));
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Failed to exit MyBookViewActivity", MainActivity.class);

        // Delete Book
        solo.clickOnText("Title EDITS");
        solo.assertCurrentActivity("Failed to exit MyBookViewActivity", MyBookViewActivity.class);
        solo.clickOnButton("Remove");

        solo.assertCurrentActivity("Failed to exit MyBookViewActivity", MainActivity.class);
        assertFalse(solo.searchText("Title TEST", true));

    }

    // Scrolls to the bottom of the page
    public void scrollDown(){
        int screenWidth = rule.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = rule.getActivity().getWindowManager().getDefaultDisplay().getHeight();

        int fromX, toX, fromY, toY = 0,stepCount=1;

        // Scroll Down // Drag Up
        fromX = screenWidth/2;
        toX = screenWidth/2;
        fromY = (screenHeight/2) + (screenHeight/3);
        toY = (screenHeight/2) - (screenHeight/3);

        solo.drag(fromX, toX, fromY, toY, stepCount);
    }
}
