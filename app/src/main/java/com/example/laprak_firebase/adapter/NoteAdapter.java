package com.example.laprak_firebase.adapter;

import android.app.Activity;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laprak_firebase.Database.dao.NoteDao;
import com.example.laprak_firebase.Database.model.Note;
import com.example.laprak_firebase.NotesClickListener;
import com.example.laprak_firebase.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private ArrayList<Note> notes = new ArrayList<>();
    private NoteDao noteDao;
    private Activity context;
    private NotesClickListener listener;
    public NoteAdapter(ArrayList<Note> notes, Activity context, NotesClickListener listener) {
        this.notes = notes;
        this.context = context;
        this.listener = listener;

    }

    public void setListNotes(ArrayList<Note> notes){
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(getColor(), null));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(notes.get(holder.getAdapterPosition()), holder.cardView);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView title, content;
        private Activity context;
        private NoteDao noteDao;
        private ImageView edit, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notetitle);
            content = itemView.findViewById(R.id.notecontent);
            cardView = itemView.findViewById(R.id.card);

        }
    }

    private int getColor(){
        List <Integer> get_color = new ArrayList<>();
        Random random = new Random();

        get_color.add(R.color.merah);
        get_color.add(R.color.kuning);
        get_color.add(R.color.hijau);
        get_color.add(R.color.putih);
        get_color.add(R.color.biru);
        int random_color = random.nextInt(get_color.size());
        return get_color.get(random_color);
    }

}
