package com.example.bobslittlefreelibrary.views.books;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.requests.ShowAllRequestsActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by the User
 * */
public class MyBookViewActivity extends AppCompatActivity {

    // Class variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Book book;
    private User userObject;
    // UI variables
    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private TextView descText;
    private TextView borrowerText;
    private Button borrowerProfileButton;
    private TextView bookStatus;
    private Button viewRequestsButton;
    private Button removeBookButton;
    private Button editInfoButton;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_view);

        // Get Book object passed from Intent
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("BOOK");
        // Initialize UI
        setupUIReferences();
        setUIValues(book);

        if (book.getCurrentBorrowerID() != null) {
            // Query for User document to convert into object to pass to profile view (if it is pressed)
            db.collection("users").document(book.getCurrentBorrowerID()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            userObject = document.toObject(User.class);
                            borrowerProfileButton.setText(userObject.getUsername());

                            borrowerProfileButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MyBookViewActivity.this, PublicProfileViewActivity.class);
                                    intent.putExtra("USER", userObject);  // Send user to be displayed in profile view activity
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        } else {
            borrowerProfileButton.setVisibility(View.INVISIBLE);
            borrowerText.setVisibility(View.INVISIBLE);
        }

        // Set onClickListeners for the buttons
        viewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBookViewActivity.this, ShowAllRequestsActivity.class);
                intent.putExtra("BOOK", book);
                startActivity(intent);
                finish();
            }
        });
        removeBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check whether the book can be deleted or not
                if (bookStatus.getText().equals("Available") || bookStatus.getText().equals("Requested")) {
                    removeBookID();
                    removeBookRequests();
                    removeFromBookCollection();
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Unable to delete: Your book is currently being borrowed / will be borrowed soon.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Edit Info button pressed");
                Intent intent = new Intent(MyBookViewActivity.this, EditBookActivity.class);
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
        descText = findViewById(R.id.my_book_view_desc);
        borrowerText = findViewById(R.id.my_book_view_borrower_text);
        borrowerProfileButton = findViewById(R.id.my_book_view_borrower_profile_button);
        bookStatus = findViewById(R.id.my_book_view_status_text);
        viewRequestsButton = findViewById(R.id.my_book_view_see_requests_button);
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
                break;
            case "Borrowed":
                bookStatus.setTextColor(getResources().getColor(R.color.borrowed_red));
                break;
        }
    }

    /**
     * This method runs a query that searches for the user document of the owner of the book and removes the book ID from bookIDs
     * */
    private void removeBookID() {
        DocumentReference userDoc = db.collection("users").document(user.getUid());
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userObject = documentSnapshot.toObject(User.class);

                ArrayList<String> usersBooks = userObject.getBookIDs();
                Log.d("TEMP", "onSuccess: " + book.getBookID());
                usersBooks.remove(book.getBookID());

                Map<String, Object> newBooksMap = new HashMap<>();
                newBooksMap.put("bookIDs", usersBooks);

                Log.d("TEMP", "onSuccess: " + usersBooks);

                userDoc.update(newBooksMap);
            }
        });
    }

    /**
     * This method runs a query that removes the book document for this book from the books collection
     * */
    private void removeFromBookCollection() {
        // Now remove the book from the books collection
        DocumentReference bookRef = db.collection("books").document(book.getBookID());
        bookRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: " + book.getBookID());
                Log.d("TEMP", "Book document deleted");
                Toast toast = Toast.makeText(getApplicationContext(), "Book Deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TEMP", "Error deleting Book document", e);
            }
        });
    }

    /**
     * This method runs a query that searches for all the requests for this book and deletes them from the db
     * */
    private void removeBookRequests() {
        db.collection("requests")
                .whereEqualTo("bookRequestedID", book.getBookID())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("requests").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TEMP", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TEMP", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d("TEMP", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
