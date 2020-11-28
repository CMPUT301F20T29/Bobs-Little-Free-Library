package com.example.bobslittlefreelibrary;


import android.content.ComponentName;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.example.bobslittlefreelibrary.views.users.SignupActivity;
import com.robotium.solo.Solo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {

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
        solo.clickOnView(solo.getView(R.id.signUpBtn));
    }

    /**
     * Checks for fails without touching Places API
     * Places API is tested manually along with successful sign up
     */
    @Test
    public void checkSignupFails() {
        // check if user's own profile is accessible and displayed
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
        solo.clickOnView(solo.getView(R.id.signup));
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
        solo.enterText(0,"asdf");
        solo.enterText(1,"aaaa");
        solo.enterText(2,"as1234");
        solo.enterText(3,"as1111");
        solo.clickOnView(solo.getView(R.id.signup));
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.enterText(0,"asdf@yahoo.ca");
        solo.enterText(1,"aaaa");
        solo.enterText(2,"as1234");
        solo.enterText(3,"as1234");
        solo.clickOnView(solo.getView(R.id.signup));
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
    }
}
