package com.example.bobslittlefreelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

public class BooksFragment extends Fragment{
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> dataList;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.books_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookList = getActivity().findViewById(R.id.bookList);
        dataList = new ArrayList<>();
        bookList.setAdapter(bookAdapter);

        TextView titleCard = getActivity().findViewById(R.id.sectionText);
        titleCard.setText("My Books");

        final FloatingActionButton addItemButton = getActivity().findViewById(R.id.add_Item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        Button filterButton = getActivity().findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Add filter button actions
                //TextView status = getActivity().findViewById(R.id.statusText);
                //status.setText("/*Skippidi-pap-pap*/");
            }
        });

        //TODO: firestore MYBooks books into listview

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }

}
