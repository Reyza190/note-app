package com.example.laprak_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laprak_firebase.Database.dao.NoteDao;
import com.example.laprak_firebase.Database.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

public class BuatNote extends AppCompatActivity {
    private EditText ettitle, etcontent;
    private FloatingActionButton savebtn, updatebtn;
    private final NoteDao noteDao = new NoteDao();
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_note);
        init();
        Intent i =getIntent();
        String key = i.getStringExtra("key");
        if (key != null){
            savebtn.setVisibility(View.GONE);
            updatebtn.setVisibility(View.VISIBLE);
            getData(key);
            updatebtn.setOnClickListener(view -> {
                updateData(key);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            });
        }else{
            savebtn.setVisibility(View.VISIBLE);
            updatebtn.setVisibility(View.GONE);
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertData();
                }
            });
        }
    }

    private void insertData() {
        String title = ettitle.getText().toString();
        String content = etcontent.getText().toString();
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "isi semua note", Toast.LENGTH_SHORT).show();
        }
        note = new Note(title, content);
        noteDao.insert(note).addOnSuccessListener(success -> {
            Toast.makeText(getApplicationContext(), "Note berhasil tersimpan", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Note gagal tersimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(){
        etcontent = findViewById(R.id.etcontent);
        ettitle = findViewById(R.id.ettitle);
        savebtn = findViewById(R.id.savenote);
        updatebtn = findViewById(R.id.updatenote);
    }

    private void getData(String key){
        noteDao.getall(key).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    note = task.getResult().getValue(Note.class);
                    ettitle.setText(note.getTitle());
                    etcontent.setText(note.getContent());
                }
            }
        });
    }

    private void updateData(String key){
        String title = ettitle.getText().toString();
        String content = etcontent.getText().toString();
        if (title.isEmpty() || content.isEmpty()){
            Toast.makeText(getApplicationContext(), "isi semua note", Toast.LENGTH_SHORT).show();
        }
        note.setTitle(title);
        note.setContent(content);
        noteDao.update(key, note).addOnSuccessListener(success -> {
            Toast.makeText(getApplicationContext(), "Note berhasil diperbaharui", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(error -> {
            Toast.makeText(getApplicationContext(), "Note gagal diperbaharui", Toast.LENGTH_SHORT).show();
        });
    }
}