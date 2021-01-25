package com.example.bookme.Fragments;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.Adapters.ReservedBookViewHolder;
import com.example.bookme.BookPage;
import com.example.bookme.HomePage;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.ObjectModels.HistoryObject;
import com.example.bookme.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static androidx.core.content.ContextCompat.getSystemService;

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

        final String userId = mFirebaseAuth.getCurrentUser().getUid(); //id-ul userului curent
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
                        final String book_id = getRef(position).getKey();
                        final BookObject updated_book = book;
                        updated_book.setAvailable(true);
                        updated_book.setReservedUserId("");
                        DatabaseReference ref_new = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                        ref_new.setValue(updated_book).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // afisare mesaj succes: datele cartii s-au updatat
                                            Toast.makeText(getContext(), book.getBookName() + " is available again!", Toast.LENGTH_LONG).show();

                                            DatabaseReference ref3  = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                                            HashMap usersList = book.getNotifyUserIds();

                                            if(usersList.containsKey(userId)) {
                                                Intent resultIntent = new Intent(getContext(), BookPage.class);
                                                //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                  //      | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                resultIntent.putExtra("book_id", book_id);

                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
                                                stackBuilder.addNextIntent(resultIntent);
                                                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext().getApplicationContext(), "0")
                                                        .setSmallIcon(R.drawable.ic_open_book)
                                                        .setContentTitle("Hello!")
                                                        .setContentText(book.getBookName() + " is available again!");
                                                builder.setContentIntent(resultPendingIntent);

                                                //NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                //manager.notify(0, builder.build());
                                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                                                notificationManager.notify(0, builder.build());
                                            }

                                            usersList.put(userId, null);
                                            ref3.child("notifyUserIds").setValue(usersList).addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                DatabaseReference ref4  = FirebaseDatabase.getInstance().getReference("history").child(book_id);
                                                                final String key = ref4.push().getKey();
                                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                Date date = new Date();
                                                                String nowDate = dateFormat.format(date);
                                                                HistoryObject historyObject = new HistoryObject(book_id, updated_book.getReservedUsername(), updated_book.getReservedDate(), nowDate);
                                                                ref4.child(key).setValue(historyObject);
                                                            }
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                }
                        );
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