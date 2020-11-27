package com.example.bobslittlefreelibrary.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.controllers.NotificationAdapter;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.models.Notification;
import com.example.bobslittlefreelibrary.views.books.MyBookViewActivity;
import com.example.bobslittlefreelibrary.views.books.PublicBookViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private ArrayList<Notification> listOfNotifications;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ListView notificationView;
    private ArrayList<String> listOfNotifIDs;
    private ArrayList<Book> listOfNotifBooks;
    private NotificationAdapter notificationAdapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // get user and db
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // init lists
        listOfNotifications = new ArrayList<>();
        listOfNotifIDs = new ArrayList<>();
        listOfNotifBooks = new ArrayList<>();

        // get views
        notificationView = findViewById(R.id.notifs_list_view);
        backButton = findViewById(R.id.notifs_back_button);

        // Setup adapter for requests overview
        notificationAdapter = new NotificationAdapter(this, listOfNotifications);
        notificationView.setAdapter(notificationAdapter);

        // fill lists
        ArrayList<Task<QuerySnapshot>> taskArrayList = new ArrayList<>();
        db.collection("notifications")
                .whereEqualTo("userID", user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Notification notification = document.toObject(Notification.class);
                            listOfNotifications.add(notification);
                            listOfNotifIDs.add(document.getId());
                            notificationAdapter.notifyDataSetChanged();
                        }
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Setup Requests Overview listeners
        /*notificationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Select which activity to go to based on the owner of the book.
                Notification notification = listOfNotifications.get(position);
                Book currentBook = listOfNotifBooks.get(position);
                if (currentBook.getOwnerID().equals(user.getUid())) {
                    Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                    intent.putExtra("BOOK_ID", notification.getBookID());
                    intent.putExtra("BOOK", currentBook);  // Send book to be displayed in book view activity
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                    intent.putExtra("BOOK_ID", notification.getBookID());
                    intent.putExtra("BOOK", currentBook);
                    startActivity(intent);
                }
            }
        });*/
        // Listener for deleting a notification
        notificationView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NOTIF", "Start deleting notification");
                // Delete the notification from db
                Notification notificationToRemove = (Notification) notificationView.getItemAtPosition(position);
                String notifIDToRemove = listOfNotifIDs.get(position);
                db.collection("notifications").document(listOfNotifIDs.get(position)).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Notification Deleted", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                // Delete all data related to the notification
                listOfNotifications.remove(notificationToRemove);
                listOfNotifIDs.remove(notifIDToRemove);
                notificationAdapter.notifyDataSetChanged();

                Log.d("NOTIF", " deleting notification finished");
                return true;
            }
        });
    }
}