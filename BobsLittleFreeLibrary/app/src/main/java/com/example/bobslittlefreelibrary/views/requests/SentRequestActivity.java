package com.example.bobslittlefreelibrary.views.requests;

/**
 * This class is the activity for a sent request.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Notification;
import com.example.bobslittlefreelibrary.models.NotificationType;
import com.example.bobslittlefreelibrary.models.Request;
import com.example.bobslittlefreelibrary.controllers.DownloadImageTask;
import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.example.bobslittlefreelibrary.views.users.PublicProfileViewActivity;
import com.google.android.gms.maps.model.LatLng;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SentRequestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Request currentRequest;
    private LinearLayout bookInfoContainer;
    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private Button userProfileButton;
    private Button deleteRequestButton;
    private Button backButton;
    private Button mapButton;
    private Book currentBook;
    private String notificationMessage;
    private boolean hasSelectedMap = false;
    private TextView requestStatusText;
    private User requestReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request);

        // get the request object from the intent and set userid
        Intent intent = getIntent();
        currentRequest = (Request) intent.getSerializableExtra("REQUEST");

        // getting references to the objects
        bookInfoContainer = findViewById(R.id.book_info_container);
        bookImage = findViewById(R.id.book_view_image);
        titleText = findViewById(R.id.book_view_title);
        authorText = findViewById(R.id.book_view_author);
        ISBNText = findViewById(R.id.book_view_ISBN);
        userProfileButton = findViewById(R.id.provider_profile_button);
        deleteRequestButton = findViewById(R.id.delete_request_button);
        backButton = findViewById(R.id.back_button);
        mapButton = findViewById(R.id.open_map_button);
        requestStatusText = findViewById(R.id.request_status_text);

        // query for the username
        db.collection("users")
                .document(currentRequest.getReqReceiverID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userProfileButton.setText((String)documentSnapshot.get("username"));
                        requestReceiver = documentSnapshot.toObject(User.class);
                    }
                });

        // query for the book information
        db.collection("books")
                .document(currentRequest.getBookRequestedID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentBook = documentSnapshot.toObject(Book.class);
                        String pictureURL = currentRequest.getBookImageURL();
                        if (pictureURL != null) {
                            new DownloadImageTask(bookImage).execute(pictureURL);
                        }
                        titleText.setText((String)documentSnapshot.get("title"));
                        authorText.setText((String)documentSnapshot.get("author"));
                        ISBNText.setText((String)documentSnapshot.get("isbn"));

                        // If book status has been exchanged but not a return request yet,
                        // or if it has been accepted but not exchanged yet, setup the UI in that way.
                        String bookStatus = (String) documentSnapshot.get("status");

                        if (bookStatus.equals("Borrowed") && !(currentRequest.isReturnRequest())) {
                            requestStatusText.setText("Borrowed");
                            requestStatusText.setTextColor(getResources().getColor(R.color.borrowed_red));
                            mapButton.setText("Select Location");
                            deleteRequestButton.setText("Request to Return");

                            mapButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hasSelectedMap = true;
                                    Intent intent = new Intent(SentRequestActivity.this, MapsActivity.class);
                                    intent.putExtra("REQUESTTYPE", 0);
                                    intent.putExtra("REQUEST", currentRequest);
                                    startActivityForResult(intent, 0);
                                }
                            });

                            deleteRequestButton.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   if (hasSelectedMap) {
                                       currentRequest.changeToReturnRequest();
                                       db.collection("requests")
                                               .whereEqualTo("bookRequestedID", currentRequest.getBookRequestedID())
                                               .get()
                                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           for (QueryDocumentSnapshot document : task.getResult()) {
                                                               //Log.d("TEMP", document.getId() + " => " + document.getData());
                                                               Request tempRequest = document.toObject(Request.class);
                                                               String documentID = document.getId();
                                                               if (currentRequest.getReqSenderID().equals(tempRequest.getReqSenderID())) {
                                                                   db.collection("requests").document(documentID).set(currentRequest);
                                                               }
                                                           }
                                                       } else {
                                                           Log.d("TEMP", "Error getting documents: ", task.getException());
                                                       }
                                                   }
                                               });

                                       // Reduce number of requests the book has
                                       db.collection("books").document(currentBook.getBookID())
                                               .update("numberOfRequests", currentBook.getNumberOfRequests() - 1);

                                       // Create a Notification that will be for the book owner
                                       notificationMessage = "%s wants to return your book %s.";
                                       db.collection("users").document(user.getUid())
                                               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               DocumentSnapshot document = task.getResult();
                                               if (document.exists()) {
                                                   User thisUser = document.toObject(User.class); // thisUser refers to the user who pressed on the request return button
                                                   notificationMessage = String.format(notificationMessage, thisUser.getUsername(), currentBook.getTitle());
                                                   String timestamp = java.text.DateFormat.getDateInstance().format(new Date());

                                                   Notification notification = new Notification(NotificationType.RETURN, notificationMessage, timestamp, currentRequest.getBookRequestedID(), currentBook.getOwnerID());
                                                   db.collection("notifications").add(notification);
                                               }
                                           }
                                       });

                                       // Display toast to notify user and exit out of the request
                                       Toast toast = Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT);
                                       toast.show();
                                       finish();
                                   } else {
                                       Snackbar sb = Snackbar.make(v, "Please select a location!", Snackbar.LENGTH_SHORT);
                                       sb.show();
                                   }
                               }
                            });
                        } else if (bookStatus.equals("Accepted")) {
                            requestStatusText.setText("Accepted");
                            requestStatusText.setTextColor(getResources().getColor(R.color.accepted_yellow));
                            mapButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SentRequestActivity.this, MapsActivity.class);
                                    intent.putExtra("REQUESTTYPE", 1);
                                    intent.putExtra("REQUEST", currentRequest);
                                    startActivity(intent);
                                }
                            });
                            deleteRequestButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        // setup buttons & book click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SentRequestActivity.this, PublicBookViewActivity.class);
                intent.putExtra("BOOK_ID", currentRequest.getBookRequestedID());
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SentRequestActivity.this, PublicProfileViewActivity.class);
                intent.putExtra("USER", requestReceiver);
                startActivity(intent);
            }
        });

        // In order, the if statements go: when user has requested to return,
        // when the request has not been accepted
        if (currentRequest.isReturnRequest()) {
            requestStatusText.setText("Returning");
            requestStatusText.setTextColor(getResources().getColor(R.color.requested_blue));
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SentRequestActivity.this, MapsActivity.class);
                    intent.putExtra("REQUESTTYPE", 1);
                    intent.putExtra("REQUEST", currentRequest);
                    startActivity(intent);
                }
            });
            deleteRequestButton.setVisibility(View.INVISIBLE);
        } else if ((currentRequest.getLatitude() == 1000.0) && (currentRequest.getLongitude() == 1000.0)) {
            requestStatusText.setText("Request Not Accepted");
            requestStatusText.setTextColor(getResources().getColor(R.color.available_green));
            mapButton.setVisibility(View.INVISIBLE);

            deleteRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("requests")
                            .whereEqualTo("reqSenderID", currentRequest.getReqSenderID())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Request tempRequest = document.toObject(Request.class);
                                            if ((currentRequest.getReqReceiverID().equals(tempRequest.getReqReceiverID())) &&
                                                    (currentRequest.getBookRequestedID().equals(tempRequest.getBookRequestedID())))
                                            {
                                                String documentID = document.getId();
                                                db.collection("requests").document(documentID)
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("TEMP", "DocumentSnapshot successfully deleted!");
                                                                Toast toast = Toast.makeText(getApplicationContext(), "Request Deleted", Toast.LENGTH_SHORT);
                                                                toast.show();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("TEMP", "Error deleting document", e);
                                                            }
                                                        });
                                                // Reduce number of requests the book has
                                                db.collection("books").document(currentBook.getBookID())
                                                        .update("numberOfRequests", currentBook.getNumberOfRequests() - 1);
                                                if (currentBook.getNumberOfRequests() - 1 == 0 && !currentRequest.isReturnRequest()) {
                                                    // Update book status
                                                    db.collection("books").document(currentBook.getBookID())
                                                            .update("status", "Available");
                                                }
                                            }
                                        }
                                    } else {
                                        Log.d("TEMP", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    // Create a Notification that will be for the requester
                    notificationMessage = "%s has deleted their request for your book %s.";
                    db.collection("users").document(user.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User thisUser = document.toObject(User.class); // thisUser refers to the user who pressed on the delete request button
                                notificationMessage = String.format(notificationMessage, thisUser.getUsername(), currentBook.getTitle());
                                String timestamp = java.text.DateFormat.getDateInstance().format(new Date());

                                Notification notification = new Notification(NotificationType.DELETE, notificationMessage, timestamp, currentRequest.getBookRequestedID(), currentBook.getOwnerID());
                                db.collection("notifications").add(notification);
                            }
                        }
                    });
                }
            });
        }
    }

    // Get the last location from the MapsActivity and store it.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        currentRequest = (Request) data.getSerializableExtra("NEW_REQUEST");
    }
}