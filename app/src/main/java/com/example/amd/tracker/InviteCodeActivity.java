package com.example.amd.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,date,isSharing,code;
    Uri imageUri;
    Uri downloadUri;

    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog ProgressDialog;
    String UserId;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = (TextView)findViewById(R.id.textView3);
        auth = FirebaseAuth.getInstance();
        ProgressDialog = new ProgressDialog(this);
        Intent myintent=getIntent();
        reference= FirebaseDatabase.getInstance().getReference().child("USERS");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images");


            if (myintent != null)
            {
                name = myintent.getStringExtra("Name");
                email = myintent.getStringExtra("Email");
                password = myintent.getStringExtra("Password");
                code = myintent.getStringExtra("code");
                isSharing = myintent.getStringExtra("issharing");
                imageUri = myintent.getParcelableExtra("ImageUri");
            }

            t1.setText(code);


    }

    public void registeruser(View v)
    {
        ProgressDialog.setMessage("Please wait whie we add your account");
        ProgressDialog.show();


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            // insert value in realtime database
                            user = auth.getCurrentUser();


                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","user.getUid()");

                            user = auth.getCurrentUser();
                            UserId = auth.getUid();
                            UserId = user.getUid();



                            reference.child(UserId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())

                                            {


                                                //  Save image to Firebase Storage

                                                StorageReference sr = storageReference.child(user.getUid() +".jpg");
                                                sr.putFile(imageUri)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(Task<UploadTask.TaskSnapshot> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                {




                                                                    /////////                               /////////
                                                                    ////////            getdownloadUri()    ///////
                                                                    ////////                                ///////
                                                                    ////////                                /////////










                                                                    String download_image_path = task.getResult().getStorage().getDownloadUrl().toString();
                                                                    reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful())
                                                                                    {
                                                                                        ProgressDialog.dismiss();
                                                                                        Toast.makeText(getApplicationContext(), "Email Sent For Verification Check Your Email", Toast.LENGTH_SHORT).show();
                                                                                        sendVerificationEmail();

                                                                                        Intent myintent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                                                                        startActivity(myintent);
                                                                                    }
                                                                                    else
                                                                                        {
                                                                                            ProgressDialog.dismiss();
                                                                                            Toast.makeText(getApplicationContext(), "An Error Occurred while creating Account", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                }
                                                                            });

                                                                }



                                                            }
                                                        });

                                                }
                                            else
                                            {
                                                ProgressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Sorry For Inconvenience You Can't Register Try Again Later", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                });


    }
    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Email sent for verification", Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Could not send Email", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}
