package com.example.bobslittlefreelibrary.views.users;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPassEditText;
    private EditText addressEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.username_signup);
        emailEditText = findViewById(R.id.email_signup);
        passwordEditText = findViewById(R.id.password_signup);
        confirmPassEditText = findViewById(R.id.confirm_password);
        addressEditText = findViewById(R.id.address_signup);
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
                final String address = addressEditText.getText().toString();

                if (isInputValid()) {
                    // change layout accordingly to use email as longin id instead of username
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign up success, save user info and update UI
                                        db = FirebaseFirestore.getInstance();
//                                        Map<String, Object> userData = new HashMap<>();
//                                        userData.put("email", email);
//                                        userData.put("username", username);
//                                        userData.put("address", address);
                                        User userData = new User(username, email, address);
                                        db.collection("users").document(mAuth.getUid()).set(userData);

                                        Snackbar.make(v, "Signup success.",
                                                Snackbar.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Snackbar.make(v, "Failed to signup, account already exists.",
                                                Snackbar.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                } else {
                    showInvalidInputSnackbar(v);
                }
            }
        });
    }

    private boolean isInputValid(){
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPass = confirmPassEditText.getText().toString();
        String address = addressEditText.getText().toString();

        boolean underCharLimitCheck = !(username.length() > 12 || (email.length() > 100) ||
                (password.length() < 6) || (password.length() > 100) || address.length() > 100);
        boolean emptyCheck = !(username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                address.isEmpty());
        boolean passwordConfirmation = (confirmPass.equals(password));
        boolean emailFormat = (email.contains("@")) && !(email.contains(" "));
        boolean usernameFormat = (!username.contains(" "));

        return  underCharLimitCheck && emptyCheck && passwordConfirmation && emailFormat && usernameFormat;

    }

    public void updateUI(FirebaseUser user){
        if (user != null){
            finish();
        }
    }

    private void showInvalidInputSnackbar(View v){

        String msg = "Sign up failed, please fix the following issues to sign up:\n";

        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPass = confirmPassEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (!email.contains("@") || email.contains(" ")) {
            if (email.isEmpty()) {
                msg += "\n - Email is empty";
            } else {
                msg += "\n - Email is not in correct format";
            }
        }
        if (email.length() > 100) { msg += "\n - Email is too long"; }
        if (username.length() > 12) { msg += "\n - Username is too long"; }
        if (password.length() > 100) { msg += "\n - Password is too long"; }
        if (password.length() < 6) {
            if (password.isEmpty()) {
                msg += "\n - Password is empty";
            } else {
                msg += "\n - Password is too short";
            }
        }
        if (!password.isEmpty() && !confirmPass.equals(password)) { msg += "\n - Password confirmation is not the same as password"; }
        if (address.length() > 100) { msg += "\n - Address is too long"; }
        if (username.contains(" ")) {
            msg += "\n - Username contains empty space";
        }
        if (username.isEmpty()) { msg += "\n - Username is empty"; }
        if (address.isEmpty()) { msg += "\n - Address is empty"; }


        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View sbView = sb.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(8);
        sb.show();
    }
}