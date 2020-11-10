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
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by the User
 * TODO:
 *  - Setup profile button functionality, profile button with username and link it to UserProfileView activity
 *  - Check if book is in the possession of another user before allowing owner to delete it
 *  - when coming back from EditBookActivity if the image is changed, the image in this activity doesnt update
 *
 *
 * */
public class MyBookViewActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String bookID;
    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private TextView descText;
    private Button ownerProfileButton;
    private TextView bookStatus;
    private Button removeBookButton;
    private Button editInfoButton;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_view);

        // Get Book object passed from Intent
        Intent intent = getIntent();
        // Class variables
        Book book = (Book) intent.getSerializableExtra("BOOK");
        // Set references to UI elements
        setupUIReferences();
        // Set UI values
        setUIValues(book);
        bookID = intent.getStringExtra("BOOK_ID");

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
        removeBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Remove Book button pressed");
                // First remove the book from the user's bookID array
                DocumentReference userDoc = db.collection("users").document(user.getUid());
                userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User userObject = documentSnapshot.toObject(User.class);

                        ArrayList<String> usersBooks = userObject.getBookIDs();
                        Log.d("TEMP", "onSuccess: " + bookID);
                        usersBooks.remove(bookID);

                        HashMap newBooksMap = new HashMap<String, ArrayList>();
                        newBooksMap.put("bookIDs", usersBooks);

                        Log.d("TEMP", "onSuccess: " + usersBooks);

                        userDoc.update(newBooksMap);
                    }
                });

                // Now remove the book from the books collection
                DocumentReference bookRef = db.collection("books").document(bookID);
                bookRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: " + bookID);
                        Log.d("TEMP", "Book document deleted");
                        Snackbar sb = Snackbar.make(v, "Book Deleted", Snackbar.LENGTH_SHORT);
                        sb.show();
                        Intent intent = new Intent(MyBookViewActivity.this, MainActivity.class);
                        intent.putExtra("WHICH_FRAGMENT", "BOOKS");
                        MyBookViewActivity.this.startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEMP", "Error deleting Book document", e);
                    }
                });
            }
        });
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Edit Info button pressed");
                Intent intent = new Intent(MyBookViewActivity.this, EditBookActivity.class);
                intent.putExtra("BOOK_ID", bookID);
                intent.putExtra("BOOK", book);  // Send book to be displayed in book view activity
                startActivity(intent);
                finish();
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
        bookImage = findViewById(R.id.my_book_view_image);
        titleText = findViewById(R.id.my_book_view_title);
        authorText = findViewById(R.id.my_book_view_author);
        ISBNText = findViewById(R.id.my_book_view_ISBN);
        descText =findViewById(R.id.my_book_view_desc);
        ownerProfileButton = findViewById(R.id.my_book_view_owner_profile_button);
        bookStatus = findViewById(R.id.my_book_view_status_text);
        removeBookButton = findViewById(R.id.my_book_view_remove_button);
        editInfoButton = findViewById(R.id.my_book_view_edit_button);
        backButton = findViewById(R.id.my_book_view_back_button);
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
