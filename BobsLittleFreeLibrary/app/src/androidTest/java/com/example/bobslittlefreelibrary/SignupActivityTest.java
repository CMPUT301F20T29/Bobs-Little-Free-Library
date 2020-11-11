package com.example.bobslittlefreelibrary;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bobslittlefreelibrary.views.users.LoginActivity;
import com.example.bobslittlefreelibrary.views.users.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
        solo.clickOnButton("Sign Up");
    }

    /**
     * Checks if all sign up mistakes are caught first,
     * then check if user is taken to Main Activity after successful sign up.
     */
    @Test
    public void SignupChecklist() {
        solo.assertCurrentActivity("Wrong Activity", SignupActivity.class);
        solo.enterText(0, "");
        solo.enterText(1, "");
        solo.enterText(2, "");
        solo.enterText(3, "");
        solo.enterText(4, "");
        solo.clickOnButton("Sign Up");
        assertTrue(solo.waitForText("Email is empty\n - Password is empty\n - Username is empty\n - Address is empty", 1, 200));
        solo.enterText(0, "abcd");
        solo.enterText(1, "asdfasdfsfdsfsdfasdfsda");
        solo.enterText(2, "aa");
        solo.enterText(3, "aab");
        solo.enterText(4, "8510 111 Street");
        solo.clickOnButton("Sign Up");
        assertTrue(solo.waitForText("Email is not in correct format\n - Username is too long\n - Password is too short\n - Password confirmation is not the same as password", 1, 200));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.clearEditText(4);
        solo.enterText(0, "test123@gmail.com");
        solo.enterText(1, "sarah11");
        solo.enterText(2, "choco123");
        solo.enterText(3, "choco123");
        solo.enterText(4, "8510 111 Street");
        solo.clickOnButton("Sign Up");
        assertTrue(solo.waitForText("account already exists.",1,200));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.clearEditText(4);
        solo.enterText((EditText) solo.getView(R.id.email_signup), "tester@gmail.com");
        solo.enterText(1, "sarah11");
        solo.enterText(2, "choco123");
        solo.enterText(3, "choco123");
        solo.enterText(4, "8510 111 Street");
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Deletes the account after test is over
     */
    @After
    public void tearDown(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.delete();
    }

}
