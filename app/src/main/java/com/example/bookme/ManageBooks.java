package com.example.bookme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bookme.Adapters.ManageBooksAdapter;
import com.example.bookme.ObjectModels.BookObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageBooks extends AppCompatActivity {

    FloatingActionButton addBookButton;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private ArrayList<BookObject> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("all_books");
        recyclerView = findViewById(R.id.manageBooksRecyclerview);
        searchView = findViewById(R.id.searchView);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBooks.this, AddBook.class));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        bookList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            bookList.add(ds.getValue(BookObject.class));
                        }
                    }

                    ManageBooksAdapter manageBooksAdapter = new ManageBooksAdapter(bookList);
                    recyclerView.setAdapter(manageBooksAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ManageBooks.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String newText) {
        ArrayList<BookObject> newBookList = new ArrayList<>();
        for (BookObject book : bookList) {
            if(book.getBookName().toLowerCase().contains(newText.toLowerCase())) {
                newBookList.add(book);
            }
        }

        ManageBooksAdapter manageBooksAdapter = new ManageBooksAdapter(newBookList);
        recyclerView.setAdapter(manageBooksAdapter);
    }
}