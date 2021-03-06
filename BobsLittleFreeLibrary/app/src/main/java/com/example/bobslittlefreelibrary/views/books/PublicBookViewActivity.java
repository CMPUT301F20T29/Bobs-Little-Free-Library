package com.example.bobslittlefreelibrary.views.books;

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

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Notification;
import com.example.bobslittlefreelibrary.models.NotificationType;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by another User
 * */
public class PublicBookViewActivity extends AppCompatActivity {

    // Class variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Book book;
    private boolean userAlreadyRequested;
    private String notificationMessage;
    private User bookOwner;
    // UI Variables
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
        book = (Book) intent.getSerializableExtra("BOOK");
        // Initialize UI
        setupUIReferences();
        checkAlreadyRequested();
        setUIValues(book);

        // Query for User document to convert into object to pass to profile view (if it is pressed)
        db.collection("users").document(book.getOwnerID()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        bookOwner = document.toObject(User.class);
                        ownerProfileButton.setText(bookOwner.getUsername());

                        ownerProfileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PublicBookViewActivity.this, PublicProfileViewActivity.class);
                                intent.putExtra("USER", bookOwner);  // Send user to be displayed in profile view activity
                                startActivity(intent);
                            }
                        });
                    }
                });
        // Set onClickListeners for the buttons
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get access to requests collection
                CollectionReference requestCollectionRef = db.collection("requests");
                // Create Request Object
                Request request = new Request(user.getUid(), book.getOwnerID(), book.getBookID(), book.getPictureURL(), book.getTitle());
                // Add request to db
                requestCollectionRef.add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.update("requestID", documentReference.getId());
                                Snackbar sb = Snackbar.make(v, "Request Sent", Snackbar.LENGTH_SHORT);
                                sb.show();
                                requestButton.setClickable(false);
                                requestButton.setVisibility(View.INVISIBLE);
                                bookStatus.setText("Requested");
                                bookStatus.setTextColor(getResources().getColor(R.color.requested_blue));
                            }
                        });

                // Update Book document
                db.collection("books").document(book.getBookID())
                        .update("numberOfRequests", book.getNumberOfRequests() + 1);
                if (book.getNumberOfRequests() == 0) { // The book object we are getting the value from has not been updated so the number of requests would still be n - 1
                    // Update book status
                    db.collection("books").document(book.getBookID())
                            .update("status", "Requested");
                }

                // Create a Notification that will be for the Owner of this book
                notificationMessage = "Your book %s has been requested by %s.";
                db.collection("users").document(user.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User thisUser = document.toObject(User.class); // thisUser refers to the user that pressed the request button
                            notificationMessage = String.format(notificationMessage, book.getTitle(), thisUser.getUsername());
                            String timestamp = java.text.DateFormat.getDateInstance().format(new Date());

                            Notification notification = new Notification(NotificationType.BORROW, notificationMessage, timestamp, book.getBookID(), book.getOwnerID());
                            db.collection("notifications").add(notification);
                        }
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
        descText = findViewById(R.id.public_book_view_desc);
        ownerProfileButton = findViewById(R.id.public_book_view_owner_profile_button);
        bookStatus = findViewById(R.id.public_book_view_status_text);
        requestButton = findViewById(R.id.public_book_view_request_button);
        backButton = findViewById(R.id.public_book_view_back_button);
    }

    /**
     * This method runs a query that checks whether or not the user has already requested this book.
     * */
    private void checkAlreadyRequested() {
        // Check if the user has already requested this book
        db.collection("requests")
                .whereEqualTo("bookRequestedID", book.getBookID())
                .whereEqualTo("reqSenderID", user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userAlreadyRequested = !queryDocumentSnapshots.isEmpty();
                        Log.d("TEMP", "userAlreadyRequested right after query = " + userAlreadyRequested);
                        if (userAlreadyRequested) {
                            requestButton.setClickable(false);
                            requestButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    /**
     * This method assigns values to UI elements in the activity layout.
     * @param book The book object to draw values from
     * */
    private void setUIValues(Book book) {
        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookImage).execute(pictureURL);
        }
        titleText.setText(book.getTitle());
        authorText.setText(String.format("Author: %s", book.getAuthor()));
        ISBNText.setText(String.format("ISBN: %s", book.getISBN()));
        descText.setText(book.getDescription());
        bookStatus.setText(book.getStatus());
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
                requestButton.setClickable(false);
                requestButton.setVisibility(View.INVISIBLE);
                break;
            case "Borrowed":
                bookStatus.setTextColor(getResources().getColor(R.color.borrowed_red));
                requestButton.setClickable(false);
                requestButton.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
