package com.example.bobslittlefreelibrary.views.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.User;

public class PublicProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile_view);

        TextView name = findViewById(R.id.user_name);
        TextView email = findViewById(R.id.user_email);
        TextView addy = findViewById(R.id.user_address);
        TextView phone = findViewById(R.id.phone);
        TextView bio = findViewById(R.id.bio);
        Button backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("USER");

        name.setText(user.getUsername());
        email.setText(user.getEmail());
        addy.setText(user.getAddress());
        if (user.getPhone()!=null){
            phone.setText(user.getPhone());
        }
        if(user.getBio()!=null){
            bio.setText(user.getBio());
        }

    }
}
