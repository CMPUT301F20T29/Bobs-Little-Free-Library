package com.example.bobslittlefreelibrary.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.views.requests.RequestsFragment;
import com.example.bobslittlefreelibrary.views.books.BooksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This activity is the first activity to be launched after logging in. It houses a container for fragments and a bottom navigation bar
 * that allows the user to switch between 3 fragments: BooksFragment, HomeFragment, and RequestsFragment
 * */
public class MainActivity extends AppCompatActivity {

    private String lastActiveTab;
    private BottomNavigationView bottomNavigationView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // brings up information about the user; in this case, log email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("userEmail",user.getEmail().toString());

        // Setup Bottom Nav view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Set default screen as home screen
        lastActiveTab = "HOME";
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    // We define this listener outside the onCreate method to customize it to our fragments and set it in the OnCreate method
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.books_page:
                        selectedFragment = new BooksFragment();
                        break;
                    case R.id.home_page:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.requests_page:
                        selectedFragment = new RequestsFragment();
                        break;
                }
                // Switch to selected fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true; // true means we select the current item, fragments would still show if this is false.
            };

    @Override
    protected void onStart() {
        super.onStart();
        switch (lastActiveTab) {
            case "DASH":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.home_page);
                break;
            case "BOOKS":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BooksFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.books_page);
                break;
            case "REQS":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RequestsFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.requests_page);
                break;
        }
    }

    /**
     * Updates the value of lastActiveTab
     * @param tabName the name of the tab to be set as last active
     * */
    public void setLastActiveTab(String tabName) {
        lastActiveTab = tabName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}