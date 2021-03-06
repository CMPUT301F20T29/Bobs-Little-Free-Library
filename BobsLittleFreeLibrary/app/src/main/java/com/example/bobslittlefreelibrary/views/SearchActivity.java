package com.example.bobslittlefreelibrary.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentHostCallback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.BookAdapter;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;


// Ideas for this Activity have been taken from Youtube Tutorials by the channel "Code with Cal"
// Youtube Channel Link: https://www.youtube.com/channel/UCDTKfxFIFmMnR-u_HodZGjA

public class SearchActivity extends AppCompatActivity {

    // setting up the ArrayList
    public static ArrayList<Book> searchBookList = new ArrayList<Book>();

    // setting up the ID list for book
    public static ArrayList<String> searchBookIDList = new ArrayList<>();

    // making a reference to the listView
    private ListView listView;

    // setting up what the current filter choice of the user is, all books by default
    private String currentFilter = "all";

    // holds what currently is being searched right now
    private String currentSearchQuery = "";

    // searchView holds the SearchView from our Search Activity
    private SearchView searchView;

    // filter button
    private Button filterButton;

    // Firebase items
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    // layouts for the chip group
    private ChipGroup chipGroup;

    // keeps track of whether the filter options are shown or hidden at the specific moment
    boolean filterHidden = true;

    // filter checks
    boolean allFilterSelected  = false;
    boolean availableFilterSelected = false;
    boolean requestedFilterSelected = false;
    boolean acceptedFilterSelected  = false;
    boolean borrowedFilterSelected = false;


    // Methods that run on creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupData();
        searchOptions();
        setLayoutsAndButtons();
        setUpOnClickListener();
        hideFilters();



    }


    // This method sets the filter button and the layouts to its corresponding ID in the XML file
    private void setLayoutsAndButtons() {
        filterButton = (Button) findViewById(R.id.filterButton);
        chipGroup = (ChipGroup) findViewById(R.id.filterChips);

        //filterLayout1 = (LinearLayout) findViewById(R.id.firstLinearLayout);
        //filterLayout2 = (LinearLayout) findViewById(R.id.secondLinearLayout);
        //filterLayout3 = (LinearLayout) findViewById(R.id.thirdLinearLayout);
    }


    // This method hides the filters and changes the text of button to filter
    // This is when the user clicks hide to drop off the filters from show
    private void hideFilters() {

        chipGroup.setVisibility(View.GONE);
        //filterLayout1.setVisibility(View.GONE);
        //filterLayout2.setVisibility(View.GONE);
        //filterLayout3.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    // This method shows the filters and changes the text of the button to hide
    // This is when the user clicks to display the new changes
    private void showFilters() {
        chipGroup.setVisibility(View.VISIBLE);
        //filterLayout1.setVisibility(View.VISIBLE);
        //filterLayout2.setVisibility(View.VISIBLE);
        //filterLayout3.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }



    // This method searches for the items of ListView based on what is being typed in the Search Bar of SearchView
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
                    if(book.getTitle().toLowerCase().contains(s.toLowerCase()) || book.getDescription().toLowerCase().contains(s.toLowerCase())) {

                        // if the user toggles to all, then just care about what\s being searched
                        if (currentFilter.equals("all")) {

                            filteredBooks.add(book);

                        } else {
                            // else if some other filter is selected, keep in mind the filter as well as the search query
                            if(book.getStatus().toLowerCase().contains(currentFilter)) {
                                filteredBooks.add(book);
                            }
                        }
                    }
                }

                BookAdapter adapter = new BookAdapter(getApplicationContext(), filteredBooks);
                listView.setAdapter(adapter);


                return false;
            }
        });
    }

    /**
     * This method is setting up dummy books to test the functionality
     */
    private void setupData() {


        db.collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    searchBookList.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Log.d(TAG, document.getId() + " => " +document.getData());
                        Book book = document.toObject(Book.class);
                        if (book.getStatus().toLowerCase().contains("available")) {
                            searchBookList.add(book);
                            searchBookIDList.add(document.getId());
                        } else if (book.getStatus().toLowerCase().contains("requested")) {
                            searchBookList.add(book);
                            searchBookIDList.add(document.getId());
                        }
                    }

                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        listView = (ListView) findViewById(R.id.bookSearchListView);

        // make reference to the BookAdapter that we created
        BookAdapter adapter = new BookAdapter(getApplicationContext(), searchBookList);
        listView.setAdapter(adapter);





        /*

        This was test data that was used before firebase instantiation

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

        */


    }

    // Method for rinsing out the accepted and borrowed books
    /*
    private void rinse() {
        ArrayList<Book> rinsedList = new ArrayList<Book>();
        String availableStatus = "available";
        String requestedStatus = "requested";
        for (Book book: searchBookList) {
            if (book.getStatus().toLowerCase().contains(availableStatus)) {
                rinsedList.add(book);
            }
            else if (book.getStatus().toLowerCase().contains(requestedStatus)) {
                rinsedList.add(book);
            }
        }

        BookAdapter adapter = new BookAdapter(getApplicationContext(), rinsedList);
        listView.setAdapter(adapter);


    }
     */





    // This method filters the list of the book based on the filter status - Available, Borrowed, Requested etc.
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

        BookAdapter adapter = new BookAdapter(getApplicationContext(), filteredBooks);
        listView.setAdapter(adapter);
    }

    // all books are displayed
    public void allFilterPressed(View view) {

        // when all is clicked, all the other filters should be cleared to all since it also involves deselection
        availableFilterSelected=false;
        requestedFilterSelected=false;
        acceptedFilterSelected=false;
        borrowedFilterSelected=false;

        currentFilter = "all";
        searchView.setQuery("", false);
        searchView.clearFocus();

        BookAdapter adapter = new BookAdapter(getApplicationContext(), searchBookList);
        listView.setAdapter(adapter);

    }

    // looking for books with available status
    public void availableFilterPressed(View view) {
        if (availableFilterSelected == false) {
            filterBookList("available");

            // toggle the boolean to true
            availableFilterSelected = true;

            // other filters will be toggled to false
            requestedFilterSelected = false;
            acceptedFilterSelected = false;
            borrowedFilterSelected = false; 
        } else {
            // all filter pressed actions
            allFilterPressed(view);
        }

    }

    // looking for books with requested status
    public void requestedFilterPressed(View view) {
        if (requestedFilterSelected == false) {
            filterBookList("requested");

            // toggle the boolean to true
            requestedFilterSelected = true;

            // other filters will be toggled to false
            availableFilterSelected = false;
            acceptedFilterSelected = false;
            borrowedFilterSelected  = false;
        } else {
            // all filter pressed actions
            allFilterPressed(view);
        }


    }

    // looking for books with accepted status
    public void acceptedFilterPressed(View view) {
        if (acceptedFilterSelected == false) {
            filterBookList("accepted");

            // toggle the boolean to true
            acceptedFilterSelected  = true;

            // other filters will be toggled to false
            availableFilterSelected = false;
            requestedFilterSelected = false;
            borrowedFilterSelected  = false;
        } else {
            // all filter pressed  actions
            allFilterPressed(view);
        }


    }

    // looking for books with borrowed status
    public void borrowedFilterPressed(View view) {
        if (borrowedFilterSelected  == false) {
            filterBookList("borrowed");

            // toggle the boolean to true
            borrowedFilterSelected = true;

            // other filtered will be toggled to false
            availableFilterSelected = false;
            requestedFilterSelected  = false;
            acceptedFilterSelected = false;
        } else {
            // all filter pressed actions
            allFilterPressed(view);
        }


    }

    // stop this activity and return the previous activity on stack when the back button is pressed
    public void backButtonPressed(View view) {
        // fixes the reopening bug for now
        searchBookList.clear();

        finish();
    }


    // display filters if filter button is pressed
    // hide filters if it is pressed again

    public void filterButtonPressed(View view) {

        if (filterHidden == true) {
            filterHidden = false;
            showFilters();
        } else {
            filterHidden = true;
            hideFilters();
        }

    }



    // On click listener for items
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book selectedBook = (Book) (listView.getItemAtPosition(position));

                if (user.getUid().equals(selectedBook.getOwnerID())) {
                    Intent intent = new Intent(SearchActivity.this, MyBookViewActivity.class);
                    intent.putExtra("BOOK", selectedBook);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SearchActivity.this, PublicBookViewActivity.class);
                    intent.putExtra("BOOK", selectedBook);
                    startActivity(intent);

                }


            }
        });
    }


}