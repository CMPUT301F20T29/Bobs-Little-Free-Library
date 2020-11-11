package com.example.bobslittlefreelibrary.views.books;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.bobslittlefreelibrary.controllers.CustomList;
import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;


/**
 *  This fragment manages all interactions and data displayed within
 *  the Books screen.
 *
 *  Accesses Firebase and displays all books that belong to the current User
 *  list view.
 *
 *  Clicking on any Book displayed by the Books screen will call upon
 *  PublicBookView or MyBookView appropriately
 *
 *  TODO: Add filter functionality
 *
 *  Add FAB starts AddBookActivity
 */
public class BooksFragment extends Fragment{

    //Instantiate List of books and firebase variables
    ListView bookList;
    ArrayList<Book> dataList;
    ArrayList<String> bookIDList;
    ArrayAdapter<Book> bookAdapter;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.books_fragment, container, false);
    }

    /**
     * onResume is the point at which the code begins when another post-occuring
     * activity ends and returns to BooksFragment
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setLastActiveTab("BOOKS");

        bookList = getActivity().findViewById(R.id.bookList);
        dataList = new ArrayList<>();
        bookIDList = new ArrayList<>();
        bookAdapter = new CustomList(getContext(), dataList);
        bookList.setAdapter(bookAdapter);

        TextView titleCard = getActivity().findViewById(R.id.sectionText);
        titleCard.setText("Books");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        /*
         *  Accesses database and Uses Querying to select books where ownerID is the Users Id
         *  Then adds them to the listview of books
         *
         * Exception handling in case the database is inaccessible
         */
        db.collection("books").whereEqualTo("ownerID", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " +document.getData());
                                Book book = document.toObject(Book.class);
                                dataList.add(book);
                                bookIDList.add(document.getId());
                                bookAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //Click listener linking to AddBookActivity
        final FloatingActionButton addItemButton = getActivity().findViewById(R.id.add_Item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                startActivity(intent);
            }
        });
        //Click listener linking to filter activity
        Button filterButton = getActivity().findViewById(R.id.filterAllButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Add filter button actions
                //TextView status = getActivity().findViewById(R.id.statusText);
                //status.setText("/*Skippidi-pap-pap*/");
            }
        });
        // In event of an element of the list of books being clicked either
        // PublicBookView or MyBookView are called
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Select which activity to go to based on owner of book.
                if (user.getUid().equals(dataList.get(position).getOwnerID())) {
                    Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                    intent.putExtra("BOOK_ID", bookIDList.get(position));
                    intent.putExtra("BOOK", dataList.get(position));  // Send book to be displayed in book view activity
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                    intent.putExtra("BOOK_ID", bookIDList.get(position));
                    intent.putExtra("BOOK", dataList.get(position));
                    startActivity(intent);
                }

            }
        });
    }
}
