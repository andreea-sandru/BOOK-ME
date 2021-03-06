package com.example.bookme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;


import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.bookme.Fragments.AllBooksFragment;
import com.example.bookme.Fragments.AvailableFragment;
import com.example.bookme.Fragments.ReservedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class HomePage extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    private static final String adminId = "2QR6MZbRWgdZGiwC5QWyHZDRvMe2";

    FloatingActionButton addButton;
    FirebaseAuth mFirebaseAuth;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mFirebaseAuth = FirebaseAuth.getInstance();

        final String userId = mFirebaseAuth.getCurrentUser().getUid(); //id-ul userului curent
        if(userId.equals(adminId)) {
            addButton = findViewById(R.id.addButton);
            addButton.setVisibility(View.VISIBLE);
            // click addbuton button
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePage.this, ManageBooks.class));
                }
            });
        }

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));


        //if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_container,
                    new AllBooksFragment()).commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.availableBooks:
                            selectedFragment = new AvailableFragment();
                            break;
                        case R.id.allBooks:
                            selectedFragment = new AllBooksFragment();
                            break;
                        case R.id.reservedBooks:
                            selectedFragment = new ReservedFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.up_menu, menu);
        return true;
    }


}