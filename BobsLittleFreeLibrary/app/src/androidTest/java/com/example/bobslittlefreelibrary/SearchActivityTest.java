package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.SearchActivity;
import com.robotium.solo.Solo;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SearchActivity> rule = new ActivityTestRule<>(SearchActivity.class, true, true);


    /**
     * Runs before all tests and creates the solo instance
     * @throws Exception
     * */
    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     *
     * This test check if the text can be entered in the Search Bar properly
     */
    @Test
    public void searchBarTest() {
        solo.assertCurrentActivity("Failed Search Activity", SearchActivity.class);
        solo.enterText(0, "Test Text");
        assertTrue(solo.searchText("Test Text"));
    }

    /**
     * This Test checks the functionality of filter button
     */
    @Test
    public void filterButtonTest() {
        assertTrue(solo.searchText("FILTER"));
        solo.clickOnButton("FILTER");
        assertTrue(solo.searchText("HIDE"));
        solo.clickOnButton("HIDE");
    }

    /**
     * This test check
     * 1) if the filter button shows the appropriate filters
     * 2) if the filter chips function properly
     *
     */
    @Test
    public void filterChipsTest() {
        solo.clickOnButton("FILTER");

        assertTrue(solo.searchText("All"));
        assertTrue(solo.searchText("Available"));
        assertTrue(solo.searchText("Requested"));
        assertTrue(solo.searchText("Accepted"));
        assertTrue(solo.searchText("Borrowed"));

        // chip for All filter
        solo.clickOnButton("All");
        assertTrue(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));

        // chip for Available filter
        solo.clickOnButton("Available");
        assertTrue(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));

        // chip for Requested filter
        solo.clickOnButton("Requested");
        assertTrue(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("Borrowed"));

        // chip for Accepted filter
        solo.clickOnButton("Accepted");
        assertTrue(solo.isTextChecked("Accepted"));
        assertFalse(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Borrowed"));

        // chip for Borrowed filter
        solo.clickOnButton("Borrowed");
        assertTrue(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Accepted"));

        // click on Borrowed Again
        solo.clickOnButton("Borrowed");
        assertFalse(solo.isTextChecked("Borrowed"));
        assertFalse(solo.isTextChecked("All"));
        assertFalse(solo.isTextChecked("Requested"));
        assertFalse(solo.isTextChecked("Available"));
        assertFalse(solo.isTextChecked("Accepted"));

        // click on Hide filter
        solo.clickOnButton("HIDE");

    }



    /**
     * Closes the activity after each test
     * @throws Exception
     * */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}
