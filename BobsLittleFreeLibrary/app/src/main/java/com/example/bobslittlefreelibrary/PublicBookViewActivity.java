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
 * This activity provides a location to display all the information that pertains to a Book owned by another User
 * TODO: Setup button functionality, initialize imageView, populate profile button with username
 *
 * */
public class PublicBookViewActivity extends AppCompatActivity {

    private Book book;
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
        //book = intent.getSerializableExtra("BOOK"); // need to make Book class serializable

        // Set references to UI elements
        setupUIReferences();
        // Set UI values
        //setUIValues(book);

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

    private void setUIValues(Book book) {
        // bookImage.setImageResource("@drawable/" + book.getPicture());
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        ISBNText.setText(book.getISBN());
        descText.setText(book.getDescription());
        // ownerProfileButton // probably get book.getOwner() and then either populate button with that User object or do smthn with db
        bookStatus.setText(book.getStatus());
    }
}
