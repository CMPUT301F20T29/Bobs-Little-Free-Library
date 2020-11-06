package com.example.bobslittlefreelibrary.views.templates;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.R;

public class SomeAssetsActivity extends AppCompatActivity {

    private Button backButton;
    private Button popUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_assets);

        backButton = findViewById(R.id.back_button);
        popUpButton = findViewById(R.id.pop_up);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SomeAssetsActivity.this, MainActivity.class);
                SomeAssetsActivity.this.startActivity(intent);
            }
        });

        popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SomeAssetsActivity.this)
                        .setTitle("Would you like to Continue?")
                        .setMessage("A message goes here")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }
}