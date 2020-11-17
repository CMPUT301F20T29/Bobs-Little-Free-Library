package com.example.bobslittlefreelibrary.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Notification;
import com.example.bobslittlefreelibrary.models.NotificationType;
import com.example.bobslittlefreelibrary.views.HomeFragment;
import com.example.bobslittlefreelibrary.views.books.BooksFragment;
import com.example.bobslittlefreelibrary.views.requests.RequestsFragment;

import java.util.ArrayList;

/**
 * This class is a custom adapter for Notification objects which allows us to take Notifications and
 * display information about them in a ListView.
 * */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    private ArrayList<Notification> notifications;
    private Context context;

    public NotificationAdapter(@NonNull Context context, ArrayList<Notification> notifications){
        super(context,0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_notification, parent, false);
        }

        // Get notification object
        Notification notification = notifications.get(position);

        // Get references to UI
        ImageView notificationIcon = view.findViewById(R.id.notification_image);
        TextView notificationMessage = view.findViewById(R.id.notification_message);
        TextView notificationTime = view.findViewById(R.id.notification_timestamp);

        // Set UI Values
        switch (notification.getType()) {
            case REQUEST:
                notificationIcon.setImageResource(R.drawable.ic_baseline_notification_important_24);
                break;
            case ACCEPT:
                notificationIcon.setImageResource(R.drawable.ic_baseline_check_circle_24);
                break;
            case DECLINE:
                notificationIcon.setImageResource(R.drawable.ic_baseline_cancel_24);
                break;
        }

        notificationMessage.setText(notification.getMessage());
        notificationTime.setText(notification.getTimestamp());

        return view;
    }
}
