package com.example.bookme.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookme.R;

public class ReservedBookViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewName;
    public ImageView imageViewBook;
    public TextView textViewYear;
    public TextView textViewCategory;
    public TextView textViewAuthor;
    public CardView cardView;
    public Button returnBook;

    public ReservedBookViewHolder(@NonNull View itemView) {
        super(itemView);
        this.textViewYear = (TextView) itemView.findViewById(R.id.bookYear);
        this.textViewName = (TextView) itemView.findViewById(R.id.bookName);
        this.imageViewBook = (ImageView) itemView.findViewById(R.id.bookImg);
        this.textViewAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
        this.textViewCategory = (TextView) itemView.findViewById(R.id.bookCategory);
        this.cardView = (CardView) itemView.findViewById(R.id.idCardViewBook);
        this.returnBook = (Button) itemView.findViewById(R.id.returnBook);
    }
}