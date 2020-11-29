package com.example.bobslittlefreelibrary.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;

import java.util.ArrayList;
import java.util.List;

// Adapter for books in the SearchActivity and BooksFragment
public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Context context, ArrayList<Book> bookSearchList) {
        super(context,0, bookSearchList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The book to display
        Book book = getItem(position);

        // Initializing the book cell view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_cell, parent, false);
        }

        // Get views
        TextView bookName = (TextView) convertView.findViewById(R.id.bookSearchName);
        TextView bookStatus = (TextView) convertView.findViewById(R.id.bookSearchStatus);
        TextView owner = (TextView) convertView.findViewById(R.id.owner);
        ImageView bookSearchImage = (ImageView) convertView.findViewById(R.id.bookSearchImage);

        // Set TextViews
        bookStatus.setText(book.getStatus());
        bookStatus.setText(book.getStatus());
        bookName.setText(book.getTitle());
        String ownerText = "Owned by " + book.getOwnerUsername();
        owner.setText(ownerText);

        // Set books's image
        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookSearchImage).execute(pictureURL);
        }

        return convertView;
    }
}
