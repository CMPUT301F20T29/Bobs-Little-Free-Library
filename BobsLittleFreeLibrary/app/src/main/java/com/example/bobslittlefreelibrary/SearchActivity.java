package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

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

        searchOptions();
        setupData();
        setupList();
    }

    private void searchOptions() {
        SearchView searchView = (SearchView) findViewById(R.id.bookSearchBar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }


            // Mathces books with the containing keyword as the user types
            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<Book> filteredBooks = new ArrayList<Book>();

                // going through all the books available in the populated list and finding the ones that match the search query

                for (Book book: searchBookList) {
                    if(book.getTitle().toLowerCase().contains(s.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                }

                BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, filteredBooks);
                listView.setAdapter(adapter);

                return false;
            }
        });
    }

    /**
     * This method is setting up dummy books to test the functionality
     */
    private void setupData() {

        Book book1 = new Book("Test Book 1", "Test Author 1", "1-56619-909-3", "Nice", "123", "available");
        searchBookList.add(book1);

        Book book2 = new Book("Test Title 2", "Test Author 2", "1-56619-909-4", "Cool", "234", "available");
        searchBookList.add(book2);

        Book book3 = new Book("Test Title 3", "Test Author 3", "1-56619-909-5", "Wow", "345", "available");
        searchBookList.add(book3);

        Book book4 = new Book("Different Title 1", "Test Author 4", "1-56619-909-6", "Wow", "456", "not available");
        searchBookList.add(book4);

        Book book5 = new Book("Different Title 2", "Test Author 5", "1-56619-909-7", "Wow", "567", "not available");
        searchBookList.add(book5);

        Book book6 = new Book("Different Title 3", "Test Author 6", "1-56619-909-9", "Wow", "567", "not available");
        searchBookList.add(book6);

        Book book7 = new Book("Chess Rules 1", "Test Author 7", "1-56619-909-0", "Wow", "567", "not available");
        searchBookList.add(book7);

        Book book8 = new Book("Chess Rules 2", "Test Author 7", "1-56619-909-1", "Wow", "567", "available");
        searchBookList.add(book8);

        Book book9 = new Book("Chess Rules 3", "Test Author 7", "1-56619-909-1", "I dont like this book", "567", "available");
        searchBookList.add(book9);




    }


    private void setupList() {

        listView = (ListView) findViewById(R.id.bookSearchListView);

        // make reference to the BookAdapter that we created
        BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, searchBookList);
        listView.setAdapter(adapter);

    }
}