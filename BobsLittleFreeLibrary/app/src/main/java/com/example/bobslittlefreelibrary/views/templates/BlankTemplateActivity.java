package com.example.bobslittlefreelibrary.views.templates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bobslittlefreelibrary.views.MainActivity;
import com.example.bobslittlefreelibrary.R;

public class BlankTemplateActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_template);

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlankTemplateActivity.this, MainActivity.class);
                BlankTemplateActivity.this.startActivity(intent);
            }
        });
    }
}