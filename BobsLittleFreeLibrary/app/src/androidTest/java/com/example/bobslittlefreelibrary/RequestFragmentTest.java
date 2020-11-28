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

public class RequestFragmentTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.enterText(0, "testrequest1@gmail.com");
        solo.enterText(1, "password");
        solo.clickOnButton("Login");
        solo.clickOnView(solo.getView(R.id.requests_page));
        assertTrue(solo.searchText("Requests"));
    }

    @Test
    public void listObjectTest(){
        assertTrue(solo.searchText("Diary of a Wimpy Kid"));
        solo.clickOnText("Sent");
        assertTrue(solo.searchText("The Sea of Monsters"));
    }

    @Test
    public void filterTest(){
        // Test the Received requests first
        solo.clickOnText("FILTER");
        solo.clickOnText("Not Accepted");
        assertTrue(solo.searchText("Diary of a Wimpy Kid"));
        solo.clickOnText("Accepted");
        assertTrue(solo.searchText("d"));
        solo.clickOnText("Exchanged");
        assertTrue(solo.searchText("1"));
        solo.clickOnText("Returns");
        assertTrue(solo.searchText("w"));
        solo.clickOnText("All");
        assertTrue(solo.searchText("Diary of a Wimpy Kid"));
    }
}
