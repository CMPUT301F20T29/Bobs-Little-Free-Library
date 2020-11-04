package com.example.bobslittlefreelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.utils.DownloadImageTask;

/**
 * This activity provides a location to display all the information that pertains to a Book owned by the User
 * TODO: Setup button functionality, profile button with username and link it to UserProfileView activity
 *
 * */
public class MyBookViewActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private TextView descText;
    private Button borrowerProfileButton;
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
        borrowerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Borrower Profile button pressed");
            }
        });
        removeBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Remove Book button pressed");
            }
        });
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Edit Info button pressed");
                Intent intent = new Intent(MyBookViewActivity.this, EditBookActivity.class);
                intent.putExtra("BOOK_ID", getIntent().getExtras().getString("BOOK_ID"));
                intent.putExtra("BOOK", book);  // Send book to be displayed in book view activity
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBookViewActivity.this, MainActivity.class);
                startActivity(intent);
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
        borrowerProfileButton = findViewById(R.id.my_book_view_borrower_profile_button);
        bookStatus = findViewById(R.id.my_book_view_status_text);
        removeBookButton = findViewById(R.id.my_book_view_remove_button);
        editInfoButton = findViewById(R.id.my_book_view_edit_button);
        backButton = findViewById(R.id.my_book_view_back_button);

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
        // borrowerProfileButton
        bookStatus.setText(book.getStatus());
    }
}
