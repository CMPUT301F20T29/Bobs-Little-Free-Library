package com.example.bobslittlefreelibrary.views.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bobslittlefreelibrary.ScanFragment;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.SelectImageFragment;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * AddBookActivity is an activity where a user can add a book to their collection. Scan book to
 * have it's info be autofilled by Google Books API.
 */
public class AddBookActivity extends AppCompatActivity implements
        ScanFragment.OnFragmentInteractionListener,
        SelectImageFragment.OnFragmentInteractionListener {

    final String TAG = "AddBookActivity";

    // views
    private Button backButton;
    private Button scanButton;
    private Button selectImageButton;
    private Button addButton;
    private Button autoFillButton;
    private EditText isbnInput;
    private EditText titleInput;
    private EditText authorInput;
    private EditText descInput;
    private ImageView imageView;
    private ProgressBar spinner;

    // book data
    private String usersImageFile;
    private String imageUrlFromResponse;
    private String bookId;
    private Book book;

    private Boolean validInput = false;
    private FirebaseFirestore db;
    private RequestQueue mQueue;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // get views
        backButton = findViewById(R.id.back_button);
        scanButton = findViewById(R.id.scan_button);
        addButton = findViewById(R.id.add_button);
        selectImageButton = findViewById(R.id.image_label_button);
        isbnInput = findViewById(R.id.isbn_input);
        titleInput = findViewById(R.id.title_input);
        authorInput = findViewById(R.id.author_input);
        descInput = findViewById(R.id.desc_input);
        imageView = findViewById(R.id.image);
        autoFillButton = findViewById(R.id.auto_fill);
        spinner = findViewById(R.id.progressBar1);
        autoFillButton.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        // Add text watcher to EditTexts
        isbnInput.addTextChangedListener(inputFormTextWatcher);
        titleInput.addTextChangedListener(inputFormTextWatcher);
        authorInput.addTextChangedListener(inputFormTextWatcher);
        descInput.addTextChangedListener(inputFormTextWatcher);

        // button onClick Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanFragment().show(getSupportFragmentManager(), "SCAN");
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectImageFragment().show(getSupportFragmentManager(), "CHOOSE");
            }
        });

        autoFillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(AddBookActivity.this);
                spinner.setVisibility(View.VISIBLE);
                isbnInput.clearFocus();
                autofillBookData(isbnInput.getText().toString());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInput) {
                    // Get instance of db
                    db = FirebaseFirestore.getInstance();

                    // Get strings
                    String isbn = isbnInput.getText().toString();
                    String title = titleInput.getText().toString();
                    String author = authorInput.getText().toString();
                    String desc = descInput.getText().toString();

                    db.collection("books").whereEqualTo("isbn", isbn)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                // Create new Books object and it to add to firestore
                                book = new Book(title, author, isbn, desc, currentUser.getUid(), "Available");
                                addBook(book);
                            } else {
                                Log.d(TAG, "onSuccess: HERE");
                                String msg = "Oops, looks like that book has already been uploaded, please enter a different book.";
                                Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    showInvalidInputSnackbar(v);
                }
            }
        });

        // create a request queue
        mQueue = Volley.newRequestQueue(this);
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
            isbnInput = findViewById(R.id.isbn_input);
            titleInput = findViewById(R.id.title_input);
            authorInput = findViewById(R.id.author_input);
            descInput = findViewById(R.id.desc_input);

            String isbn = isbnInput.getText().toString();
            String title = titleInput.getText().toString();
            String author = authorInput.getText().toString();
            String desc = descInput.getText().toString();

            boolean underCharLimitCheck = (desc.length() < 1000) || (title.length() < 50) || (author.length() < 50);
            boolean emptyCheck = (isbn.isEmpty() || title.isEmpty() || author.isEmpty());
            boolean validIsbn = (isbn.length() == 10 || isbn.length() == 13);
            validInput = underCharLimitCheck && !emptyCheck && validIsbn;

            if (!isbn.isEmpty()) {
                autoFillButton.setVisibility(View.VISIBLE);
                if (validIsbn) {
                    autoFillButton.setEnabled(true);
                } else {
                    autoFillButton.setEnabled(false);
                }
            } else {
                autoFillButton.setVisibility(View.GONE);
            }
        }
    };

    // Given a book this method adds it as a document int the books collection in firestore,
    // uploads it's image if nessecary, and sets it's bookID field.
    private void addBook(Book book) {
        spinner.setVisibility(View.VISIBLE);
        db.collection("books")
                .add(book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                bookId = documentReference.getId();

                // if the user took/selected an image, then upload it to storage, and save it's url to
                // the book's document
                if (imageUrlFromResponse != null) {
                    db.collection("books").document(bookId).update("pictureURL", imageUrlFromResponse);

                } else if (usersImageFile != null) {
                    uploadImageFile();
                }

                // add bookID to book document
                Map<String, Object> bookUpdateMap = new HashMap<>();
                book.setBookID(bookId);
                bookUpdateMap.put("bookID", bookId);
                documentReference.update(bookUpdateMap);

                // Update the bookIDs array in the user collection
                db.collection("users").document(currentUser.getUid()).
                        get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);

                        ArrayList<String> usersBooks = user.getBookIDs();
                        Log.d(TAG, "onSuccess: " + bookId);
                        usersBooks.add(bookId.toString());

                        Map<String, Object> newBooksMap = new HashMap<>();
                        newBooksMap.put("bookIDs", usersBooks);

                        Log.d(TAG, "onSuccess: " + usersBooks);

                        db.collection("users").
                                document(currentUser.getUid()).update(newBooksMap);

                        db.collection("books").document(bookId)
                                .update("ownerUsername", user.getUsername())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Return to main activity
                                spinner.setVisibility(View.GONE);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    // ScanFragment calls this method when it finds an isbn, here I look up that isbn in the
    // Google Books API and auto-fill the EditTexts according to the response.
    @Override
    public void onIsbnFound(final String isbn) {
        Log.d(TAG, "onIsbnFound: " + isbn);
        spinner.setVisibility(View.VISIBLE);
        autofillBookData(isbn);
    }

    // Given an isbn this method gets the books' information from the google books API and auto-fills
    // all fields
    private void autofillBookData(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=ISBN:" + isbn
                + "&key=" + getString(R.string.BOOKS_API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse JSON response
                            JSONArray items = response.getJSONArray("items");
                            JSONObject book = (JSONObject) items.get(0);
                            JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                            String title = volumeInfo.getString("title");
                            String author = (String) volumeInfo.getJSONArray("authors").get(0);
                            String desc = volumeInfo.getString("description");

                            JSONObject imagesLinks = volumeInfo.optJSONObject("imageLinks");
                            if (imagesLinks != null) {
                                // if there are image links in response then get the url for image
                                imageUrlFromResponse = getImageUrl(imagesLinks);

                                if (imageUrlFromResponse != null) {
                                    // dl and set image to picture ImageView
                                    new DownloadImageTask(imageView).execute(imageUrlFromResponse);
                                }
                            }

                            // auto-fill EditTexts
                            isbnInput.setText(isbn);
                            titleInput.setText(title.trim());
                            authorInput.setText(author.trim());
                            descInput.setText(desc.trim());

                            hideKeyboard(AddBookActivity.this);
                            isbnInput.clearFocus();
                            autoFillButton.setVisibility(View.GONE);
                            spinner.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.d(TAG, "onErrorResponse: ");
                            String msg = "We couldn't find that book, please check if the isbn is correct.";
                            View view = findViewById(R.id.scroll_view);
                            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // add request to queue
        mQueue.add(request);
    }

    // image urls from Google Books API are http urls, android will only load
    // data over https, so this method just adds the s to the url.
    private String getHTTPSImage(String image) {
        return image.substring(0,4) + "s" + image.substring(4);
    }

    // Gets the largest available image from the Google Books API,
    // If the only images are smaller than medium, return null
    // so the default image is kept.
    private String getImageUrl(JSONObject imagesLinks) {
        String xlImageUrl = imagesLinks.optString("extraLarge");
        if (xlImageUrl != "") { return getHTTPSImage(xlImageUrl); }
        String lImageUrl = imagesLinks.optString("large");
        if (lImageUrl != "") { return getHTTPSImage(lImageUrl); }
        String mImageUrl = imagesLinks.optString("medium");
        if (mImageUrl != "") { return getHTTPSImage(mImageUrl); }

        return null;
    }

    // SelectImageFragment calls this method when the user either takes and image or selects one
    // from their device.
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

        imageUrlFromResponse = null;
    }

    // Gets a message to be displayed on a snackbar in the event the user's input is invalid.
    public void showInvalidInputSnackbar(View v){

        String msg = "Please fix the following issues before adding your book:\n";

        String isbn = isbnInput.getText().toString();
        String title = titleInput.getText().toString();
        String author = authorInput.getText().toString();
        String desc = descInput.getText().toString();

        if (desc.length() > 1000) { msg += "\n - Description is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (author.length() > 50) { msg += "\n - Author is too long"; }
        if (!isbn.isEmpty() && isbn.length() != 10 && isbn.length() != 13) { msg += "\n - ISBN is invalid"; }
        if (isbn.isEmpty()) { msg += "\n - ISBN is empty"; }
        if (title.isEmpty()) { msg += "\n - Title is empty"; }
        if (author.isEmpty()) { msg += "\n - Author is empty"; }

        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(6);
        sb.show();
    }

    // Exits AddBookActivity and returns to MainActivity showing BooksFragment
    private void exitActivity(){
        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Uploads the image file at usersImageFile
    private void uploadImageFile() {
        // Get Storage reference to to /books-images
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference bookImagesRef = storageRef.child("book-images");

        // Create and get Storage reference to to /books-images/{bookId}.jpg
        StorageReference imageRef = bookImagesRef.child(bookId + ".jpg");

        // Upload image
        UploadTask uploadTask = imageRef.putFile(Uri.parse(usersImageFile));
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                // Get the url to the uploaded image and save it in the pictureURL field of the book
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("books").document(bookId).update("pictureURL", uri.toString());
                    }
                });
            }
        });
    }

    // hides keyoboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}