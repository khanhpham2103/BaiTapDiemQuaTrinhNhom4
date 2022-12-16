package com.example.multinotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Note> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        this.mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateHolder: Making New");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        view.setOnClickListener(mainAct);
        view.setOnLongClickListener(mainAct);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate());
        String content = note.getContent();
        if (content.length() > 80) {
            content = content.substring(0, 80) + "...";
        }
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
