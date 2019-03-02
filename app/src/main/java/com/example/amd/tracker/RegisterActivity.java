package com.example.amd.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {
    EditText e_Email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e_Email = (EditText)findViewById(R.id.Emailid_Reg);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

    }
    public void gotoPasswordActivity (View v)
    {
        dialog.setMessage("Checking Email Address");
        dialog.show();
        // check if email is already registered or not
        auth.fetchProvidersForEmail(e_Email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check = !task.getResult().getProviders().isEmpty();

                            if (!check)
                            {

                                //email does not exist we can create new user
                                Intent myintent = new Intent(RegisterActivity.this,PasswordActivity.class);
                                myintent.putExtra("Email",e_Email.getText().toString());
                                startActivity(myintent);
                                finish();
                                }

                            else
                                {
                                dialog.dismiss();

                                Toast.makeText(getApplicationContext(), "This email is already Registered", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });
    }
}
