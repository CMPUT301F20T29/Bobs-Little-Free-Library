package com.example.bobslittlefreelibrary.views.requests;

/**
 * This class is the activity for received requests
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class ReceivedRequestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Request currentRequest;
    private LinearLayout bookInfoContainer;
    private ImageView bookImage;
    private TextView titleText;
    private TextView authorText;
    private TextView ISBNText;
    private Button userProfileButton;
    private Button acceptRequestButton;
    private Button declineRequestButton;
    private Button backButton;
    private Button mapButton;
    private Book currentBook;
    private String notificationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);

        // get the request object from the intent and set userid
        Intent intent = getIntent();
        currentRequest = (Request) intent.getSerializableExtra("REQUEST");

        // getting references to the objects
        bookInfoContainer = findViewById(R.id.book_info_container);
        bookImage = findViewById(R.id.book_view_image);
        titleText = findViewById(R.id.book_view_title);
        authorText = findViewById(R.id.book_view_author);
        ISBNText = findViewById(R.id.book_view_ISBN);
        userProfileButton = findViewById(R.id.requester_profile_button);
        acceptRequestButton = findViewById(R.id.accept_request_button);
        declineRequestButton = findViewById(R.id.decline_request_button);
        backButton = findViewById(R.id.back_button);
        mapButton = findViewById(R.id.open_map_button);

        // query for the username
        db.collection("users")
                .document(currentRequest.getReqSenderID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userProfileButton.setText((String)documentSnapshot.get("username"));
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
                        } else {
                            bookImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                        }
                        titleText.setText((String)documentSnapshot.get("title"));
                        authorText.setText((String)documentSnapshot.get("author"));
                        ISBNText.setText((String)documentSnapshot.get("isbn"));
                    }
                });

        // setup buttons & book click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO REMEMBER TO ADD A TOAST FOR ACCEPTED / SNACKBAR IF IT DOESNT WORK PROPERLY LIKE LOCATION FALSE
                Log.d("TEMP", "Accept button pressed");



                // Create a Notification that will be for the requester  -- Note: Feel free to move this to a better spot if you have conditionals for if the accept passes or fails
                notificationMessage = "Your request for %s has been accepted by %s.";
                db.collection("users").document(user.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User thisUser = document.toObject(User.class); // thisUser refers to the user who pressed on the accept button
                            notificationMessage = String.format(notificationMessage, currentBook.getTitle(), thisUser.getUsername());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            Notification notification = new Notification(NotificationType.ACCEPT, notificationMessage, timestamp.toString(), currentRequest.getBookRequestedID(), currentBook.getOwnerID());
                            db.collection("notifications").add(notification)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String notificationID = documentReference.getId();
                                            // Get document of the user we want to send notification to (i.e. the person who sent the request)
                                            db.collection("users").document(currentRequest.getReqSenderID())
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    User userToSendNotif = documentSnapshot.toObject(User.class);

                                                    ArrayList<String> usersNotifs = userToSendNotif.getNotificationIDs();
                                                    usersNotifs.add(notificationID);

                                                    HashMap newBooksMap = new HashMap<String, ArrayList>();
                                                    newBooksMap.put("notificationIDs", usersNotifs);

                                                    db.collection("users").
                                                            document(user.getUid()).update(newBooksMap);
                                                }
                                            });
                                        }
                                    });
                        }
                    }
                });
            }
        });

        declineRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("requests")
                        .whereEqualTo("reqReceiverID", currentRequest.getReqReceiverID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Request tempRequest = document.toObject(Request.class);
                                        if ((currentRequest.getReqSenderID().equals(tempRequest.getReqSenderID())) &&
                                                (currentRequest.getBookRequestedID().equals(tempRequest.getBookRequestedID())))
                                        {
                                            String documentID = document.getId();
                                            db.collection("requests").document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("TEMP", "DocumentSnapshot successfully deleted!");
                                                            Toast toast = Toast.makeText(getApplicationContext(), "Request Declined", Toast.LENGTH_SHORT);
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
                                        }
                                    }
                                } else {
                                    Log.d("TEMP", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                // Create a Notification that will be for the requester
                notificationMessage = "Your request for %s has been declined by %s.";
                db.collection("users").document(user.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User thisUser = document.toObject(User.class); // thisUser refers to the user who pressed on the decline button
                            notificationMessage = String.format(notificationMessage, currentBook.getTitle(), thisUser.getUsername());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            Notification notification = new Notification(NotificationType.DECLINE, notificationMessage, timestamp.toString(), currentRequest.getBookRequestedID(), currentBook.getOwnerID());
                            db.collection("notifications").add(notification)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String notificationID = documentReference.getId();
                                            // Get document of the user we want to send notification to (i.e. the person who sent the request)
                                            db.collection("users").document(currentRequest.getReqSenderID())
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    User userToSendNotif = documentSnapshot.toObject(User.class);

                                                    ArrayList<String> usersNotifs = userToSendNotif.getNotificationIDs();
                                                    usersNotifs.add(notificationID);

                                                    HashMap newBooksMap = new HashMap<String, ArrayList>();
                                                    newBooksMap.put("notificationIDs", usersNotifs);

                                                    db.collection("users").
                                                            document(user.getUid()).update(newBooksMap);
                                                }
                                            });
                                        }
                                    });
                        }
                    }
                });
            }
        });

        bookInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this , MyBookViewActivity.class);
                intent.putExtra("BOOK_ID", currentRequest.getBookRequestedID());
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Borrower Profile button pressed");
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceivedRequestActivity.this, MapsActivity.class);
                intent.putExtra("REQUESTTYPE", 0);
                intent.putExtra("REQUEST", currentRequest);
                startActivity(intent);
            }
        });

        // In order, the if statements go: when user has requested to return,
        // when the request has not been accepted, when the book has been exchanged already to the
        // borrower, and when the book has been accepted but not exchanged yet.
        if (currentRequest.isReturnRequest()) {
            // TODO: Make bottom buttons disappear and map button to view return location
        } else if ((currentRequest.getLatitude() == 1000.0) && (currentRequest.getLongitude() == 1000.0)) {
            mapButton.setText(R.string.request_not_accepted);
            mapButton.setClickable(false);
        } else if (currentBook.getStatus().equals("Borrowed")) {
            //TODO: Set map button to be able to select a location & bottom button to send return request
        } else {
            // TODO: Be able to view the map for the location but bottom button gone
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        currentRequest = (Request) data.getSerializableExtra("NEW_REQUEST");
    }
}