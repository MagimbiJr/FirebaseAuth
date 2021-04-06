package com.tana.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mAuth;
    TextInputEditText mEmailAddress, mPassword;
    AppCompatButton mSignButton;
    TextView mRecoverPassword, mResendVerLink;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mEmailAddress = (TextInputEditText) findViewById(R.id.dialog_email_input);
        mPassword = (TextInputEditText) findViewById(R.id.dialog_password_input);
        mSignButton = (AppCompatButton) findViewById(R.id.sign_in_button);
        mRecoverPassword = (TextView) findViewById(R.id.to_recover_password);
        mResendVerLink = (TextView) findViewById(R.id.resend_verification_link);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_in_progress_bar);

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailAddress.getText().toString();
                String password = mPassword.getText().toString();
                handleSignIn(email, password);
            }
        });

        mResendVerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailVerificationDialog dialog = new EmailVerificationDialog();
                dialog.show(getSupportFragmentManager(), "resend link verification");
            }
        });

        setupAuthListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void setupAuthListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email sent to you to verify your email ", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                }
            }
        };
    }

    private void handleSignIn(String email, String password) {
        //Check for null inputs
        if (!isEmpty(mEmailAddress.getText().toString()) && !isEmpty(mPassword.getText().toString())) {
            showProgressBar();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        hideProgressBar();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                    hideProgressBar();
                }
            });
        }
    }

    private void showProgressBar() { mProgressBar.setVisibility(View.VISIBLE); }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isEmpty(String string) { return string.equals(""); }
}