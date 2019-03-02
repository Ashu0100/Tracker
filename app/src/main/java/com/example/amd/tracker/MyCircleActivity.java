package com.example.amd.tracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCircleActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference,userReference;
    String circlememberid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        namelist=new ArrayList<>();

       layoutManager=new LinearLayoutManager(this);
       recyclerview.setLayoutManager(layoutManager);
       recyclerview.setHasFixedSize(true);

       userReference= FirebaseDatabase.getInstance().getReference().child("Users");
       reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               namelist.clear();


               if(dataSnapshot.exists())
               {
                   for(DataSnapshot dss: dataSnapshot.getChildren())
                   {
                       circlememberid = dss.child("circlememberid").getValue().toString();

                       userReference.child(circlememberid)
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       createUser = dataSnapshot.getValue(CreateUser.class);
                                       namelist.add(createUser);
                                       adapter.notifyDataSetChanged();

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {
                                       Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                                   }
                               });
                   }
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();


           }
       });



       adapter=new MembersAdapter(namelist,getApplicationContext());
       recyclerview.setAdapter(adapter);
       adapter.notifyDataSetChanged();



    }
}
