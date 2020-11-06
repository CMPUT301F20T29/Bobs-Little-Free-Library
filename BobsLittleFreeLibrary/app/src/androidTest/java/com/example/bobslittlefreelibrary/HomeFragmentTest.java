package com.example.bobslittlefreelibrary;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This is a test class that tests the functionality of the UI in HomeFragment
 * The tests use Robotium and ActivityTestRule (which is depreciated). Maybe consider using ActivityScenarioRule and see if robotium works with it?
 * TODO: make test for profile button, quick scan button, and then requests overview after its changed, alter openLatestBook() to handle both MyBookViewActivity and PublicBookViewActivity
 *
 * */
public class HomeFragmentTest {

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
        solo.enterText(0, "kvnguyen@ualberta.ca");
        solo.enterText(1, "chickennuggets");
        solo.clickOnButton("Login");
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
     * This test checks if the search button works
     * */
    @Test
    public void goToSearchActivity() {
        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Did not go to SearchActivity", SearchActivity.class);
    }

    /**
     * This test checks if the ImageButtons work
     * */
    @Test
    public void openLatestBook() {
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Did not go to PublicBookView", PublicBookViewActivity.class);
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
