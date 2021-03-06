package com.example.bookme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText emailID, password;
    Button loginButton;
    Button loginButtonAdmin;
    TextView signUp;
    FirebaseAuth mFirebaseAuth;
    ProgressBar progressBar;

    FirebaseDatabase mFirebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        createNotificationChannel();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this.getApplicationContext());
        super.onCreate(savedInstanceState);

        createNotificationChannel();
        setContentView(R.layout.activity_login);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        loginButtonAdmin = findViewById(R.id.loginButtonAdmin);
        signUp = findViewById(R.id.signUpRedirect);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    startActivity(new Intent(LoginActivity.this, HomePage.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT);
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Please enter email");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please insert password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT);
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener <AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task < AuthResult > task) {
                            if (!task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Sucessful login!", Toast.LENGTH_LONG);
                                toast.show();

                                startActivity(new Intent(LoginActivity.this, HomePage.class));
                            }
                        }
                    });
                }
            }
        });

        loginButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Please enter email");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please insert password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT);
                }
                else if (!email.equals("admin@email.com")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Admin credentials are wrong!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener <AuthResult> () {
                        @Override
                        public void onComplete(@NonNull Task < AuthResult > task) {
                            if (!task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Sucessful login!", Toast.LENGTH_LONG);
                                toast.show();
                                startActivity(new Intent(LoginActivity.this, HomePage.class));
                            }
                        }
                    });
                }
            }
        });

        // click signup button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "0";
            String description = "0";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}