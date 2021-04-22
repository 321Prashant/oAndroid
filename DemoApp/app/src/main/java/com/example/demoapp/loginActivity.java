package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginActivity extends AppCompatActivity {

    EditText t1,t2;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t1 = (EditText) findViewById(R.id.email);
        t2 = (EditText) findViewById(R.id.pwd);

        mAuth = FirebaseAuth.getInstance();

    }

    public void logIn(View view) {
        String email = t1.getText().toString().trim();
        // Log.i("abc",email);
        String password = t2.getText().toString().trim();
        // Log.i("abc",password);
        if(TextUtils.isEmpty(password)){
            t2.setError("Fill this up please");
            return;
        }
        if(TextUtils.isEmpty(email)){
            t1.setError("Fill this up please");
            return;
        }
        if(password.length() < 5 && password.length() > 9){
            t2.setError("please put some more alphabets between 5 to 9");
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginActivity.this, "Sahi pkde hai!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AllImages.class));
                }
            else
                {
                    Toast.makeText(loginActivity.this, "Bete ERROR krdi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}