package com.example.bobslittlefreelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bobslittlefreelibrary.templates.BlankTemplateActivity;
import com.example.bobslittlefreelibrary.templates.FormTemplateActivity;
import com.example.bobslittlefreelibrary.templates.SomeAssetsActivity;

public class MainActivity extends AppCompatActivity {

    private Button formTemplateButton;
    private Button blankTemplateButton;
    private Button someAssetsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formTemplateButton = findViewById(R.id.form_template);
        blankTemplateButton = findViewById(R.id.blank_template);
        someAssetsButton = findViewById(R.id.some_assets);

        formTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormTemplateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        blankTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlankTemplateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        someAssetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SomeAssetsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }



}