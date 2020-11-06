package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;


// Ideas for this Activity have been taken from Youtube Tutorials by the channel "Code with Cal"
// Youtube Channel Link: https://www.youtube.com/channel/UCDTKfxFIFmMnR-u_HodZGjA

public class SearchActivity extends AppCompatActivity {

    // setting up the ArrayList
    public static ArrayList<Book> searchBookList = new ArrayList<Book>();

    // making a reference to the listView
    private ListView listView;

    // setting up what the current filter choice of the user is, all books by default
    private String currentFilter = "all";

    private String currentSearchQuery = "";

    private SearchView searchView;

    private Button filterButton;

    private LinearLayout filterLayout1;
    private LinearLayout filterLayout2;
    private LinearLayout filterLayout3;

    boolean filterHidden = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchOptions();
        setLayoutsAndButtons();
        setupData();
        setupList();
        hideFilters();
    }

    private void setLayoutsAndButtons() {
        filterButton = (Button) findViewById(R.id.filterButton);
        filterLayout1 = (LinearLayout) findViewById(R.id.firstLinearLayout);
        filterLayout2 = (LinearLayout) findViewById(R.id.secondLinearLayout);
        filterLayout3 = (LinearLayout) findViewById(R.id.thirdLinearLayout);
    }

    private void hideFilters() {
        filterLayout1.setVisibility(View.GONE);
        filterLayout2.setVisibility(View.GONE);
        filterLayout3.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    private void showFilters() {
        filterLayout1.setVisibility(View.VISIBLE);
        filterLayout2.setVisibility(View.VISIBLE);
        filterLayout3.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }

    private void searchOptions() {
        searchView = (SearchView) findViewById(R.id.bookSearchBar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }


            // Mathces books with the containing keyword as the user types
            @Override
            public boolean onQueryTextChange(String s) {

                currentSearchQuery = s;

                ArrayList<Book> filteredBooks = new ArrayList<Book>();

                // going through all the books available in the populated list and finding the ones that match the search query

                for (Book book: searchBookList) {
                    if(book.getTitle().toLowerCase().contains(s.toLowerCase())) {

                        if (currentFilter.equals("all")) {

                            filteredBooks.add(book);
                        } else {
                            if(book.getStatus().toLowerCase().contains(currentFilter)) {
                                filteredBooks.add(book);
                            }
                        }
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

        Book book1 = new Book("Test Title 1", "Test Author 1", "1-56619-909-3", "Nice", "123", "available");
        searchBookList.add(book1);

        Book book2 = new Book("Test Title 2", "Test Author 2", "1-56619-909-4", "Cool", "234", "available");
        searchBookList.add(book2);

        Book book3 = new Book("Test Title 3", "Test Author 3", "1-56619-909-5", "Wow", "345", "requested");
        searchBookList.add(book3);

        Book book4 = new Book("Different Title 1", "Test Author 4", "1-56619-909-6", "Wow", "456", "requested");
        searchBookList.add(book4);

        Book book5 = new Book("Different Title 2", "Test Author 5", "1-56619-909-7", "Wow", "567", "accepted");
        searchBookList.add(book5);

        Book book6 = new Book("Different Title 3", "Test Author 6", "1-56619-909-9", "Wow", "567", "accepted");
        searchBookList.add(book6);

        Book book7 = new Book("Chess Rules 1", "Test Author 7", "1-56619-909-0", "Wow", "567", "borrowed");
        searchBookList.add(book7);

        Book book8 = new Book("Chess Rules 2", "Test Author 7", "1-56619-909-1", "Wow", "567", "borrowed");
        searchBookList.add(book8);

        Book book9 = new Book("Chess Rules 3", "Test Author 7", "1-56619-909-1", "I dont like this book", "567", "available");
        searchBookList.add(book9);

        Book book10 = new Book("Very Nice Book", "Test Author 7", "1-56619-909-1", "I like this book", "567", "available");
        searchBookList.add(book10);




    }


    private void setupList() {

        listView = (ListView) findViewById(R.id.bookSearchListView);

        // make reference to the BookAdapter that we created
        BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, searchBookList);
        listView.setAdapter(adapter);

    }






    private void filterBookList(String bookStatus) {

        currentFilter = bookStatus;

        ArrayList<Book> filteredBooks = new ArrayList<Book>();

        for (Book book: searchBookList) {
            if(book.getStatus().toLowerCase().contains(bookStatus)) {

                // to implement search while filtering is working as well
                if (currentSearchQuery == "") {
                    filteredBooks.add(book);
                } else {
                    if(book.getTitle().toLowerCase().contains(currentSearchQuery)){
                        filteredBooks.add(book);
                    }
                }
            }

        }

        BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, filteredBooks);
        listView.setAdapter(adapter);
    }

    // all books are displayed
    public void allFilterPressed(View view) {
        currentFilter = "all";
        searchView.setQuery("", false);
        searchView.clearFocus();

        BookAdapter adapter = new BookAdapter(getApplicationContext(), 0, searchBookList);
        listView.setAdapter(adapter);

    }

    // looking for books with available status
    public void availableFilterPressed(View view) {
        filterBookList("available");
    }

    // looking for books with requested status
    public void requestedFilterPressed(View view) {
        filterBookList("requested");

    }

    // looking for books with accepted status
    public void acceptedFilterPressed(View view) {
        filterBookList("accepted");

    }

    // looking for books with borrowed status
    public void borrowedFilterPressed(View view) {
        filterBookList("borrowed");

    }

    public void backButtonPressed(View view) {
        // fixes the reopening bug for now
        searchBookList.clear();

        finish();
    }

    public void filterButtonPressed(View view) {

        if (filterHidden == true) {
            filterHidden = false;
            showFilters();
        } else {
            filterHidden = true;
            hideFilters();
        }

    }


}