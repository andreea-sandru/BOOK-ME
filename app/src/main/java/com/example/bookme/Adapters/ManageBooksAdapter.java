package com.example.bookme.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.bookme.ManageBooks;
import com.example.bookme.ObjectModels.BookObject;
import com.example.bookme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ManageBooksAdapter extends RecyclerView.Adapter<ManageBooksAdapter.ManageBooksViewHolder> {

    ArrayList<BookObject> bookList;

    public ManageBooksAdapter( ArrayList<BookObject> bookList){
        this.bookList = bookList;
    }
    @NonNull
    @Override
    public ManageBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_manage_books, parent, false);
        return new ManageBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageBooksViewHolder holder, int position) {
        holder.textViewAuthor.setText(bookList.get(position).getBookAuthor());
        holder.textViewCategory.setText(bookList.get(position).getBookCategory());
        holder.textViewYear.setText(bookList.get(position).getBookYear());
        holder.textViewName.setText(bookList.get(position).getBookName());
        final BookObject book = bookList.get(position);
        Glide.with(holder.imageViewBook.getContext()).load(book.getImageUri()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.imageViewBook);
        final ManageBooksViewHolder endHolder = holder;
        holder.deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_id = book.getBookId();
                BookObject updated_book = book;
                DatabaseReference ref_new = FirebaseDatabase.getInstance().getReference("all_books").child(book_id);
                ref_new.removeValue().addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(endHolder.deleteBook.getContext(), book.getBookName() + " was deleted!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );

            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class ManageBooksViewHolder extends BooksViewHolder {
        public Button deleteBook;

        public ManageBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            this.deleteBook = (Button) itemView.findViewById(R.id.deleteBook);
        }
    }
}
