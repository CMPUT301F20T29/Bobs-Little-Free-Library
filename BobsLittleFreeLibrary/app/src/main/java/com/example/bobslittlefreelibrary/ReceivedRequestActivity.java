package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bobslittlefreelibrary.utils.DownloadImageTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                Log.d("TEMP", "Accept button pressed");
            }
        });

        declineRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Decline button pressed");
            }
        });

        bookInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "BOOK VIEW pressed");
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Borrower Profile button pressed");
            }
        });
    }
}