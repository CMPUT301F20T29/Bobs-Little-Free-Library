package com.example.bobslittlefreelibrary.views.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_view);

        TextView name = findViewById(R.id.user_name);
        TextView email = findViewById(R.id.user_email);
        TextView addy = findViewById(R.id.user_address);
        TextView phone = findViewById(R.id.phone);
        TextView bio = findViewById(R.id.bio);
        Button backButton = findViewById(R.id.back_button);
        Button logOutButton = findViewById(R.id.log_out);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyProfileViewActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("USER");

        Button editBtn = (Button) findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditProfileFragment().show(getSupportFragmentManager(), "EDIT_PROFILE");
            }
        });

        name.setText(user.getUsername());
        email.setText(user.getEmail());
        addy.setText(user.getAddress());
        if (user.getPhoneNumber()!=null){
            phone.setText(user.getPhoneNumber());
        }
        if(user.getBio()!=null){
            bio.setText(user.getBio());
        }

    }
}
