package com.example.bobslittlefreelibrary;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.NotificationsActivity;
import com.example.bobslittlefreelibrary.views.SearchActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is a test class that tests the functionality of the tab system used in MainActivity.
 * The tests use Robotium and ActivityTestRule (which is depreciated). Maybe consider using ActivityScenarioRule and see if robotium works with it?
 *
 * */

public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates the solo instance
     * @throws Exception if setup cannot be completed.
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
     * This test checks if the tab system works.
     * */
    @Test
    public void checkSwitchBetweenFragments() {
        solo.assertCurrentActivity("Started on the wrong Activity", MainActivity.class);
        // Switch to BooksFragment
        solo.clickOnView(solo.getView(R.id.books_page));
        assertTrue(solo.searchText("Bookshelf"));
        // Switch to RequestsFragment
        solo.clickOnView(solo.getView(R.id.requests_page));
        assertTrue(solo.searchText("Requests"));
        // Switch back to HomeFragment
        solo.clickOnView(solo.getView(R.id.home_page));
        assertTrue(solo.searchText("Latest Books"));
    }

    /**
     * This test checks if the search button in the Home fragment works
     * */
    @Test
    public void goToSearchActivity() {
        solo.clickOnView(solo.getView(R.id.home_search_button));
        solo.assertCurrentActivity("Did not go to SearchActivity", SearchActivity.class);
    }

    /**
     * This test checks if the search button in the Home fragment works
     * */
    @Test
    public void goToNotificationsActivity() {
        solo.clickOnView(solo.getView(R.id.notifs_card));
        solo.assertCurrentActivity("Did not go to NotificationsActivity", NotificationsActivity.class);
    }

    /**
     * This test checks if the profile button Home fragment works
     * */
    @Test
    public void goToMyProfileViewActivity() {
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.home_user_profile_button));
        solo.assertCurrentActivity("Did not go to MyProfileViewActivity", MyProfileViewActivity.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception if the currently opened activities cannot be closed.
     * */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
