package com.example.bobslittlefreelibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        TextView tv = (TextView) convertView.findViewById(R.id.bookSearchName);
        ImageView iv = (ImageView) convertView.findViewById(R.id.bookSearchImage);

        tv.setText(book.getTitle());
        //iv.setImageResource();

        return convertView;
    }
}
