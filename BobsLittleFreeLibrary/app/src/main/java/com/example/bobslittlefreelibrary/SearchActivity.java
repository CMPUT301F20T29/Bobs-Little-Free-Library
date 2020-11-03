package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // setting up the ArrayList
    public static ArrayList<Book> searchBookList = new ArrayList<Book>();

    // making a reference to the listView
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    private void setupData() {

    }


    private void setupList() {

    }
}