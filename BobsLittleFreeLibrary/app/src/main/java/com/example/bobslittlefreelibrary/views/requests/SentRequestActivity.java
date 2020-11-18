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
import com.google.android.gms.maps.model.LatLng;
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

        // query for the username
        db.collection("users")
                .document(currentRequest.getReqReceiverID())
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

                           Notification notification = new Notification(NotificationType.DELETE, notificationMessage, timestamp.toString(), currentRequest.getBookRequestedID(), currentBook.getOwnerID());
                           db.collection("notifications").add(notification)
                                   .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                       @Override
                                       public void onSuccess(DocumentReference documentReference) {
                                           String notificationID = documentReference.getId();
                                           // Get document of the user we want to send notification to (i.e. the person who sent the request)
                                           db.collection("users").document(currentRequest.getReqReceiverID())
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
                Intent intent = new Intent(SentRequestActivity.this, PublicBookViewActivity.class);
                intent.putExtra("BOOK_ID", currentRequest.getBookRequestedID());
                intent.putExtra("BOOK", currentBook);
                startActivity(intent);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEMP", "Borrower Profile button pressed");
                //TODO REMOVE
                Log.d("TEMP", currentRequest.getLatitude() + "hehe");
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SentRequestActivity.this, MapsActivity.class);
                intent.putExtra("REQUESTTYPE", 1);
                intent.putExtra("REQUEST", currentRequest);
                startActivity(intent);
            }
        });

        // In order, the if statements go: when user has requested to return,
        // when the request has not been accepted, when the book has been exchanged already to the
        // borrower, and when the book has been accepted but not exchanged yet.
        if (currentRequest.isReturnRequest()) {
            // TODO: Make bottom button disappear and map button to view return location
        } else if ((currentRequest.getLatitude() == 1000.0) && (currentRequest.getLongitude() == 1000.0)) {
            mapButton.setText(R.string.request_not_accepted);
            mapButton.setClickable(false);
        } else if (currentBook.getStatus().equals("Borrowed")) {
            //TODO: Set map button to be able to select a location & bottom button to send return request
            // LOCATATION DEFAULT TO ORIGINAL SPOT, BUT USER CAN SWITCH
        } else {
            // TODO: Be able to view the map for the location but bottom buttons gone
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