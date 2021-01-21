package com.example.bookme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bookme.Fragments.AllBooksFragment;
import com.example.bookme.Fragments.AvailableFragment;
import com.example.bookme.Fragments.ReservedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        addButton = findViewById(R.id.addButton);

        // click addbuton button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ManageBooks.class));
            }
        });

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
}