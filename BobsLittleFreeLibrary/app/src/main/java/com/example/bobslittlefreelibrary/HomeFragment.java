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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("TEMP", document.getId() + " => " + document.getData());
                                listOfBooks.add(document.toObject(Book.class));
                            }
                            setupImageButtons();
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

    private void setupImageButtons() {
        // Setup onClickListeners for all of the ImageButtons and set which book they go to
        int currentRow = 0;
        for (int i = 0; i < listOfBooks.size(); i++) {
            Book currentBook = listOfBooks.get(i);
            if (user.getUid().equals(currentBook.getOwnerID())) {
                TableRow rowOne = (TableRow) latestBooks.getChildAt(currentRow);
                ImageButton button;
                if (i < 3) {
                    button = (ImageButton) rowOne.getChildAt(i);
                } else {
                    button = (ImageButton) rowOne.getChildAt(i - 3);
                }
                if (currentBook.getPictureURL() != null) {
                    new DownloadImageTask(button).execute(currentBook.getPictureURL());
                } else {
                    button.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }
                int finalI = i;
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                    intent.putExtra("BOOK", listOfBooks.get(finalI));
                    startActivity(intent);
                });
            } else {
                TableRow rowOne = (TableRow) latestBooks.getChildAt(currentRow);
                ImageButton button;
                if (i < 3) {
                    button = (ImageButton) rowOne.getChildAt(i);
                } else {
                    button = (ImageButton) rowOne.getChildAt(i - 3);
                }
                if (currentBook.getPictureURL() != null) {
                    new DownloadImageTask(button).execute(currentBook.getPictureURL());
                } else {
                    button.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }
                int finalI1 = i;
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                    intent.putExtra("BOOK", listOfBooks.get(finalI1));
                    startActivity(intent);
                });
            }
            if (i == 2) {
                currentRow = 1;
            }
        }
    }
}
