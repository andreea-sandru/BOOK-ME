package com.example.bookme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.Adapters.BooksViewHolder;
import com.example.bookme.BookPage;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllBooksFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<BookObject> options;
    private FirebaseRecyclerAdapter< BookObject, BooksViewHolder> adapter;
    Button actionCat, fantasyCat, biographyCat, romanceCat, allCat; // allCat = all categories, to reset filters
    private String filterCategory = "";

    public AllBooksFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_all_books, container, false);

        // initializare componente design din XML-ul fragment_all_books
        actionCat = (Button) view.findViewById(R.id.actionButton1);
        fantasyCat = (Button) view.findViewById(R.id.fantasyButton1);
        biographyCat = (Button) view.findViewById(R.id.biographyButton1);
        romanceCat = (Button) view.findViewById(R.id.romanceButton1);
        allCat = (Button) view.findViewById(R.id.allButton1);

        recyclerView = (RecyclerView) view.findViewById(R.id.allBooksRecyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("all_books");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        // query-ul initial afiseaza tot ce incepe cu "", adica tot
        Query firebaseSearchQuery = databaseReference.orderByChild("bookCategory").startAt(filterCategory);

        // daca apasam pe un buton de filtrare => actualizam query-ul si adapterul de recyclerview
        actionCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCategory = "action";
                options = updateOptions(filterCategory);
                adapter.updateOptions(options);
            }
        });

        romanceCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCategory = "romance";
                options = updateOptions(filterCategory);
                adapter.updateOptions(options);
            }
        });

        fantasyCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCategory = "fantasy";
                options = updateOptions(filterCategory);
                adapter.updateOptions(options);
            }
        });

        biographyCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCategory = "biography";
                options = updateOptions(filterCategory);
                adapter.updateOptions(options);
            }
        });


        allCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCategory = "";
                options = updateOptions(filterCategory);
                adapter.updateOptions(options);
            }
        });

        options = new FirebaseRecyclerOptions.Builder <BookObject> ().setQuery(databaseReference, BookObject.class).build();
        adapter = new FirebaseRecyclerAdapter < BookObject, BooksViewHolder > (options) {

            @Override
            protected void onBindViewHolder(BooksViewHolder holder, final int position, @NonNull BookObject model) {
                final BookObject book = model;
                Glide.with(view.getContext()).load(model.getImageUri()).override(200, 200).into(holder.imageViewBook);
                holder.textViewCategory.setText(book.getBookCategory());
                holder.textViewAuthor.setText(book.getBookAuthor());
                holder.textViewYear.setText(book.getBookYear());
                holder.textViewName.setText(book.getBookName());

                // click pe carte => redirectare pagina carte
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String book_id = getRef(position).getKey();
                        // trimitem la pagina cartii id-ul acesteia
                        Intent book_page = new Intent(getActivity(), BookPage.class);
                        book_page.putExtra("book_id", book_id);
                        startActivity(book_page);
                    }
                });
            }
            @NonNull
            @Override
            public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_books, parent, false);
                return new BooksViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
        return view;
    }

    // actualizare optiuni recyclerview pt a folosi noul filtru pentru categorii
    FirebaseRecyclerOptions<BookObject> updateOptions(String filterCategory) {
        FirebaseRecyclerOptions<BookObject> options;
        Query searchQuery = databaseReference.orderByChild("bookCategory").equalTo(filterCategory);
        if(filterCategory == "") {
            searchQuery = databaseReference;
        }

        options = new FirebaseRecyclerOptions.Builder <BookObject> ().setQuery(searchQuery, BookObject.class).build();
        return options;
    }
}