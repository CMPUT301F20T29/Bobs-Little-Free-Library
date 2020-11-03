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

        setupData();
        setupList();
    }


    private void setupData() {

        Book book1 = new Book("Test Title 1", "Test Author 1", "1-56619-909-3", "Nice", "123", "available");
        searchBookList.add(book1);

        Book book2 = new Book("Test Title 2", "Test Author 2", "1-56619-909-4", "Cool", "234", "available");
        searchBookList.add(book2);

        Book book3 = new Book("Test Title 3", "Test Author 3", "1-56619-909-5", "Wow", "345", "available");
        searchBookList.add(book3);





    }


    private void setupList() {

        listView = (ListView) findViewById(R.id.bookSearchListView);

        // make reference to the BookAdapter that we created
        BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, searchBookList);
        listView.setAdapter(adapter);

    }
}