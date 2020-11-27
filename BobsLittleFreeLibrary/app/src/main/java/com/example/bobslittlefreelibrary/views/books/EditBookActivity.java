package com.example.bobslittlefreelibrary.views.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.SelectImageFragment;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * EditBookActivity is an activity where a user can add a book to their collection. Scan book to
 * have it's info be autofilled by Google Books API.
 */
public class EditBookActivity extends AppCompatActivity implements SelectImageFragment.OnFragmentInteractionListener {

    final String TAG = "EditBookActivity";

    // views
    private Button backButton;
    private Button selectImageButton;
    private Button addButton;
    private EditText titleInput;
    private EditText authorInput;
    private EditText descInput;
    private ImageView imageView;

    // book data
    private Book book;
    private String usersImageFile;

    private Boolean validInput = true;
    private FirebaseFirestore db;
    DocumentReference bookRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // get views
        backButton = findViewById(R.id.back_button);
        addButton = findViewById(R.id.add_button);
        selectImageButton = findViewById(R.id.image_label_button);
        titleInput = findViewById(R.id.title_input);
        authorInput = findViewById(R.id.author_input);
        descInput = findViewById(R.id.desc_input);
        imageView = findViewById(R.id.image);

        // add text watcher
        descInput.addTextChangedListener(inputFormTextWatcher);

        // Get Book object and id passed from Intent
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("BOOK");

        // Set image and desc input field
        if (book.getPictureURL() != null) {
            new DownloadImageTask(imageView).execute(book.getPictureURL());
        }
        titleInput.setText(book.getTitle());
        authorInput.setText(book.getAuthor());
        descInput.setText(book.getDescription());

        // button onClick Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectImageFragment().show(getSupportFragmentManager(), "CHOOSE");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInput) {
                    String title = titleInput.getText().toString();
                    String author = authorInput.getText().toString();
                    String desc = descInput.getText().toString();

                    // get book reference
                    db = FirebaseFirestore.getInstance();
                    bookRef = db.collection("books").document(book.getBookID());

                    // get book
                    bookRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Book bookFromDb = documentSnapshot.toObject(Book.class);
                            Map<String, Object> bookUpdateMap = new HashMap<>();

                            // if title entered and title from db are different then update db
                            if (!title.equals(bookFromDb.getTitle())) {
                                bookUpdateMap.put("title", title);
                                book.setTitle(title);
                            }

                            // if author entered and author from db are different then update db
                            if (!desc.equals(bookFromDb.getAuthor())) {
                                bookUpdateMap.put("author", author);
                                book.setAuthor(author);
                            }

                            // if description entered and description from db are different then update db
                            if (!desc.equals(bookFromDb.getDescription())) {
                                bookUpdateMap.put("description", desc);
                                book.setDescription(desc);
                            }

                            if (usersImageFile != null && !usersImageFile.equals(bookFromDb.getPictureURL())) {
                                uploadImageFile();
                            }

                            bookRef.update(bookUpdateMap);
                            exitActivity();
                        }
                    });

                } else {
                    showInvalidInputSnackbar(v);
                }
            }
        });
    }

    /*
     * TextWatcher checks if input is valid as the user changes text fields.
     */
    private TextWatcher inputFormTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            descInput = findViewById(R.id.desc_input);
            String desc = descInput.getText().toString();

            validInput = ( (desc.length() < 1000) && !desc.isEmpty() );

            Log.d(TAG, "onTextChanged: " + validInput);
        }
    };


    // Gets a message to be displayed on a snackbar in the event the user's input is invalid.
    private void showInvalidInputSnackbar(View v){

        String msg = "Please fix the following issues before adding your book:\n";

        String title = titleInput.getText().toString();
        String author = authorInput.getText().toString();
        String desc = descInput.getText().toString();

        if (desc.length() > 1000) { msg += "\n - Description is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (author.length() > 50) { msg += "\n - Author is too long"; }
        if (title.isEmpty()) { msg += "\n - Title is empty"; }
        if (author.isEmpty()) { msg += "\n - Author is empty"; }

        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(6);
        sb.show();
    }

    // Exits EditBookActivity and returns to MainActivity showing BooksFragment
    private void exitActivity(){
        Intent intent = new Intent(EditBookActivity.this, MyBookViewActivity.class);
        intent.putExtra("BOOK", book);  // Send book to be displayed in book view activity\
        Log.d(TAG, "exitActivity: " + book.getDescription());
        startActivity(intent);
        finish();
    }

    // Uploads the image file at usersImageFile
    private void uploadImageFile() {
        // Get Storage reference to to /books-images
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference bookImagesRef = storageRef.child("book-images");

        // Create and get Storage reference to to /books-images/{bookId}.jpg
        StorageReference imageRef = bookImagesRef.child(bookRef.getId() + ".jpg");

        // Upload image
        UploadTask uploadTask = imageRef.putFile(Uri.parse(usersImageFile));
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                // Get the url to the uploaded image and save it in the pictureURL field of the book
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("books").document(bookRef.getId()).update("pictureURL", uri.toString());
                        book.setPictureURL(uri.toString());
                    }
                });
            }
        });
    }

    @Override
    public void imageSelected(int requestCode, int resultCode, Intent imageReturnedIntent, String currentPhotoPath) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            this.usersImageFile = Uri.fromFile(new File(currentPhotoPath)).toString();
            imageView.setImageURI(Uri.parse(currentPhotoPath));
        }

        if (requestCode == 2 && resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            this.usersImageFile = selectedImage.toString();
            imageView.setImageURI(selectedImage);
        }
    }
}