package com.example.bookme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.bookme.Fragments.AvailableFragment;
import com.example.bookme.Fragments.ReservedFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

        // la intoarcerea dintr-un BookPage, stergem cartea care a fost rezervata din "available_books"
        if(getIntent().hasExtra("book_delete_id")) {
            String delete_book_id = getIntent().getExtras().get("book_delete_id").toString();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child("available_books").child(delete_book_id).setValue(null);
        }

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new PageAdapter( getSupportFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AvailableFragment();
                case 1:
                    return new ReservedFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    String recent_news = "Available";
                    return recent_news;
                case 1:
                    String category = "Reserved";
                    return category;
            }
            return null;
        }
    }
}