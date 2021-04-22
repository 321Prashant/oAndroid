package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Authentication extends AppCompatActivity {

    TextInputLayout t1,t2,t3,t4;
    ProgressBar bar;

    String userId;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        t1 =  (TextInputLayout)findViewById(R.id.Name);
        t2 = (TextInputLayout)findViewById(R.id.Number);
        t3 = (TextInputLayout)findViewById(R.id.email);
        t4 = (TextInputLayout)findViewById(R.id.pwd);
        bar= (ProgressBar)findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();
//        if(mAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), loginActivity.class));
//            finish();
//        }
    }


    public void signUpHere(View view) {

    //Log.i("Prashant","36");
        bar.setVisibility(View.VISIBLE);

        String name = t1.getEditText().getText().toString();
        String number = t2.getEditText().getText().toString();
        String email = t3.getEditText().getText().toString();
       // Log.i("abc",email);
        String password = t4.getEditText().getText().toString();
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


        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Authentication.this,new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);
                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String, Object> user =  new HashMap<>();
                            user.put("Email",email);
                            user.put("Name",name);
                            user.put("Number" , number);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","user Id is"+userId);
                                }
                            });
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(getApplicationContext(),
                                    "ohh!! Bete",
                                    Toast.LENGTH_LONG)
                                    .show();

//                            // hide the progress bar
//                            progressBar.setVisibility(View.GONE);
//
//                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(Authentication.this,
                                    AllImages.class);
                            startActivity(intent);

                        }

                        else {
                           // bar.setVisibility(View.VISIBLE);

                            bar.setVisibility(View.GONE);

                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(getApplicationContext(),
                                    "Reh GAYA",
                                    Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                });
    }
}