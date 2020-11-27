package com.example.bobslittlefreelibrary.views.requests;

/**
 * This class is the fragment for the list of requests
 * either sent or received.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.CustomRequestsAdapter;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class RequestsFragment extends Fragment {

    private ListView requestsList;
    private Context context;
    private ArrayList<Request> sentRequestsList;
    private ArrayList<Request> receivedRequestsList;
    private ArrayList<Request> currentRequestsList;
    private CustomRequestsAdapter currentAdapter;
    private int tabPosition = 0;
    private ChipGroup chipGroup;
    private boolean filterHidden = true;
    private Button filterButton;
    private Chip filterAllButton;
    private Chip filterNotAcceptedButton;
    private Chip filterAcceptedButton;
    private Chip filterExchangedButton;
    private Chip filterReturnButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.requests_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting variables needed
        ListView requestsList = view.findViewById(R.id.requests_list);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        context = this.getActivity();
        sentRequestsList = new ArrayList<>();
        receivedRequestsList = new ArrayList<>();
        currentRequestsList = new ArrayList<>();
        currentAdapter = new CustomRequestsAdapter(context, currentRequestsList, false);
        requestsList.setAdapter(currentAdapter);                                // we start with received tab and set the adapter
        chipGroup = view.findViewById(R.id.filterChips);
        filterButton = view.findViewById(R.id.filterButton);
        filterAllButton = view.findViewById(R.id.filterAllButton);
        filterNotAcceptedButton = view.findViewById(R.id.filterNotAcceptedButton);
        filterAcceptedButton = view.findViewById(R.id.filterAcceptedButton);
        filterExchangedButton = view.findViewById(R.id.filterExchangedButton);
        filterReturnButton = view.findViewById(R.id.filterReturnButton);

        // Create the top tabs when the fragment is created
        tabLayout.addTab(tabLayout.newTab().setText("Received"));
        tabLayout.addTab(tabLayout.newTab().setText("Sent"));

        // initialize the user and get the user's id and get the database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // hide the filter buttons
        hideFilters();

        // query all the received requests, add them to the list
        // add to current list too because we display received first, and notify the adapter
        db.collection("requests")
                .whereEqualTo("reqReceiverID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TEMP", document.getId() + " => " + document.getData());
                                receivedRequestsList.add(document.toObject(Request.class));
                                currentRequestsList.add(document.toObject(Request.class));
                                currentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // query all the sent requests, add them to the list
        db.collection("requests")
                .whereEqualTo("reqSenderID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TEMP", document.getId() + " => " + document.getData());
                                sentRequestsList.add(document.toObject(Request.class));
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // set a tab on click listener to know when the tabs have been switched & methods to handle
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tabLayout.getSelectedTabPosition();

                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    currentAdapter.setSentTab(false);
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        currentRequestsList.add(receivedRequestsList.get(i));
                    }
                    currentAdapter.notifyDataSetChanged();
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    currentAdapter.setSentTab(true);
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        currentRequestsList.add(sentRequestsList.get(i));
                    }
                    currentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // pass
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // pass
            }
        });

        // handling the user selecting a request
        requestsList.setClickable(true);
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Based on which tab the user is on, it will decide which activity to go to
                if (tabPosition == 0) {
                    Intent intent = new Intent(getActivity(), ReceivedRequestActivity.class);
                    intent.putExtra("REQUEST", currentRequestsList.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SentRequestActivity.class);
                    intent.putExtra("REQUEST", currentRequestsList.get(position));
                    startActivity(intent);
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterHidden == true) {
                    filterHidden = false;
                    showFilters();
                } else {
                    filterHidden = true;
                    hideFilters();
                }
            }
        });

        filterAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        currentRequestsList.add(receivedRequestsList.get(i));
                    }
                    currentAdapter.notifyDataSetChanged();
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        currentRequestsList.add(sentRequestsList.get(i));
                    }
                    currentAdapter.notifyDataSetChanged();
                }
            }
        });

        filterNotAcceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        if ((receivedRequestsList.get(i).getLongitude() == 1000.0) && (receivedRequestsList.get(i).getLatitude() == 1000.0)) {
                            currentRequestsList.add(receivedRequestsList.get(i));
                        }
                    }
                    currentAdapter.notifyDataSetChanged();
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        if ((sentRequestsList.get(i).getLongitude() == 1000.0) && (sentRequestsList.get(i).getLatitude() == 1000.0)) {
                            currentRequestsList.add(sentRequestsList.get(i));
                        }
                    }
                    currentAdapter.notifyDataSetChanged();
                }
            }
        });

        filterAcceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        Request tempRequest = receivedRequestsList.get(i);
                        if (!(tempRequest.isReturnRequest())) {
                            db.collection("books")
                                    .document(tempRequest.getBookRequestedID())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Book tempBook = documentSnapshot.toObject(Book.class);
                                            if (tempBook.getStatus().equals("Accepted")) {
                                                currentRequestsList.add(tempRequest);
                                                currentAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        Request tempRequest = sentRequestsList.get(i);
                        if (!(tempRequest.isReturnRequest())) {
                            db.collection("books")
                                    .document(tempRequest.getBookRequestedID())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Book tempBook = documentSnapshot.toObject(Book.class);
                                            if (tempBook.getStatus().equals("Accepted")) {
                                                currentRequestsList.add(tempRequest);
                                                currentAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

        filterExchangedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        Request tempRequest = receivedRequestsList.get(i);
                        if (!(tempRequest.isReturnRequest())) {
                            db.collection("books")
                                    .document(tempRequest.getBookRequestedID())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Book tempBook = documentSnapshot.toObject(Book.class);
                                            if (tempBook.getStatus().equals("Borrowed")) {
                                                currentRequestsList.add(tempRequest);
                                                currentAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        Request tempRequest = sentRequestsList.get(i);
                        if (!(tempRequest.isReturnRequest())) {
                            db.collection("books")
                                    .document(tempRequest.getBookRequestedID())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Book tempBook = documentSnapshot.toObject(Book.class);
                                            if (tempBook.getStatus().equals("Borrowed")) {
                                                currentRequestsList.add(tempRequest);
                                                currentAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

        filterReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 0) {
                    // Received requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < receivedRequestsList.size(); i++) {
                        if (receivedRequestsList.get(i).isReturnRequest()) {
                            currentRequestsList.add(receivedRequestsList.get(i));
                        }
                    }
                    currentAdapter.notifyDataSetChanged();
                } else {
                    // Sent requests tab
                    currentRequestsList.clear();
                    for (int i = 0; i < sentRequestsList.size(); i++) {
                        if (sentRequestsList.get(i).isReturnRequest()) {
                            currentRequestsList.add(sentRequestsList.get(i));
                        }
                    }
                    currentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setLastActiveTab("REQS");
    }

    // This method hides the filters and changes the text of button to filter
    // This is when the user clicks hide to drop off the filters from show
    private void hideFilters() {
        chipGroup.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    // This method shows the filters and changes the text of the button to hide
    // This is when the user clicks to display the new changes
    private void showFilters() {
        chipGroup.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }
}
