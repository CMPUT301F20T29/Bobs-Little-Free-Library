package com.example.bobslittlefreelibrary;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProfileViewActivityTest {
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
        solo.enterText(0, "hyunseo@ualberta.ca");
        solo.enterText(1, "mari4269");
        solo.clickOnButton("Login");
    }

    @Test
    public void test1_checkMyProfileView() {
        // check if user's own profile is accessible and displayed
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_user_profile_button));
        solo.assertCurrentActivity("Wrong Activity", MyProfileViewActivity.class);
        assertTrue(solo.searchText("umari"));
    }

    @Test
    public void test2_editMyProfile(){
        // tests if edit profile works properly; address APi is tested manually
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_user_profile_button));
        solo.assertCurrentActivity("Wrong Activity", MyProfileViewActivity.class);
        solo.clickOnButton("Edit");
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.enterText(1,"7801231234");
        solo.enterText(2,"hello world");
        solo.clickOnButton("Save");
        assertTrue(solo.searchText("7801231234"));
        assertTrue(solo.searchText("hello world"));
        // this part clears the info for test repeats
        solo.clickOnButton("Edit");
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.enterText(1,"7806196579");
        solo.enterText(2,"I love tft :D");
        solo.clickOnButton("Save");
    }

    @Test
    public void test3_checkPublicProfileView() {
        // check if public profile view is displayed
        solo.clickOnView(solo.getView(R.id.latest_books_title));
        solo.assertCurrentActivity("Wrong Activity", PublicBookViewActivity.class);
        solo.clickOnView(solo.getView(R.id.public_book_view_owner_profile_button));
        solo.assertCurrentActivity("Wrong Activity", PublicProfileViewActivity.class);
    }
}
