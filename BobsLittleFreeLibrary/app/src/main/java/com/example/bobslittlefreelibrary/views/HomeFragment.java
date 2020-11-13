package com.example.bobslittlefreelibrary.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This fragment is manages all the references and interactions on the home screen.
 *
 * TODO:
 *     - Figure out final direction for the Requests Overview
 *     - setup functionality for profile button
 *     - fix the profile button changing size when switching between fragments
 *
 * All of the 'would-be' class variables are only local variables within onActivityCreated()
 * since values are only initialized once on this fragment. At no other time would they be called,
 * unless we plan to add other methods to reduce the size of onActivityCreated().
 *
 * This also means that we would only 'refresh' or update the Latest Books view and the Requests
 * Overview when the User opens up the app and switches back and forth between the main tabs.
 *
 * This could be changed in a future update.
 * */
public class HomeFragment extends Fragment {

    // Variables
    private FirebaseUser user;
    private FirebaseFirestore db;
    private TableLayout latestBooks;
    private Button profileButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setLastActiveTab("HOME");
        Log.d("TEMP", "Home Fragment view has been created");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Setup references to UI elements
        Button searchButton = getView().findViewById(R.id.home_search_button); // getView() cannot be called in onCreate() since the view isn't inflated yet (onCreate --> onCreateView() --> onActivityCreated()
        profileButton = getView().findViewById(R.id.home_user_profile_button);
        Button quickScanButton = getView().findViewById(R.id.home_quick_scan_button);
        latestBooks = getView().findViewById(R.id.latest_books_view);
        TableLayout requestsOverview = getView().findViewById(R.id.requests_overview_display);

        // Initialize UI
        db.collection("users").document(user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                profileButton.setText(user.getUsername());
            }
        });
        // Setup listeners
        searchButton.setOnClickListener(v -> {
            Log.d("TEMP", "Search Button Pressed");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        profileButton.setOnClickListener(v -> Log.d("TEMP", "User Profile Button Pressed"));
        quickScanButton.setOnClickListener(v -> Log.d("TEMP", "Quick Scan Button Pressed"));

        // Initialize Latest Books and setup listeners for ImageButtons
        ArrayList<Book> listOfBooks = new ArrayList<>();
        CollectionReference bookCollectionRef = db.collection("books");

        bookCollectionRef
                .orderBy("dateAdded", Query.Direction.ASCENDING).limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setupImageButton(document, i);
                                i++;
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Initialize Requests Overview

    }

    @Override
    public void onPause() {
        // Is called when the user switches away from the fragment
        super.onPause();
        Log.d("TEMP", "Home Fragment is paused and the view will be deleted");
    }

    /**
     * This method sets up the ImageButtons that show the latest books added to the database.
     * @param document The current document to pull data from (i.e. the book document that will be displayed)
     * @param i the index of the current ImageButton we are setting up.
     * */
    private void setupImageButton(QueryDocumentSnapshot document, int i) {

        Book currentBook = document.toObject(Book.class);
        ImageButton button;
        TableRow row;

        // Get correct button at (row, i%3)
        if (i < 3) {
            row = (TableRow) latestBooks.getChildAt(0);
        } else {
            row = (TableRow) latestBooks.getChildAt(1);
        }
        button = (ImageButton) row.getChildAt(i%3);

        String pictureURL = currentBook.getPictureURL();  // Get image url
        if (pictureURL != null) {
            new DownloadImageTask(button).execute(pictureURL);
        } else {
            button.setImageResource(R.drawable.blue_book);
        }
        // Select which book view activity pressing on the button leads to
        if (user.getUid().equals(currentBook.getOwnerID())) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                intent.putExtra("BOOK_ID", document.getId());
                intent.putExtra("BOOK", currentBook);  // Send book to be displayed in book view activity
                startActivity(intent);
            });
        } else {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                intent.putExtra("BOOK_ID", document.getId());
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            });
        }
    }
}