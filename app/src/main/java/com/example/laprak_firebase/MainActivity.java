package com.example.laprak_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laprak_firebase.Database.dao.NoteDao;
import com.example.laprak_firebase.Database.model.Note;
import com.example.laprak_firebase.adapter.NoteAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView logout, salam;
    private FloatingActionButton btn_add;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private NoteDao noteDao = new NoteDao();
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private Note note;
    DatabaseReference database = FirebaseDatabase.getInstance("https://pamfirebase-f676e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        logout.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        String email = mAuth.getCurrentUser().getEmail().toString();
        salam.setText("Hai " + email);

        noteData();
        recycleview();
    }

    private void noteData(){
        noteDao.get().addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    note = dataSnapshot.getValue(Note.class);
                    note.setKey(dataSnapshot.getKey());
                    noteArrayList.add(note);
                }
                adapter.notifyDataSetChanged();
                recycleview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void init(){
        logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recycleview);
        salam = findViewById(R.id.salam);
        btn_add = findViewById(R.id.btn_add);
    }
    private void recycleview(){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new NoteAdapter( noteArrayList, this, notesClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                Intent i = new Intent(this, BuatNote.class);
                startActivity(i);
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onLongClick(Note note, CardView cardView) {
            Note selected = new Note();
            selected = note;
            showPopUp(selected.getKey(),cardView);
        }
    };

    private void showPopUp(String key,CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        Intent i = new Intent(getApplicationContext(), BuatNote.class);
                        i.putExtra("key", key);
                        startActivity(i);
                        return true;
                    case R.id.delete:
                        deleteData(key);
                        return true;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

    private void deleteData(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Laprak_firebase));
        builder.setTitle("Delete");
        builder.setMessage("Anda yakin delete note ini ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.child("Note").child(key).removeValue().addOnSuccessListener(succes ->{
                    Toast.makeText(getApplicationContext(), "Note berhasil dihapus", Toast.LENGTH_SHORT).show();
                    noteArrayList.clear();
                    noteData();
                }).addOnFailureListener(error ->{
                    Toast.makeText(getApplicationContext(), "Note gagal dihapus", Toast.LENGTH_SHORT).show();
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Delete dibatalkan", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });builder.show();
    }


}