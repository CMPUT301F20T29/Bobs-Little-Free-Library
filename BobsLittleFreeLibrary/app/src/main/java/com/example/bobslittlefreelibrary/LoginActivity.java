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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        Button signinButton = findViewById(R.id.login_button);
        signinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                Log.d("account",email+" and "+password);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Incorrect email or password.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        Button signupButton = findViewById(R.id.signUpBtn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }


    public void updateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // onStart() checks if user is already logged in; if yes, move to main activity
        // for now, commented out cuz I need to write more code
        //updateUI(currentUser);
    }
}