package com.example.bobslittlefreelibrary.views.users;

import android.app.AppComponentFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_view);
    }
}
