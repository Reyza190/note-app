package com.example.laprak_firebase;


import static android.content.ContentValues.TAG;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.signin.internal.SignInClientImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private TextView Register;
    private Button signIn;
    private ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialisasi
        init();
        signIn.setOnClickListener(this);
        Register.setOnClickListener(this);
        google.setOnClickListener(this);
        progressDialog();



    }

    @Override
    public void onStart() {
        super.onStart();
        //check apakah user telah melakukan login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Required");
            result = false;
        }else{
            etEmail.setError(null);
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Required");
            result = false;
        }else{
            etPassword.setError(null);
        }
        return result;
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        Register = findViewById(R.id.register);
        signIn = findViewById(R.id.login);
        google = findViewById(R.id.google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1095121333689-eicdv8d5mv5an7b1di8m36j44qcumer6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn(String email, String password){
        progressDialog.show();
        if (!validateForm()){
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null)
                        {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI(firebaseUser);
                        }else {
                            Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                signIn(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                break;
            case R.id.register:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
            case R.id.google:
                loginWithGoogle();
                break;
        }
    }

    private void loginWithGoogle() {
        Intent i = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i, 1001);
        progressDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.dismiss();
        if (requestCode == 1001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(false);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                })
                .addOnFailureListener(this, e -> Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show());
                updateUI(null);
    }
}