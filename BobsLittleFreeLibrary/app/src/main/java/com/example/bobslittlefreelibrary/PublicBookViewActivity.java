package com.example.bobslittlefreelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.utils.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by another User
 * TODO: Setup button functionality, populate profile button with username and link it to UserProfileView activity
 *
 * */
public class PublicBookViewActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Book book;
    private String bookID;
    private User requester;

    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private TextView descText;
    private Button ownerProfileButton;
    private TextView bookStatus;
    private Button requestButton;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_book_view);

        // Get Book object passed from Intent
        Intent intent = getIntent();
        // Class variables
        book = (Book) intent.getSerializableExtra("BOOK");
        // Set references to UI elements
        setupUIReferences();
        // Set UI values
        setUIValues(book);

        // Change colour of status text based on book's status
        switch (bookStatus.getText().toString()) {
            case "Available":
                bookStatus.setTextColor(getResources().getColor(R.color.available_green));
                break;
            case "Requested":
                bookStatus.setTextColor(getResources().getColor(R.color.requested_blue));
                break;
            case "Accepted":
                bookStatus.setTextColor(getResources().getColor(R.color.accepted_yellow));
                break;
            case "Borrowed":
                bookStatus.setTextColor(getResources().getColor(R.color.borrowed_red));
                break;
        }

        // Set onClickListeners for the buttons
        ownerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Owner Profile button pressed");
            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Request Book button pressed");
                // Get access to requests collection
                CollectionReference requestCollectionRef = db.collection("Requests");
                // Get BookID for later
                db.collection("books")
                        .whereEqualTo("ownerID", book.getOwnerID())
                        .whereEqualTo("title", book.getTitle())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                bookID = document.getId();
                            }
                        }
                    }
                });
                // Create Request Object
                Request request = new Request(user.getUid(), book.getOwnerID(), bookID, book.getPictureURL(), book.getTitle());
                // Get reference to user
                DocumentReference userRef = db.collection("users").document(user.getUid());
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                   requester = document.toObject(User.class);
                            } else {
                                Log.d("TEMP", "Document does not exist");
                            }
                        } else {
                            Log.d("TEMP", "get failed with ", task.getException());
                        }
                    }
                });
                // Add request to db and add new document ID to requester
                requestCollectionRef.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        requester.addSentRequest(documentReference.getId());
                        DocumentReference userRef = db.collection("users").document(user.getUid());
                        userRef.update("SentRequestIDs", requester.getSentRequestsIDs());
                    }
                });
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * This method sets up all of the references to UI elements in the activity layout.
     * */
    private void setupUIReferences() {
        bookImage = findViewById(R.id.public_book_view_image);
        titleText = findViewById(R.id.public_book_view_title);
        authorText = findViewById(R.id.public_book_view_author);
        ISBNText = findViewById(R.id.public_book_view_ISBN);
        descText =findViewById(R.id.public_book_view_desc);
        ownerProfileButton = findViewById(R.id.public_book_view_owner_profile_button);
        bookStatus = findViewById(R.id.public_book_view_status_text);
        requestButton = findViewById(R.id.public_book_view_request_button);
        backButton = findViewById(R.id.public_book_view_back_button);
    }

    /**
     * This method assigns values to UI elements in the activity layout.
     * */
    private void setUIValues(Book book) {
        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookImage).execute(pictureURL);
        } else {
            bookImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
        }
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        ISBNText.setText(book.getISBN());
        descText.setText(book.getDescription());
        // ownerProfileButton // probably get book.getOwner() and then either populate button with that User object or do smthn with db
        bookStatus.setText(book.getStatus());
    }
}
