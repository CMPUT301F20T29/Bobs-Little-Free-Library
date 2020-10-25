package com.example.bobslittlefreelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bobslittlefreelibrary.templates.BlankTemplateActivity;
import com.example.bobslittlefreelibrary.templates.FormTemplateActivity;
import com.example.bobslittlefreelibrary.templates.SomeAssetsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button formTemplateButton;
    private Button blankTemplateButton;
    private Button someAssetsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        // Set default screen as home screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

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



/* code for extra template buttons
*     <Button
        android:id="@+id/form_template"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Form Template"
        style="@style/SubmitButton"
        android:layout_marginTop="100dp"
        android:layout_marginHorizontal="11dp"/>
    <Button
        android:id="@+id/blank_template"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Blank Template"
        android:layout_below="@+id/form_template"
        style="@style/SubmitButton"
        android:layout_margin="11dp"/>
    <Button
        android:id="@+id/some_assets"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Some Assets"
        android:layout_below="@+id/blank_template"
        style="@style/SubmitButton"
        android:layout_margin="11dp"/>*/