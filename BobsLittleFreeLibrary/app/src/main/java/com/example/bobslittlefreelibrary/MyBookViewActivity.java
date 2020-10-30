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

/**
 * This activity provides a location to display all the information that pertains to a Book owned by the User
 * TODO: Setup button functionality, initialize imageView, populate profile button with username
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
        Book book = (Book) intent.getSerializableExtra("BOOK");
        //Book book = new Book("Test Book", "available", "this is a test description", "Khang", );

        // Set references to UI elements
        setupUIReferences();
        // Set UI values
        setUIValues(book);

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
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Back button pressed");
            }
        });
    }

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

    private void setUIValues(Book book) {
        // bookImage.setImageResource("@drawable/" + book.getPicture());
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        ISBNText.setText(book.getISBN());
        descText.setText(book.getDescription());
        // borrowerProfileButton
        bookStatus.setText(book.getStatus());
    }
}
