package com.example.amd.tracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1,e2;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = (EditText)findViewById(R.id.email);
        e2 = (EditText)findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();

    }
    public void login(View v)
    {
        auth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                           // Toast.makeText(getApplicationContext(),"WELCOME", Toast.LENGTH_LONG).show();

                            FirebaseUser user = auth.getCurrentUser();
                            if(user.isEmailVerified())
                            {
                                finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Email Is Not Verified Yet",Toast.LENGTH_SHORT).show();
                            }



                            Intent myintent = new Intent(LoginActivity.this,UserLocationMainActivity.class);
                            finish();
                            startActivity(myintent);


                            }
                            else {
                            Toast.makeText(getApplicationContext(),"WRONG EMAIL OR PASSWORD",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
