package com.example.bobslittlefreelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
/**
 * This fragment is manages all the references and interactions on the home screen.
 *
 * TODO: Figure out final direction for the Requests Overview, finish initializing and setting up listeners for the ImageButtons, firestore I/O, setup real functionality for other buttons
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
        Log.d("TEMP", "Home Fragment view has been created");
        // Setup references to UI elements
        Button searchButton = getView().findViewById(R.id.home_search_button); // getView() cannot be called in onCreate() since the view isn't inflated yet (onCreate --> onCreateView() --> onActivityCreated()
        Button profileButton = getView().findViewById(R.id.home_user_profile_button);
        Button quickScanButton = getView().findViewById(R.id.home_quick_scan_button);
        TableLayout latestBooks = getView().findViewById(R.id.latest_books_view);
        TableLayout requestsOverview = getView().findViewById(R.id.requests_overview_display);

        // Setup onClick listeners for buttons, using lambda expressions because android studio was like use these instead of 'new View.OnClickListener()'
        // might have to change back if we want to do more than one line of code, but these buttons just start another activity so I think it's fine.
        searchButton.setOnClickListener(v -> Log.d("TEMP", "Search Button Pressed"));
        profileButton.setOnClickListener(v -> Log.d("TEMP", "User Profile Button Pressed"));
        quickScanButton.setOnClickListener(v -> Log.d("TEMP", "Quick Scan Button Pressed"));

        // Initialize Latest Books and setup listeners for ImageButtons
        // code goes here to pull from database
        // Code below is just testing code pre-firestore implementation

        // setup list of books, probably like 8 books, and make sure they have a date attribute
        // select the 6 books with the latest add time, and set their images as the images for the imagebuttons

        // Put this code below into a for loop
        TableRow rowOne = (TableRow) latestBooks.getChildAt(0);
        ImageButton button = (ImageButton) rowOne.getChildAt(0);
        button.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
        button.setOnClickListener(v -> Log.d("TEMP", "Image Button Pressed")); // this will also send to another activity so I think using lambda expression is fine

        // Initialize Requests Overview

    }

    @Override
    public void onPause() {
        // Is called when the user switches away from the fragment
        super.onPause();
        Log.d("TEMP", "Home Fragment is paused and the view will be deleted");
    }
}
