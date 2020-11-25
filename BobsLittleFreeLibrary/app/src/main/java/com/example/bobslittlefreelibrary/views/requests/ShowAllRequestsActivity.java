package com.example.bobslittlefreelibrary.views.requests;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.CustomRequestsAdapter;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.views.books.EditBookActivity;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity shows all of the current requests for a single Book object.
 *
 * It requires a Book object to be passed in through intents.
 * */
public class ShowAllRequestsActivity  extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CustomRequestsAdapter requestsAdapter;
    private ArrayList<Request> listOfRequests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_requests);

        // setting variables needed
        ListView requestsList = findViewById(R.id.show_all_requests_list);
        listOfRequests = new ArrayList<>();
        requestsAdapter = new CustomRequestsAdapter(this, listOfRequests, false);
        requestsList.setAdapter(requestsAdapter);
        Button backButton = findViewById(R.id.show_all_requests_back_button);

        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("BOOK");

        // query all the received requests, add them to the list and then make an adapter
        db.collection("requests")
                .whereEqualTo("bookRequestedID", book.getBookID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                listOfRequests.add(document.toObject(Request.class));
                                requestsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAllRequestsActivity.this, MyBookViewActivity.class);
                intent.putExtra("BOOK", book);  // Send book to be displayed in book view activity
                startActivity(intent);
                finish();
            }
        });
    }
}
