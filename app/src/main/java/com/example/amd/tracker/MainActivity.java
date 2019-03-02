package com.example.amd.tracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    PermissionManager manager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user==null)
        {
            setContentView(R.layout.activity_main);
            manager =new PermissionManager() {};
            manager.checkAndRequestPermissions(this);

        }
        else
        {
            Intent myIntent = new Intent(MainActivity.this,UserLocationMainActivity.class);
            startActivity(myIntent);
            finish();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);
        ArrayList<String> denied_Permissions = manager.getStatus().get(0).denied;

        if(denied_Permissions.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Permissions Enabled",Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLogin(View v)
    {
        Intent myIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(myIntent);
    }
    public void goToRegister(View v)
    {
        Intent myIntent=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(myIntent);
    }
}
