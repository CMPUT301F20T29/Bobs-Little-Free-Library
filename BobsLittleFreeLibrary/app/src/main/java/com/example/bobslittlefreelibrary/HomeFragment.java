package com.example.bobslittlefreelibrary;

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

import com.example.bobslittlefreelibrary.utils.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This fragment is manages all the references and interactions on the home screen.
 *
 * TODO: Figure out final direction for the Requests Overview, setup real functionality for other buttons
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference bookCollectionRef = db.collection("books");

    // Variables
    ArrayList<Book> listOfBooks;
    TableLayout latestBooks;
    TableLayout requestsOverview;
    FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TEMP", "Home Fragment view has been created");
        // Setup references to UI elements
        Button searchButton = getView().findViewById(R.id.home_search_button); // getView() cannot be called in onCreate() since the view isn't inflated yet (onCreate --> onCreateView() --> onActivityCreated()
        Button profileButton = getView().findViewById(R.id.home_user_profile_button);
        Button quickScanButton = getView().findViewById(R.id.home_quick_scan_button);
        latestBooks = getView().findViewById(R.id.latest_books_view);
        requestsOverview = getView().findViewById(R.id.requests_overview_display);

        searchButton.setOnClickListener(v -> {
            Log.d("TEMP", "Search Button Pressed");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        profileButton.setOnClickListener(v -> Log.d("TEMP", "User Profile Button Pressed"));
        quickScanButton.setOnClickListener(v -> Log.d("TEMP", "Quick Scan Button Pressed"));

        // Initialize Latest Books and setup listeners for ImageButtons
        listOfBooks = new ArrayList<>();

        bookCollectionRef
                .orderBy("dateAdded", Query.Direction.DESCENDING).limit(6)
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

//        if (user.getUid().equals(currentBook.getOwnerID())) {
        if (true) { // to test edit book 
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                intent.putExtra("BOOK_ID", document.getId());
                intent.putExtra("BOOK", currentBook);  // Send book to be displayed in book view activity
                startActivity(intent);
            });
        } else {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            });
        }
    }
}
