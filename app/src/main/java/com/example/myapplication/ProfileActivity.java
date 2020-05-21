package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends MainActivity {

    private ImageView profilepic;
    private TextView profile_username,profile_email,profile_height,profile_weight,profile_bmi;
    private Button btn_update_profile,change_pass;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private String useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_next);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        toggle.setHomeAsUpIndicator(R.drawable.menu);

        // setting click listener on hamburger icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }

        });

        // setting listerner on drawer
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        profilepic = findViewById(R.id.Profile_pic);
        profile_email = findViewById(R.id.profile_email);
        profile_username = findViewById(R.id.profile_username);
        profile_height = findViewById(R.id.profile_height);
        profile_weight = findViewById(R.id.profile_weight);
        change_pass = findViewById(R.id.btnChng_pass);
        btn_update_profile = findViewById(R.id.btnprofile_update);
        profile_bmi = findViewById(R.id.profile_bmi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null)
        {
            useremail =  mFirebaseUser.getEmail();
        }


        DatabaseReference databaseReference = mFirebaseDatabase.getReference(mFirebaseAuth.getUid());

        StorageReference storageReference = mFirebaseStorage.getReference();
        storageReference.child(mFirebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilepic);
            }
        });


        mFirestore.collection("Libra Users").document(useremail).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            profile_email.setText(documentSnapshot.getString("Email"));
                            profile_bmi.setText(documentSnapshot.getString("BMI"));
                            profile_height.setText(documentSnapshot.getString("Height"));
                            profile_weight.setText(documentSnapshot.getString("Weight"));
                            profile_username.setText(documentSnapshot.getString("Username"));
                        }else {
                            Toast.makeText(ProfileActivity.this,"Looks Like You're Not a Member ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivity.this,"Something Went Wrong!!",Toast.LENGTH_SHORT).show();

            }
        });



        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfile.class));
            }
        });


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,UpdatePassword.class));
            }
        });

    }
}
//databaseReference.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        ProfileDetails profileDetails = dataSnapshot.getValue(ProfileDetails.class);
//        profile_username.setText("Username: "+ profileDetails.getUsername());
//        profile_email.setText("Email: " +profileDetails.getUserEmail());
//        profile_height.setText("Height: "+profileDetails.getUserHeight());
//        profile_weight.setText("Weight: "+ profileDetails.getUserWeight());
//        profile_bmi.setText("BMI: " +profileDetails.getUserBMI());
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//        Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
//        }
//        });