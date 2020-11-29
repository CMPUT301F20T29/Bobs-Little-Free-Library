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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.controllers.BookAdapter;
import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Book;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.chip.Chip;


import com.google.android.material.chip.ChipGroup;
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
public class BooksFragment extends Fragment {

    //Instantiate List of books and firebase variables
    private ListView bookList;
    private ArrayList<Book> dataList;
    private ArrayAdapter<Book> bookAdapter;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ChipGroup chips;

    // filter checks
    boolean myBooksFilterSelected = false;
    boolean availableFilterSelected = false;
    boolean requestedFilterSelected = false;
    boolean acceptedFilterSelected  = false;
    boolean borrowedFilterSelected = false;


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
        bookAdapter = new BookAdapter(getContext(), dataList);
        bookList.setAdapter(bookAdapter);

        ArrayList<Book> filteredBooks = new ArrayList<Book>();

        chips = getActivity().findViewById(R.id.filterChips);
        chips.setVisibility(View.GONE);

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
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " +document.getData());
                                Book book = document.toObject(Book.class);
                                dataList.add(book);
                                bookAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("books").whereEqualTo("currentBorrowerID", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " +document.getData());
                                Book book = document.toObject(Book.class);
                                dataList.add(book);
                                bookAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Chip AllButton = getActivity().findViewById(R.id.filterAllChip);
        AllButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                filteredBooks.clear();
                for (Book book: dataList) {
                    filteredBooks.add(book);
                }
                BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                bookList.setAdapter(adapter);
            }
        });

        Chip AvailableBookButton = getActivity().findViewById(R.id.filterAvailableChip);
        AvailableBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(availableFilterSelected == false){
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        if(book.getStatus().toLowerCase().contains("available")){
                            filteredBooks.add(book);
                        }
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    availableFilterSelected = true;
                    myBooksFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                } else{
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        filteredBooks.add(book);
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }

            }
        });

        Chip RequestedButton = getActivity().findViewById(R.id.filterRequestedChip);
        RequestedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(requestedFilterSelected == false){
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        if(book.getStatus().toLowerCase().contains("requested")){
                            filteredBooks.add(book);
                        }
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    requestedFilterSelected = true;
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }else{
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        filteredBooks.add(book);
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }
            }
        });

        Chip AcceptedButton = getActivity().findViewById(R.id.filterAcceptedChip);
        AcceptedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (acceptedFilterSelected == false){
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        if(book.getStatus().toLowerCase().contains("accepted")){
                            filteredBooks.add(book);
                        }
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    acceptedFilterSelected = true;
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    borrowedFilterSelected = false;

                }else{
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        filteredBooks.add(book);
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }
            }
        });

        Chip BorrowedButton = getActivity().findViewById(R.id.filterBorrowedChip);
        BorrowedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(borrowedFilterSelected == false){
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        if(book.getStatus().toLowerCase().contains("borrowed")){
                            filteredBooks.add(book);
                        }
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    borrowedFilterSelected = true;
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;

                }else{
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        filteredBooks.add(book);
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }
            }
        });



        Chip MyBooksButton = getActivity().findViewById(R.id.filterMyBooksChip);
        MyBooksButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(myBooksFilterSelected == false){
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        if(book.getOwnerID().equals(user.getUid())){

                            filteredBooks.add(book);
                        }
                    }
                    BookAdapter adapterMy = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapterMy);

                    myBooksFilterSelected = true;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

                }else{
                    filteredBooks.clear();
                    for (Book book: dataList) {
                        filteredBooks.add(book);
                    }
                    BookAdapter adapter = new BookAdapter(getActivity().getApplicationContext(), filteredBooks);
                    bookList.setAdapter(adapter);
                    myBooksFilterSelected = false;
                    availableFilterSelected = false;
                    requestedFilterSelected = false;
                    acceptedFilterSelected  = false;
                    borrowedFilterSelected = false;

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
        Button filterButton = getActivity().findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chips.getVisibility() == View.VISIBLE) {
                    chips.setVisibility(View.GONE);
                    filterButton.setText("FILTER");
                } else {
                    chips.setVisibility(View.VISIBLE);
                    filterButton.setText("HIDE");
                }
            }
        });

        // In event of an element of the list of books being clicked either
        // PublicBookView or MyBookView are called
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = dataList.get(position);
                // Select which activity to go to based on owner of book.
                if (user.getUid().equals(selectedBook.getOwnerID())) {
                    Intent intent = new Intent(getActivity(), MyBookViewActivity.class);
                    intent.putExtra("BOOK", selectedBook);  // Send book to be displayed in book view activity
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PublicBookViewActivity.class);
                    intent.putExtra("BOOK", selectedBook);
                    startActivity(intent);
                }
            }
        });
    }
}
