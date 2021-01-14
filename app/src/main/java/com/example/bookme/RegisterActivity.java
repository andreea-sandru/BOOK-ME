package com.example.bookme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookme.ObjectModels.UserObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity  {
    private EditText emailID, password, user_fullname;
    private Button registerButton;
    private TextView login;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

        // initializari
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        user_fullname = findViewById(R.id.fullname_register);
        registerButton = findViewById(R.id.signupbutton);
        login = findViewById(R.id.login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_register);

        // listener pentru butonul de register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailID.getText().toString();
                final String name = user_fullname.getText().toString();
                final String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailID.setError("Please enter email");
                    emailID.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please insert password");
                    password.requestFocus();
                } else if (name.isEmpty()) {
                    user_fullname.setError("Please insert full name");
                    user_fullname.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    // creare user nou (cu email) Firebase
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task< AuthResult > task) {
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } else {
                                    // userul este creat + autentificat cu Firebase => extragem Id-ul lui unic:
                                    final String uid = mFirebaseAuth.getCurrentUser().getUid();

                                    // userul exista in Firebase dar stocam datele suplimentare in Baza de date:
                                    String userType = "admin";
                                    UserObject newUser = new UserObject(uid, name, email, userType);
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                    dbRef.child("users").child(uid).setValue(newUser);

                                    // redirectare HomePage + afisare mesaj success
                                    startActivity(new Intent(RegisterActivity.this, HomePage.class));
                                    Toast toast = Toast.makeText(getApplicationContext(), "Sign up succesful!", Toast.LENGTH_LONG);
                                    toast.show();
                            }
                        }
                    });
                }
            }
        });

        // click buton login => redirectare pagina login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}