package com.example.bobslittlefreelibrary;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This is a test class that tests the functionality of the login system in LoginActivity.
 * The tests use Robotium and ActivityTestRule (which is depreciated).
 *
 * */

public class LoginActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Checks if login fails if wrong format or invalid account is entered.
     */
    @Test
    public void checkLoginFail() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "abcd");
        solo.enterText(1, "chickennuggets");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "ab34523452cd@gmail.com");
        solo.enterText(1, "chickennuggets");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "");
        solo.enterText(1, "chickennuggets");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "ab34523452cd@gmail.com");
        solo.enterText(1, "");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Checks if successful login tkaes user to correct Activity
     */
    @Test
    public void checkLoginSuccess() {
        //checks if UI updates to Main Activity on successful login
        solo.enterText(0, "hyunseo@ualberta.ca");
        solo.enterText(1, "mari4269");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


}
