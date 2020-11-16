package com.example.bobslittlefreelibrary.views.requests;

/**
 * This class is the activity for received requests
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReceivedRequestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Request currentRequest;
    private LinearLayout bookInfoContainer;
    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private Button userProfileButton;
    private Button acceptRequestButton;
    private Button declineRequestButton;
    private Button backButton;
    private Button mapButton;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);

        // get the request object from the intent and set userid
        Intent intent = getIntent();
        currentRequest = (Request) intent.getSerializableExtra("REQUEST");

        // getting references to the objects
        bookInfoContainer = findViewById(R.id.book_info_container);
        bookImage = findViewById(R.id.book_view_image);
        titleText = findViewById(R.id.book_view_title);
        authorText = findViewById(R.id.book_view_author);
        ISBNText = findViewById(R.id.book_view_ISBN);
        userProfileButton = findViewById(R.id.requester_profile_button);
        acceptRequestButton = findViewById(R.id.accept_request_button);
        declineRequestButton = findViewById(R.id.decline_request_button);
        backButton = findViewById(R.id.back_button);
        mapButton = findViewById(R.id.open_map_button);

        // query for the username
        db.collection("users")
                .document(currentRequest.getReqSenderID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userProfileButton.setText((String)documentSnapshot.get("username"));
                    }
                });

        // query for the book information
        db.collection("books")
                .document(currentRequest.getBookRequestedID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentBook = documentSnapshot.toObject(Book.class);
                        String pictureURL = currentRequest.getBookImageURL();
                        if (pictureURL != null) {
                            new DownloadImageTask(bookImage).execute(pictureURL);
                        } else {
                            bookImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                        }
                        titleText.setText((String)documentSnapshot.get("title"));
                        authorText.setText((String)documentSnapshot.get("author"));
                        ISBNText.setText((String)documentSnapshot.get("isbn"));
                    }
                });

        // setup buttons & book click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // REMEMBER TO ADD A TOAST FOR ACCEPTED / SNACKBAR IF IT DOESNT WORK PROPERLY LIKE LOCATION FALSE
                Log.d("TEMP", "Accept button pressed");
            }
        });

        declineRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("requests")
                        .whereEqualTo("reqReceiverID", currentRequest.getReqReceiverID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Request tempRequest = document.toObject(Request.class);
                                        if ((currentRequest.getReqSenderID().equals(tempRequest.getReqSenderID())) &&
                                                (currentRequest.getBookRequestedID().equals(tempRequest.getBookRequestedID())))
                                        {
                                            String documentID = document.getId();
                                            db.collection("requests").document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("TEMP", "DocumentSnapshot successfully deleted!");
                                                            Toast toast = Toast.makeText(getApplicationContext(), "Request Declined", Toast.LENGTH_SHORT);
                                                            toast.show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("TEMP", "Error deleting document", e);
                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    Log.d("TEMP", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        bookInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this , MyBookViewActivity.class);
                intent.putExtra("BOOK_ID", currentRequest.getBookRequestedID());
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Borrower Profile button pressed");
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this, MapsActivity.class);
                intent.putExtra("REQUESTTYPE", 0);
                startActivity(intent);
            }
        });
    }
}