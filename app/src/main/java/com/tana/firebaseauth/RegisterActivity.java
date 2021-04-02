package com.tana.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    FirebaseAuth mAuth;
    TextInputEditText mFullName, mEmailAddress, mPassword, mConfirmPassword;
    AppCompatButton mSignUpButton;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = (TextInputEditText) findViewById(R.id.full_name_input);
        mEmailAddress = (TextInputEditText) findViewById(R.id.reg_email_input);
        mPassword = (TextInputEditText) findViewById(R.id.reg_password_input);
        mConfirmPassword = (TextInputEditText) findViewById(R.id.confirm_password_input);
        mSignUpButton = (AppCompatButton) findViewById(R.id.sign_up_btn);
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });

    }

    private void handleRegistration() {
        //Check for null inputs
        if (!isEmpty(mFullName.getText().toString())
        && !isEmpty(mEmailAddress.getText().toString()) && !isEmpty(mPassword.getText().toString())
        && !isEmpty(mConfirmPassword.getText().toString())) {
            //Check if passwords match
            if (passwordMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())) {
                registerUser(mEmailAddress.getText().toString(), mPassword.getText().toString());
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(String email, String password) {
        showProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mAuth.signOut();
                    redirectActivity();
                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                hideProgressBar();
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Email verification has been sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not send email verification", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void redirectActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean passwordMatch(String s1, String s2) { return s1.equals(s2); }

    private boolean isEmpty(String string) { return string.equals(""); }
}