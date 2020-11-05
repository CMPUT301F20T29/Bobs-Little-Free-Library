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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomRequestsAdapter extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;
    private Boolean isSentTab;          // used to know which tab we're on

    public CustomRequestsAdapter(Context context, ArrayList<Request> requests, Boolean isSentTab) {
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

        Request request = requests.get(position);
        ImageView bookImage = view.findViewById(R.id.bookImage);
        TextView bookName = view.findViewById(R.id.book_text);
        TextView userTextView = view.findViewById(R.id.name_text);

        // this is setting up data base to query for users
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            db.collection("users")
                    .document(request.getReqReceiverID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userTextView.setText("To: " + documentSnapshot.get("username"));
                        }
                    });
        } else {
            db.collection("users")
                    .document(request.getReqSenderID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userTextView.setText("From: " + documentSnapshot.get("username"));
                        }
                    });
        }
        return view;
    }

    public void setSentTab(Boolean sentTab) {
        isSentTab = sentTab;
    }
}
