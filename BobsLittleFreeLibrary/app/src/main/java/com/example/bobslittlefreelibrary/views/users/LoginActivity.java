package com.example.bobslittlefreelibrary.views.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        //if (mAuth.getCurrentUser()!=null){
        //    updateUI(mAuth.getCurrentUser());
        //}

        // get views
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button signinButton = findViewById(R.id.login_button);

        signinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                boolean validInput = !(email.isEmpty() || password.isEmpty()
                        || email.length() > 100 || password.length() > 100);

                if (validInput) {
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
                                        Snackbar.make(v, "Incorrect email or password", Snackbar.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                } else {
                    showInvalidInputSnackbar(v);
                }

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

    private void showInvalidInputSnackbar(View v){

        String msg = "Login failed, please fix the following issues to log in:\n";

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.length() > 100) { msg += "\n - Email is too long"; }
        if (password.length() > 100) { msg += "\n - Password is too long"; }
        if (email.isEmpty()) { msg += "\n - Email is empty"; }
        if (password.isEmpty()) { msg += "\n - Password is empty"; }

        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(6);
        sb.show();
    }

}