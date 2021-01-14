package com.example.bookme.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.Adapters.BooksViewHolder;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AvailableFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<BookObject> options;
    private FirebaseRecyclerAdapter< BookObject, BooksViewHolder> adapter;

    public AvailableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_available, container, false);
        final View view2 = view;

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String userId = mFirebaseAuth.getCurrentUser().getUid();

        recyclerView = (RecyclerView) view.findViewById(R.id.availableRecyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("available_books");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        options = new FirebaseRecyclerOptions.Builder <BookObject> ().setQuery(databaseReference, BookObject.class).build();
        adapter = new FirebaseRecyclerAdapter < BookObject, BooksViewHolder > (options) {

            @Override
            protected void onBindViewHolder(BooksViewHolder holder, final int position, @NonNull BookObject model) {
                final BookObject book = model;
                Glide.with(view2.getContext()).load(model.getImageUri()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.imageViewBook);
                holder.textViewCategory.setText(book.getBookCategory());
                holder.textViewAuthor.setText(book.getBookAuthor());
                holder.textViewYear.setText(book.getBookYear());
                holder.textViewName.setText(book.getBookName());

                // la apasare pe carte => redirectare pagina carte
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String book_id = getRef(position).getKey();
                        // TO DO: de creat un activity BookPage caruia ii trimitem id-ul cartii
                        // si avand id-ul putem lua din db info carte
                        //Intent book_page = new Intent(getActivity(), BookPage.class);
                        //book_page.putExtra("book_id", book_id);
                        //startActivity(book_page);
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
}