package com.example.bobslittlefreelibrary;

/**
 * This class is the adapter for all requests in
 * the list of requests for RequestsFragment
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        // If it's the sent tab, show who you are sending the request to
        // if it's received tab, show who is sending it to the you

        // TODO: Need to set images for the books

        if (isSentTab) {
            bookName.setText(request.getBookRequested().getTitle());
            userTextView.setText("To: " + request.getReqReceiver().getUsername());
        } else {
            bookName.setText(request.getBookRequested().getTitle());
            userTextView.setText("From: " + request.getReqSender().getUsername());
        }

        return view;
    }

    public void setSentTab(Boolean sentTab) {
        isSentTab = sentTab;
    }
}
