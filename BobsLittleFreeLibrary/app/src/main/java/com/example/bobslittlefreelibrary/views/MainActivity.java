package com.example.bobslittlefreelibrary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.ScanFragment;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.requests.RequestsFragment;
import com.example.bobslittlefreelibrary.views.books.BooksFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity is the first activity to be launched after logging in. It houses a container for fragments and a bottom navigation bar
 * that allows the user to switch between 3 fragments: BooksFragment, HomeFragment, and RequestsFragment
 * */
public class MainActivity extends AppCompatActivity implements ScanFragment.OnFragmentInteractionListener {

    private String lastActiveTab;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // brings up information about the user; in this case, log email
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
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

    @Override
    public void onIsbnFound(String isbn) {
        // Assuming there is only one copy of the book with this ISBN in the db
        db.collection("books").whereEqualTo("isbn", isbn)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<DocumentSnapshot> documents = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                DocumentSnapshot document = documents.get(0); // Should only be one
                Book bookToShow = document.toObject(Book.class);

                // query for the request of this book

                if (bookToShow.getStatus().equals("Accepted")) {
                    // The book being scanned needs to be handed to the borrower
                    // so first the owner scans it and the borrow request is updated
                } else if (bookToShow.getStatus().equals("Borrowed")) {
                    // The book being scanned needs to be returned to the owner
                } else {
                    // Go to a book view activity based on owner
                    if (user.getUid().equals(bookToShow.getOwnerID())) {
                        Intent intent = new Intent(MainActivity.this, MyBookViewActivity.class);
                        intent.putExtra("BOOK_ID", document.getId());
                        intent.putExtra("BOOK", bookToShow);  // Send book to be displayed in book view activity
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, PublicBookViewActivity.class);
                        intent.putExtra("BOOK_ID", document.getId());
                        intent.putExtra("BOOK", bookToShow);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}