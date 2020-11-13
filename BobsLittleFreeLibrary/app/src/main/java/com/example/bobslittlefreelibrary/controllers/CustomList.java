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
import com.example.bobslittlefreelibrary.models.Book;

import java.util.ArrayList;


public class  CustomList extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;

    public CustomList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        Book book = books.get(position);
        String pictureURL = book.getPictureURL();

        //ImageView bookImage = view.findViewById(R.id.bookImage);
        TextView bookName = view.findViewById(R.id.book_text);
        TextView authorName = view.findViewById(R.id.name_text);
        ImageView picture = view.findViewById(R.id.bookImage);




        //TODO: Set Image View
        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        if (pictureURL != null) {
            new DownloadImageTask(picture).execute(pictureURL);
        } else {
            picture.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
        }


        return view;
    }
}