package com.example.bobslittlefreelibrary.views.requests;


import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.CustomRequestsAdapter;
import com.example.bobslittlefreelibrary.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class ShowAllRequestsActivity  extends AppCompatActivity {

    private CustomRequestsAdapter sentRequestsAdapter;
    private ArrayList<Request> sentRequestList;
    private CustomRequestsAdapter receivedRequestsAdapter;
    private ArrayList<Request> receivedRequestList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_requests);

        // setting variables needed
        ListView requestsList = findViewById(R.id.requests_list);
        sentRequestList = new ArrayList<>();
        receivedRequestList = new ArrayList<>();
        receivedRequestsAdapter = new CustomRequestsAdapter(this, receivedRequestList, false);
        sentRequestsAdapter = new CustomRequestsAdapter(this, sentRequestList, true);



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

                                receivedRequestList.add(document.toObject(Request.class));
                                receivedRequestsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
