package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;
    public TextView content;

    public MyViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.cardTitle);
        date = view.findViewById(R.id.cardDate);
        content = view.findViewById(R.id.cardContent);
    }
}
