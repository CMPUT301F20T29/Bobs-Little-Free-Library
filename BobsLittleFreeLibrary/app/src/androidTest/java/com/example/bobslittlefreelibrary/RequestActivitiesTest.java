package com.example.bobslittlefreelibrary;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.requests.ReceivedRequestActivity;
import com.example.bobslittlefreelibrary.views.requests.SentRequestActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.android.material.chip.Chip;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestActivitiesTest {

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
    public void buttonsTest(){
        // ReceivedRequestActivity button tests
        solo.clickOnText("Diary of a Wimpy Kid");
        solo.assertCurrentActivity("Failed to enter ReceivedRequestActivity", ReceivedRequestActivity.class);
        solo.clickOnText("ReqTest2");
        solo.assertCurrentActivity("Failed to enter PublicProfileViewActivity", PublicProfileViewActivity.class);
        assertTrue(solo.searchText("ReqTest2"));
        solo.clickOnText("Back");
        solo.clickOnText("Diary of a Wimpy Kid");
        solo.assertCurrentActivity("Failed to enter MyBookViewActivity", MyBookViewActivity.class);
        assertTrue(solo.searchText("Diary of a Wimpy Kid"));
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        solo.assertCurrentActivity("Failed to enter MainActivity", MainActivity.class);

        // SentRequestActivity button tests
        solo.clickOnText("Sent");
        solo.clickOnText("The Sea of Monsters");
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        solo.clickOnText("ReqTest2");
        solo.assertCurrentActivity("Failed to enter PublicProfileViewActivity", PublicProfileViewActivity.class);
        assertTrue(solo.searchText("ReqTest2"));
        solo.clickOnText("Back");
        solo.clickOnText("The Sea of Monsters");
        solo.assertCurrentActivity("Failed to enter PublicBookViewActivity", PublicBookViewActivity.class);
        assertTrue(solo.searchText("The Sea of Monsters"));
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        solo.assertCurrentActivity("Failed to enter MainActivity", MainActivity.class);
    }

    @Test
    public void requestAndDeleteTest() {
        solo.clickOnView(solo.getView(R.id.home_page));
        solo.clickOnView(solo.getView(R.id.home_search_button));
        solo.enterText(0, "The Book of Tests");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.bookSearchBar));
        solo.sleep(1000);
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(1000);
        solo.clickInList(0);
        solo.clickOnText("Request Book");
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.requests_page));
        solo.clickOnText("Sent");
        solo.clickOnText("The Book of Tests");
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        solo.sleep(1000);
        solo.clickOnText("Delete Request");
        solo.clickOnText("Sent");
        assertFalse(solo.searchText("The Book of Tests"));
    }

    @Test
    public void requestNotAcceptedTest() {
        // ReceivedRequest test first
        solo.clickOnText("Diary of a Wimpy Kid");
        solo.assertCurrentActivity("Failed to enter ReceivedRequestActivity", ReceivedRequestActivity.class);
        assertTrue(solo.searchText("Accept"));
        assertTrue(solo.searchText("Decline"));
        assertTrue(solo.searchText("Select Location"));
        assertTrue(solo.searchText("Request Not Accepted"));
        solo.clickOnText("Back");

        // SentRequest test
        solo.clickOnText("Sent");
        solo.clickOnText("The Sea of Monsters");
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        assertTrue(solo.searchText("Delete Request"));
        assertTrue(solo.searchText("Request Not Accepted"));
    }

    @Test
    public void requestAcceptedTest() {
        // Need this chip to click
        Chip acceptedChip = (Chip) solo.getView(R.id.filterAcceptedButton);
        //ReceiveRequest test first
        solo.clickOnText("FILTER");
        solo.clickOnView(acceptedChip);
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter ReceivedRequestActivity", ReceivedRequestActivity.class);
        assertTrue(solo.searchText("View Location"));
        assertTrue(solo.searchText("Accepted"));
        solo.clickOnText("Back");

        // SentRequest test
        solo.clickOnText("Sent");
        solo.clickOnText("FILTER");
        solo.clickOnView(acceptedChip);
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        assertTrue(solo.searchText("View Location"));
        assertTrue(solo.searchText("Accepted"));
    }

    @Test
    public void requestExchangedTest() {
        //ReceiveRequest test first
        solo.clickOnText("FILTER");
        solo.clickOnText("Exchanged");
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter ReceivedRequestActivity", ReceivedRequestActivity.class);
        assertTrue(solo.searchText("Borrowed"));
        solo.clickOnText("Back");

        // SentRequest test
        solo.clickOnText("Sent");
        solo.clickOnText("FILTER");
        solo.clickOnText("Exchanged");
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        assertTrue(solo.searchText("Select Location"));
        assertTrue(solo.searchText("Borrowed"));
        assertTrue(solo.searchText("Request to Return"));
    }

    @Test
    public void requestReturnsTest() {
        //ReceiveRequest test first
        solo.clickOnText("FILTER");
        solo.clickOnText("Returns");
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter ReceivedRequestActivity", ReceivedRequestActivity.class);
        assertTrue(solo.searchText("View Location"));
        assertTrue(solo.searchText("Returning"));
        solo.clickOnText("Back");

        // SentRequest test
        solo.clickOnText("Sent");
        solo.clickOnText("FILTER");
        solo.clickOnText("Returns");
        solo.clickInList(0);
        solo.assertCurrentActivity("Failed to enter SentRequestActivity", SentRequestActivity.class);
        assertTrue(solo.searchText("View Location"));
        assertTrue(solo.searchText("Returning"));
    }
}
