package com.example.amd.tracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Joincircle extends AppCompatActivity {
    DatabaseReference reference,currentreference;
    FirebaseUser user;
    FirebaseAuth auth;
    String currentuserid,join_userid;
    DatabaseReference circleref;


    Pinview pinview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joincircle);
        pinview=findViewById(R.id.PinView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentreference=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        currentuserid = user.getUid();

    }

    public void submitButtononClick(View V)
    {
        // To check if input code is present in database
        //if present create a node member

        Query query = reference.orderByChild("CircleCode").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    CreateUser createUser = null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren())
                    {
                        createUser=childDss.getValue(CreateUser.class);
                        join_userid = createUser.userid;



                        circleref=FirebaseDatabase.getInstance().getReference().child("users")
                                .child(join_userid).child("Circle members");


                        circlejoin circlejoin = new circlejoin(currentuserid);
                        circlejoin circlejoin1 = new circlejoin((join_userid));


                        circleref.child(user.getUid()).setValue(circlejoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful());
                                        Toast.makeText(getApplicationContext(),"User Join Circle Successfull",Toast.LENGTH_SHORT).show();

                                    }
                                });









                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid Circle Code", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
