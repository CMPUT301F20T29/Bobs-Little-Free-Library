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

import java.util.List;

// Adapter for books in the Search Activity
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, int resource, List<Book> bookSearchList) {
        super(context, resource, bookSearchList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book book = getItem(position);

        // Initializing the book cell view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_cell, parent, false);
        }

        TextView bookName = (TextView) convertView.findViewById(R.id.bookSearchName);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.bookSearchAuthor);
        ImageView bookSearchImage = (ImageView) convertView.findViewById(R.id.bookSearchImage);

        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());

        String pictureURL = book.getPictureURL();
        if (pictureURL != null) {
            new DownloadImageTask(bookSearchImage).execute(pictureURL);
        }

        return convertView;
    }
}
