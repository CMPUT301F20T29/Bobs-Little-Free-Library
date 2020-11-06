package com.example.bobslittlefreelibrary.views.books;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by another User
 * TODO: Setup profile button functionality, populate profile button with username and link it to UserProfileView activity, make it so that when a Book is requested, the user cannot send another request
 *
 * Currently, if a user leaves the activity and views it again, they will be able to press the request button and add another request to the db
 *
 * */
public class PublicBookViewActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Book book;
    private String bookID;

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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get access to requests collection
                CollectionReference requestCollectionRef = db.collection("requests");
                // Get bookID from intent
                bookID = intent.getStringExtra("BOOK_ID");
                // Create Request Object
                Request request = new Request(user.getUid(), book.getOwnerID(), bookID, book.getPictureURL(), book.getTitle());
                // Add request to db
                requestCollectionRef.add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Snackbar sb = Snackbar.make(v, "Request Sent", Snackbar.LENGTH_SHORT);
                                sb.show();
                                requestButton.setClickable(false);
                                requestButton.setBackgroundColor(getResources().getColor(R.color.disabled_grey));
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
     * @param book The book object to draw values from
     * */
    private void setUIValues(Book book) {
        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookImage).execute(pictureURL);
        } else {
            bookImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
        }
        titleText.setText(book.getTitle());
        authorText.setText(String.format("Author: %s", book.getAuthor()));
        ISBNText.setText(String.format("ISBN: %s", book.getISBN()));
        descText.setText(book.getDescription());
        db.collection("users").document(book.getOwnerID())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                ownerProfileButton.setText(user.getUsername());
            }
        });
        bookStatus.setText(book.getStatus());
    }
}
