package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bobslittlefreelibrary.utils.DownloadImageTask;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * AddBookActivity is an activity where a user can add a book to their collection. Scan book to
 * have it's info be autofilled by Google Books API.
 *
 * TODO: Save book to user's collection in firestore.
 */
public class AddBookActivity extends AppCompatActivity implements ScanFragment.OnFragmentInteractionListener,
        SelectImageFragment.OnFragmentInteractionListener {

    final String TAG = "AddBookActivity";

    private Button backButton;
    private Button scanButton;
    private Button selectImageButton;
    private Button addButton;
    private EditText isbnInput;
    private EditText titleInput;
    private EditText authorInput;
    private EditText descInput;
    private ImageView picture;
    private String currentPhotoPath =  "drawable://" + R.drawable.blue_book;;
    private Book book;
    Boolean validInput = false;

    private RequestQueue mQueue;

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
        picture = findViewById(R.id.image);

        // Add text watcher to EditTexts
        isbnInput.addTextChangedListener(inputFormTextWatcher);
        titleInput.addTextChangedListener(inputFormTextWatcher);
        authorInput.addTextChangedListener(inputFormTextWatcher);
        descInput.addTextChangedListener(inputFormTextWatcher);

        // button onClick Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                AddBookActivity.this.startActivity(intent);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ADD button clicked");
                if (validInput) {
                    // TODO: create new book and add to firestore here
                } else {
                    showSnackbar(v);
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
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    // ScanFragment calls this method when it finds an isbn, here I look up that isbn in the
    // Google Books API and autofill the EditTexts.
    @Override
    public void onIsbnFound(final String isbn) {
        Log.d(TAG, "onIsbnFound: " + isbn);

        String url = "https://www.googleapis.com/books/v1/volumes?q=ISBN:" + isbn
                + "&key=AIzaSyA8uqm_F6NxRRpkXt0dDQ3RqkqATEmsuvM";
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
                                String url = getImageUrl(imagesLinks);
                                if (url != null) {
                                    // dl and set image to picture ImageView
                                    new DownloadImageTask(picture).execute(url);
                                }
                            }

                            // autofill EdditTexts
                            isbnInput.setText(isbn);
                            titleInput.setText(title.trim());
                            authorInput.setText(author.trim());
                            descInput.setText(desc.trim());

                            addButton.setEnabled(true);

                        } catch (JSONException e) {
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
            this.currentPhotoPath = currentPhotoPath;
            picture.setImageURI(Uri.parse(currentPhotoPath));
        }

        if (requestCode == 2 && resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            this.currentPhotoPath = selectedImage.toString();
            picture.setImageURI(selectedImage);
        }
    }

    // Gets a message to be displayed on a snackbar in the event the user's input is invalid.
    private void showSnackbar(View v){

        String msg = "Please fix the following issues before adding your book:\n";

        String isbn = isbnInput.getText().toString();
        String title = titleInput.getText().toString();
        String author = authorInput.getText().toString();
        String desc = descInput.getText().toString();

        if (desc.length() > 1000) { msg += "\n - Description is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (title.length() > 50) { msg += "\n - Title is too long"; }
        if (author.length() > 50) { msg += "\n - Author is too long"; }
        if (isbn.length() != 10 && isbn.length() != 13 && !isbn.isEmpty()) { msg += "\n - ISBN is invalid"; }
        if (isbn.isEmpty()) { msg += "\n - ISBN is empty"; }
        if (title.isEmpty()) { msg += "\n - Title is empty"; }
        if (author.isEmpty()) { msg += "\n - Author is empty"; }

        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(6);
        sb.show();
    }
}

