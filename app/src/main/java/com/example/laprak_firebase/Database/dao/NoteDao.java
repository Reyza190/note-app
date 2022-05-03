package com.example.laprak_firebase.Database.dao;


import android.content.Context;
import android.widget.Toast;

import com.example.laprak_firebase.Database.model.Note;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class NoteDao {
    private DatabaseReference databaseReference;

    public NoteDao(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://pamfirebase-f676e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = db.getReference(Note.class.getSimpleName());
    }

    public Task<Void> insert(Note note){
        return databaseReference.push().setValue(note);
    }

    public Task<Void> update(String key, Note note){
        return databaseReference.child(key).setValue(note);
    }
    public Task<Void> delete(String key){
        return databaseReference.child(key).removeValue();
    }

    public Query get(){
        return databaseReference.orderByKey();
    }

    public Task<DataSnapshot> getall(String key){
        return databaseReference.child(key).get();
    }
}
