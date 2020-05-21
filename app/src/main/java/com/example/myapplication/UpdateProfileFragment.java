package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class UpdateProfileFragment extends Fragment {

    private EditText update_uname,update_age,update_height,update_weight;
    private TextView update_bmi;
    private Button save_detail;
    private String fname,lname,email,gender;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser mFirebaseUser;

    private ImageView profile_image;
    private static int PICK_IMAGE = 123;
    Uri imagepath;
    private StorageReference storageReference;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode ==RESULT_OK && data.getData() != null){
            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagepath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_profile,container,false);

        update_uname = view.findViewById(R.id.etupdate_username);
        update_age = view.findViewById(R.id.etupdate_age);
        update_height = view.findViewById(R.id.etupadte_height);
        update_weight = view.findViewById(R.id.etupdate_weight);
        update_bmi = view.findViewById(R.id.etupdate_bmi);
        save_detail = view.findViewById(R.id.button);
        profile_image = view.findViewById(R.id.update_pic);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null)
        {
            email = mFirebaseUser.getEmail();
        }

        final StorageReference storageReference = mFirebaseStorage.getReference();
        storageReference.child(mFirebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(profile_image);
                    }
                });

        save_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tuname = update_uname.getText().toString();
                String tage = update_age.getText().toString();
                String theight = update_height.getText().toString();
                String tweight = update_weight.getText().toString();

                if (update_height.getText().toString().length() < 1)
                {
                    Toasty.info(getActivity(),"Please Enter Height", Toast.LENGTH_SHORT).show();
                    // Abort the onClick if null
                    return;
                }
                if (update_weight.getText().toString().length() < 1)
                {
                    Toasty.info(getActivity(),"You must enter your weight",Toast.LENGTH_SHORT).show();
                    // Abort the onClick if null
                    return;
                }
                // Convert height from string to float
                float h = Float.valueOf(update_height.getText().toString());
                float w = Float.valueOf(update_weight.getText().toString());
                float BMI = w / (h * h) * 10000;

                update_bmi.setText(String.valueOf(BMI));
                String tbmi = update_bmi.getText().toString();

                /*----------------------Update User Details------------------------------------*/

                Map<String,Object> field = new HashMap<>();
                field.put("Username",tuname);
                field.put("Height",theight);
                field.put("Weight",tweight);
                field.put("BMI",tbmi);
                field.put("Age",tage);

                mFirestore.collection("Libra Users").document(email).update(field)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getActivity(),"Error!! Profile Not Updated",Toast.LENGTH_SHORT).show();

                    }
                });


                StorageReference imageref = storageReference.child(mFirebaseAuth.getUid()).child("Images").child("Profile Pic");
                UploadTask uploadTask =imageref.putFile(imagepath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getActivity(),"Ooops! Something's Wrong",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toasty.success(getActivity(),"Profile Photo Uploaded",Toast.LENGTH_SHORT).show();

                    }
                });

                startActivity(new Intent(getActivity(),MainActivity.class));


            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setImage = new Intent();
                setImage.setType("image/*");
                setImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(setImage,"Select Profile Photo"),PICK_IMAGE);
            }
        });

        return view;
    }
}
