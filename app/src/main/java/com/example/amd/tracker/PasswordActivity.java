package com.example.amd.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {
    String email;
    EditText e3_password;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e3_password = (findViewById(R.id.edittext3));
        Intent myIntent = getIntent();
                if (myIntent!=null);
        {
            email = myIntent.getStringExtra("email");
        }
    }

    public void goToNamePicActivity(View v)
    {
        if(e3_password.getText().toString().length() > 8 )

        {

            Intent myintent = new Intent(PasswordActivity.this, NameActivity.class);
            myintent.putExtra("Email", email);
            myintent.putExtra("Password", e3_password.getText().toString());
            startActivity(myintent);
            finish();
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Password length should be more then 8 characters",Toast.LENGTH_SHORT).show();

        }

    }
}
