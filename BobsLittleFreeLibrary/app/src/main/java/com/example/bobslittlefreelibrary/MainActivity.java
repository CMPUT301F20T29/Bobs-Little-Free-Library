package com.example.bobslittlefreelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bobslittlefreelibrary.templates.BlankTemplateActivity;
import com.example.bobslittlefreelibrary.templates.FormTemplateActivity;
import com.example.bobslittlefreelibrary.templates.SomeAssetsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button formTemplateButton;
    private Button blankTemplateButton;
    private Button someAssetsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // brings up information about the user; in this case, log email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("userEmail",user.getEmail().toString());

        /*formTemplateButton = findViewById(R.id.form_template);
        blankTemplateButton = findViewById(R.id.blank_template);
        someAssetsButton = findViewById(R.id.some_assets);

        formTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormTemplateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        blankTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlankTemplateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        someAssetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SomeAssetsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });*/

        // Setup Bottom Nav view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        // Set default screen as home screen
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
}