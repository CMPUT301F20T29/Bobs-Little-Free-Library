package com.example.bobslittlefreelibrary;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is a test class that tests the functionality and UI of the 3 main tab (fragments) shown in MainActivity.
 * The tests use Robotium and ActivityTestRule (which is depreciated). Maybe consider using ActivityScenarioRule and see if robotium works with it?
 *
 * TODO: Make tests for:
 *
 *
 * */

public class MainActivityTest {

    private Solo solo;

    /**
     *
     * */
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates the solo instance
     * @throws Exception
     * */
    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        // TODO: change it so that we start at LoginActivity, and then sign in, and then get to MainActivity where we begin the tests
    }

    /**
     * Gets the Activity
     * @throws Exception
     * */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * This test checks if fragment_container is initialized properly.
     * */
    @Test
    public void checkStartHomeFragment() {
        solo.assertCurrentActivity("Started on the wrong Activity", MainActivity.class);
        assertTrue(solo.searchText("Latest Books"));
    }

    /**
     * This test checks if the tab system works.
     * */
    @Test
    public void checkSwitchBetweenFragments() {
        solo.assertCurrentActivity("Started on the wrong Activity", MainActivity.class);
        // Switch to BooksFragment
        solo.clickOnView(solo.getView(R.id.books_page));
        assertTrue(solo.searchText("My Books"));
        // Switch to RequestsFragment
        solo.clickOnView(solo.getView(R.id.requests_page));
        assertTrue(solo.searchText("Tab 1"));
        // Switch back to HomeFragment
        solo.clickOnView(solo.getView(R.id.home_page));
        assertTrue(solo.searchText("Requests Overview"));
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
