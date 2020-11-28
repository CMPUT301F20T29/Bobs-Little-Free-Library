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

// Adapter for books in the Search Activity
public class BookAdapter extends ArrayAdapter<Book> {
    private Boolean search;

    public BookAdapter(Context context, ArrayList<Book> bookSearchList, Boolean search) {
        super(context,0, bookSearchList);
        this.search = search;
    }

    public BookAdapter(Context context, ArrayList<Book> bookSearchList) {
        super(context,0, bookSearchList);
        this.search = true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book book = getItem(position);

        // Initializing the book cell view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_cell, parent, false);
        }

        TextView bookName = (TextView) convertView.findViewById(R.id.bookSearchName);
        TextView bookStatus = (TextView) convertView.findViewById(R.id.bookSearchStatus);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.bookSearchAuthor);
        ImageView bookSearchImage = (ImageView) convertView.findViewById(R.id.bookSearchImage);

        if (this.search) {
            String msg = book.getStatus() + " to borrow from " + book.getOwnerUsername();
            bookStatus.setText(msg);
        } else {
            bookStatus.setText(book.getStatus());
        }
        
        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());

        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookSearchImage).execute(pictureURL);
        }

        return convertView;
    }
}
