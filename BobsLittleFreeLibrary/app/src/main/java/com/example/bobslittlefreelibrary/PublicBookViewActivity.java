package com.example.bobslittlefreelibrary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by another User
 * TODO:
 *
 * */
public class PublicBookViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_book_view);
    }
}
