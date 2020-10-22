package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bobslittlefreelibrary.templates.FormTemplateActivity;

public class AddBookActivity extends AppCompatActivity implements ScanFragment.OnFragmentInteractionListener {

    final String TAG = "AddBookActivity";

    private Button backButton;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        backButton = findViewById(R.id.back_button);
        scanButton = findViewById(R.id.scan_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                AddBookActivity.this.startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanFragment().show(getSupportFragmentManager(), "SCAN");
            }
        });
    }

    @Override
    public void onIsbnFound(String isbn) {
        Log.d(TAG, "onIsbnFound: " + isbn);
    }
}