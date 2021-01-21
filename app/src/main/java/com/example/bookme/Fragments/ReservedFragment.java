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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.Adapters.ReservedBookViewHolder;
import com.example.bookme.BookPage;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReservedFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<BookObject> options;
    private FirebaseRecyclerAdapter< BookObject, ReservedBookViewHolder> adapter;

    public ReservedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reserved, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.reservedRecyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("all_books");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        String userId = mFirebaseAuth.getCurrentUser().getUid(); //id-ul userului curent
        // afisam doar cartile rezervate de userul curent
        Query firebaseSearchQuery = databaseReference.orderByChild("reservedUserId").equalTo(userId);
        options = new FirebaseRecyclerOptions.Builder <BookObject> ().setQuery(firebaseSearchQuery, BookObject.class).build();
        adapter = new FirebaseRecyclerAdapter < BookObject, ReservedBookViewHolder> (options) {

            @Override
            protected void onBindViewHolder(ReservedBookViewHolder holder, final int position, @NonNull BookObject model) {
                final BookObject book = model;
                Glide.with(view.getContext()).load(model.getImageUri()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.imageViewBook);
                holder.textViewCategory.setText(book.getBookCategory());
                holder.textViewAuthor.setText(book.getBookAuthor());
                holder.textViewYear.setText(book.getBookYear());
                holder.textViewName.setText(book.getBookName());

                // la apasare pe carte => redirectare pagina carte
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String book_id = getRef(position).getKey();
                        // trimitem la pagina cartii, id-ul cartii pe care am dat click
                        // pentru a putea extrage info despre carte din baza de date
                        Intent book_page = new Intent(getActivity(), BookPage.class);
                        book_page.putExtra("book_id", book_id);
                        startActivity(book_page);
                    }
                });

                // la apasare pe carte => redirectare pagina carte
                holder.returnBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String book_id = getRef(position).getKey();
                        BookObject updated_book = book;
                        updated_book.setAvailable(true);
                        updated_book.setReservedUserId("");
                        DatabaseReference ref_new = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                        ref_new.setValue(updated_book).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // afisare mesaj succes: datele cartii s-au updatat
                                            Toast.makeText(getContext(), book.getBookName() + "is available again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );

                        /// to do:

                        // trebuie accesata cartea
                        // book.getNotifyMeUserList();

                        // list.contains(currentUserId) => trimiti o notificare
                        //



                    }
                });
            }
            @NonNull
            @Override
            public ReservedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reserved_books, parent, false);
                return new ReservedBookViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
        return view;
    }
}