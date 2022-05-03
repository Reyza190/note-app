package com.example.laprak_firebase;

import androidx.cardview.widget.CardView;

import com.example.laprak_firebase.Database.model.Note;

public interface NotesClickListener {
    void onLongClick(Note note, CardView cardView);
}
