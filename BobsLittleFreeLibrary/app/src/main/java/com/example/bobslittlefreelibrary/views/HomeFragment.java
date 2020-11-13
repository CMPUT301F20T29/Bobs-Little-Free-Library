package com.example.bobslittlefreelibrary.views;

import android.app.Activity;
import android.content.Intent;
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
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
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
 *     - figure out how to get the viewpager pages to load the images
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
    private Button profileButton;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private ArrayList<Book> listOfBooks;
    private ArrayList<String> listOfBookIDS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        listOfBooks = new ArrayList<>();
        listOfBookIDS = new ArrayList<>();
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

        // Setup references to UI elements
        Button searchButton = getView().findViewById(R.id.home_search_button); // getView() cannot be called in onCreate() since the view isn't inflated yet (onCreate --> onCreateView() --> onActivityCreated()
        profileButton = getView().findViewById(R.id.home_user_profile_button);
        Button quickScanButton = getView().findViewById(R.id.home_quick_scan_button);
        TableLayout requestsOverview = getView().findViewById(R.id.requests_overview_display);
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = getView().findViewById(R.id.home_view_pager);
        pagerAdapter = new LatestBooksPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);

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
        profileButton.setOnClickListener(v -> {
            Log.d("TEMP", "User Profile Button Pressed");
            Intent intent = new Intent(getActivity(), MyProfileViewActivity.class);
            startActivity(intent);
        });
        quickScanButton.setOnClickListener(v -> Log.d("TEMP", "Quick Scan Button Pressed"));

        // Initialize Latest Books and setup listeners for ImageButtons
        CollectionReference bookCollectionRef = db.collection("books");

        bookCollectionRef
                //.whereNotEqualTo("pictureURL", null)
                //.orderBy("pictureURL")
                .orderBy("dateAdded", Query.Direction.ASCENDING).limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addBookToList(document);
                                pagerAdapter.notifyDataSetChanged();
                                Log.d("TEMP", "size of listofbooks = " + listOfBooks.size());
                                Log.d("TEMP", "size of listofbookIDs = " + listOfBookIDS.size());
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                updateLatestBooksImageView(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateLatestBooksImageView(position);
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

    private void addBookToList(QueryDocumentSnapshot document) {
        Book currentBook = document.toObject(Book.class);
        String bookID = document.getId();
        listOfBooks.add(currentBook);
        listOfBookIDS.add(bookID);
    }

    private void updateLatestBooksImageView(int position) {
        ImageView imageView;
        Book currentBook = listOfBooks.get(position);

        imageView = (ImageView)viewPager.findViewWithTag(position);
        Log.d("TEMP", "imageView = " + imageView);

        String pictureURL = currentBook.getPictureURL();  // Get image url
        if (pictureURL != null) {
            new DownloadImageTask(imageView).execute(pictureURL);
        } else {
            imageView.setImageResource(R.drawable.blue_book);
        }
        // Select which book view activity pressing on the button leads to
        if (user.getUid().equals(currentBook.getOwnerID())) {
            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                intent.putExtra("BOOK_ID", listOfBookIDS.get(position));
                intent.putExtra("BOOK", currentBook);  // Send book to be displayed in book view activity
                startActivity(intent);
            });
        } else {
            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                intent.putExtra("BOOK_ID", listOfBookIDS.get(position));
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            });
        }
    }

    /**
     * A simple pager adapter that represents up to 6 LatestBooksSlidePageFragments in sequence.
     */
    private class LatestBooksPagerAdapter extends FragmentStateAdapter {

        public LatestBooksPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            LatestBooksSlidePageFragment fragment = new LatestBooksSlidePageFragment();
            // Give position to fragment to set as tag for view
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return listOfBooks.size();
        }
    }
}
