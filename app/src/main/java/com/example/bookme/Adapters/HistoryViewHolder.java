package com.example.bookme.Adapters;


import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookme.R;

import org.w3c.dom.Text;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    public TextView reservedUserName;
    public TextView startDate;
    public TextView endDate;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.reservedUserName = (TextView) itemView.findViewById(R.id.userReservedName);
        this.startDate = (TextView) itemView.findViewById(R.id.startHistory);
        this.endDate = (TextView) itemView.findViewById(R.id.endHistory);
    }
}