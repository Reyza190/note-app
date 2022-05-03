package com.example.laprak_firebase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText dafEmail, dafPassword;
    private Button signUp;
    private TextView back;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressdialog();
        init();

        signUp.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void progressdialog() {
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(false);
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        dafEmail = findViewById(R.id.daftaremail);
        dafPassword = findViewById(R.id.daftarpassword);
        signUp = findViewById(R.id.signup);
        back = findViewById(R.id.back);
    }
    public void onStart() {
        super.onStart();
        //check apakah user telah melakukan login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(dafEmail.getText().toString())){
            dafEmail.setError("Required");
            result = false;
        }else{
            dafEmail.setError(null);
        }
        if (TextUtils.isEmpty(dafPassword.getText().toString())){
            dafPassword.setError("Required");
            result = false;
        }else{
            dafPassword.setError(null);
        }
        return result;
    }

    private void signUp(){
        String email = dafEmail.getText().toString().trim();
        String password = dafPassword.getText().toString().trim();
        progressDialog.show();
        if (!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup:
                signUp();
                break;
        }
    }
}