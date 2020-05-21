package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView profilepic;
    private TextView profile_username,profile_email,profile_height,profile_weight,profile_bmi;
    private Button btn_update_profile,change_pass;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private String useremail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_profile,container,false);

        profilepic = view.findViewById(R.id.Profile_pic);
        profile_email = view.findViewById(R.id.profile_email);
        profile_username = view.findViewById(R.id.profile_username);
        profile_height = view.findViewById(R.id.profile_height);
        profile_weight = view.findViewById(R.id.profile_weight);
        change_pass = view.findViewById(R.id.btnChng_pass);
        btn_update_profile = view.findViewById(R.id.btnprofile_update);
        profile_bmi = view.findViewById(R.id.profile_bmi);


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
                            Toast.makeText(getActivity(),"Looks Like You're Not a Member ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(),"Something Went Wrong!!",Toast.LENGTH_SHORT).show();

            }
        });



        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.content_frame,new UpdateProfileFragment()).commit();
            }
        });


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),UpdatePassword.class));
            }
        });

        return view;
    }
}
