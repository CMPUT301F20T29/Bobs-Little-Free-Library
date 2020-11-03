package com.example.bobslittlefreelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.templates.BlankTemplateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);

        EditText usernameEditText = findViewById(R.id.username_signup);
        EditText emailEditText = findViewById(R.id.email_signup);
        EditText passwordEditText = findViewById(R.id.password_signup);
        EditText confirmPassEditText = findViewById(R.id.confirm_password);
        EditText addressEditText = findViewById(R.id.address_signup);

        Button backButton = findViewById(R.id.back_button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button signupButton = findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String username = usernameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirmPass = confirmPassEditText.getText().toString();
                final String address = addressEditText.getText().toString();
                if (checkSignupInfo(username, email, password, confirmPass, address)) {
                    // change layout accordingly to use email as longin id instead of username
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(SignupActivity.this, "Signup success.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignupActivity.this, "Failed to signup",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean checkSignupInfo(String username, String email, String password, String confirmPass, String address){
        // checks when signing up:
        // email format = alphanumeric@alphanumeric
        // username format = alphanumeric within 12 characters
        // password format = more than 6 characters
        // confirm pass format = same as password
        // address format = no format at the moment
        return true;
    }

    public void updateUI(FirebaseUser user){
        if (user != null){
            finish();
        }
    }


}