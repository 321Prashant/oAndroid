package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class MainActivity extends AppCompatActivity {


    EditText description,name,number,password;
    Uri filepath;
    ImageView img;
    Button browse,signup;
    Bitmap bitmap;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView)findViewById(R.id.img);
        signup = (Button)findViewById(R.id.SignUp);
        browse = (Button)findViewById(R.id.browser);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {

                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"select any Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtofirebase();
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
       if(resultCode == 1 && resultCode == RESULT_OK){
           filepath = data.getData();
           try {

               InputStream inputStream = getContentResolver()
                       .openInputStream(filepath);
               bitmap = BitmapFactory.decodeStream(inputStream);
               img.setImageBitmap(bitmap);
           }catch (Exception e)
           {


           }
       }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void uploadtofirebase() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();
        name = (EditText)findViewById(R.id.t1);
        description = (EditText)findViewById(R.id.t2);
        //number = (EditText)findViewById(R.id.t3);
        //password = (EditText)findViewById(R.id.t4);

        final StorageReference uploader = storage.getReference("Image1"+new Random().nextInt(50));
        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog.dismiss();
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference root= db.getReference("users");
                         dataholder obj = new dataholder(description.getText().toString()
                         ,name.getText().toString());
                         root.child(description.getText().toString()).setValue(obj);

                         name.setText("");
                         description.setText("");
                         //number.setText("");
                         //password.setText("");
                         img.setImageResource(R.drawable.ic_launcher_background);
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                       // float percent =(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        //dialog.setMessage("Uploaded"+(int)percent+"%");
                    }
                });
    }
}