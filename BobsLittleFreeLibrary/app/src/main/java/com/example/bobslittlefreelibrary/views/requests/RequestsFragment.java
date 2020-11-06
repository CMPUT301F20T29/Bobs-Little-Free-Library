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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.CustomRequestsAdapter;
import com.example.bobslittlefreelibrary.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    private ListView requestsList;
    private Context context;
    private CustomRequestsAdapter sentRequestsAdapter;
    private ArrayList<Request> sentRequestList;
    private CustomRequestsAdapter receivedRequestsAdapter;
    private ArrayList<Request> receivedRequestList;
    private int tabPosition = 0;

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
        sentRequestList = new ArrayList<>();
        receivedRequestList = new ArrayList<>();
        receivedRequestsAdapter = new CustomRequestsAdapter(context, receivedRequestList, false);
        sentRequestsAdapter = new CustomRequestsAdapter(context, sentRequestList, true);

        // Create the top tabs when the fragment is created
        tabLayout.addTab(tabLayout.newTab().setText("Received"));
        tabLayout.addTab(tabLayout.newTab().setText("Sent"));

        // initialize the user and get the user's id and get the database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // query all the received requests, add them to the list and then make an adapter
        db.collection("requests")
                .whereEqualTo("reqReceiverID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TEMP", document.getId() + " => " + document.getData());
                                receivedRequestList.add(document.toObject(Request.class));
                                receivedRequestsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // query all the sent requests, add them to the list and then make an adapter
        db.collection("requests")
                .whereEqualTo("reqSenderID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TEMP", document.getId() + " => " + document.getData());
                                sentRequestList.add(document.toObject(Request.class));
                                sentRequestsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // display the received tab
        requestsList.setAdapter(receivedRequestsAdapter);

        // set a tab on click listener to know when the tabs have been switched & methods to handle
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tabLayout.getSelectedTabPosition();

                if (tabPosition == 0) {
                    // Received requests tab
                    requestsList.setAdapter(receivedRequestsAdapter);
                } else {
                    // Sent requests tab
                    requestsList.setAdapter(sentRequestsAdapter);
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
                    intent.putExtra("REQUEST", receivedRequestList.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SentRequestActivity.class);
                    intent.putExtra("REQUEST", sentRequestList.get(position));
                    startActivity(intent);
                }
            }
        });

    }
}
