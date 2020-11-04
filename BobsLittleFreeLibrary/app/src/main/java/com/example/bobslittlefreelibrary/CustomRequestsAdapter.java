package com.example.bobslittlefreelibrary;

/**
 * This class is the adapter for all requests in
 * the list of requests for RequestsFragment
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bobslittlefreelibrary.utils.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomRequestsAdapter extends ArrayAdapter<Requests> {

    private ArrayList<Requests> requests;
    private Context context;
    private Boolean isSentTab;          // used to know which tab we're on

    public CustomRequestsAdapter(Context context, ArrayList<Requests> requests, Boolean isSentTab) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
        this.isSentTab = isSentTab;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        Requests request = requests.get(position);
        ImageView bookImage = view.findViewById(R.id.bookImage);
        TextView bookName = view.findViewById(R.id.book_text);
        TextView userTextView = view.findViewById(R.id.name_text);

        // this is setting up data base to query for users
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("users");

        // If it's the sent tab, show who you are sending the request to
        // if it's received tab, show who is sending it to the you

        bookName.setText(request.getBookTitle());
        String pictureURL = request.getBookImageURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookImage).execute(pictureURL);
        } else {
            bookImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
        }

        if (isSentTab) {
            usersCollectionRef
                    .whereEqualTo("userID", request.getReqReceiverID())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d("TEMP", document.getId() + " => " + document.getData());
                                    userTextView.setText("To: " + document.toObject(User.class).getUsername());
                                }
                            } else {
                                Log.d("TEMP", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            usersCollectionRef
                    .whereEqualTo("userID", request.getReqSenderID())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d("TEMP", document.getId() + " => " + document.getData());
                                    userTextView.setText("From: " + document.toObject(User.class).getUsername());
                                }
                            } else {
                                Log.d("TEMP", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        return view;
    }

    public void setSentTab(Boolean sentTab) {
        isSentTab = sentTab;
    }
}
