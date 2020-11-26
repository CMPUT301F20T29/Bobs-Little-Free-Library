package com.example.bobslittlefreelibrary.views.users;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.EditProfileFragment;
import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        if (user.getPhone()!=null){
            phone.setText(user.getPhone());
        }
        if(user.getBio()!=null){
            bio.setText(user.getBio());
        }

    }
}
