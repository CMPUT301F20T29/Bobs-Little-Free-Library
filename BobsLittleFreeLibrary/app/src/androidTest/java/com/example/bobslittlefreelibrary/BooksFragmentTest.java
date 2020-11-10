package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BooksFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.enterText(0, "kobe@rip.com");
        solo.enterText(1, "password");
        solo.clickOnButton("Login");
        solo.clickOnView(solo.getView(R.id.books_page));
        assert(solo.searchText("Books"));
    }

    @Test
    public void listObjectTest(){
        solo.clickInList(0, 0);
        assert(solo.searchText("Author"));
        solo.clickOnButton(0);
        assert(solo.searchText("Books"));

    }

    @Test
    public void clickFAB(){
        solo.clickOnView(solo.getView(R.id.add_Item));
        assert(solo.searchText("Add Book"));
        solo.clickOnButton(0);
        assert(solo.searchText("Books"));

    }



    @Test
    public void filterTest(){
        //TODO

    }





}
