package com.example.bobslittlefreelibrary.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.ScanFragment;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This fragment manages all the references and interactions on the home screen.
 *
 * TODO:
 *     - EXTRA: turn latestBooks viewpager into a carousel
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

    // Class Variables
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private ArrayList<Book> listOfBooks;
    private TextView numberOfNotifs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        listOfBooks = new ArrayList<>();
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

        // Setup references to UI elements
        ImageButton searchButton = getView().findViewById(R.id.home_search_button); // getView() cannot be called in onCreate() since the view isn't inflated yet (onCreate --> onCreateView() --> onActivityCreated()
        ImageButton profileButton = getView().findViewById(R.id.home_user_profile_button);
        Button quickScanButton = getView().findViewById(R.id.home_quick_scan_button);
        CardView notificationCard = getView().findViewById(R.id.notifs_card);
        numberOfNotifs = getView().findViewById(R.id.num_of_notifs);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = getView().findViewById(R.id.home_view_pager);
        pagerAdapter = new LatestBooksPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);

        // Get number of notifications
        db.collection("notifications").whereEqualTo("userID", user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                numberOfNotifs.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });

        // Query for User document to convert into object to pass to profile view (if it is pressed)
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        User userObject = document.toObject(User.class);
                        profileButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), MyProfileViewActivity.class);
                            intent.putExtra("USER", userObject);
                            startActivity(intent);
                        });
                    }
                });

        // Setup listeners
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        quickScanButton.setOnClickListener(v -> {
            new ScanFragment().show(getActivity().getSupportFragmentManager(), "SCAN");
        });

        // Query for latest books and add them to listOfBooks
        db.collection("books")
                .orderBy("dateAdded", Query.Direction.ASCENDING).limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book currentBook = document.toObject(Book.class);
                                listOfBooks.add(currentBook);
                                pagerAdapter.notifyDataSetChanged();  // Update pagerAdapter
                            }
                        } else {
                            Log.d("QUERY", "Error getting documents: ", task.getException());
                        }
                    }
                });

        notificationCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationsActivity.class);
            startActivity(intent);
        });
        // Added functionality to viewpager to update the ImageView inside the fragment page when a page is selected.
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateLatestBooksImageView(position);
            }
        });
    }

    @Override
    public void onPause() {
        // Is called when the user switches away from the fragment
        super.onPause();
    }

    /**
     * This method gets a reference to the ImageView of a LatestBooksSlidePageFragment and update's the imageView
     * and sets an onClick listener for the ImageView.
     * @param position The position of the current page in the ViewPager
     * */
    private void updateLatestBooksImageView(int position) {
        Book currentBook = listOfBooks.get(position);

        ImageView imageView = viewPager.findViewWithTag(position);
        TextView titleView = viewPager.findViewWithTag(position + 6);
        CardView view = viewPager.findViewWithTag(position + 12);

        titleView.setText(currentBook.getTitle());

        String pictureURL = currentBook.getPictureURL();  // Get image url
        if (pictureURL != null) {
            new DownloadImageTask(imageView).execute(pictureURL);
        } else {
            imageView.setImageResource(R.drawable.blue_book);
        }
        // Select which book view activity pressing on the button leads to
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUid().equals(currentBook.getOwnerID())) {
                    Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                    intent.putExtra("BOOK", currentBook);  // Send book to be displayed in book view activity
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                    intent.putExtra("BOOK", currentBook);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * A simple pager adapter that represents up to 6 LatestBooksSlidePageFragments in sequence.
     * When a new fragment is created, we pass in the position of that fragment to be used when
     * setting the tag of it's UI elements.
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
