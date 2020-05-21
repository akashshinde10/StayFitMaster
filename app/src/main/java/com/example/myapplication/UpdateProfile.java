package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.view.Gravity.*;

public class UpdateProfile extends MainActivity {

    private EditText update_uname,update_age,update_height,update_weight;
    private TextView update_bmi,back_btn;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode ==RESULT_OK && data.getData() != null){

            imagepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_update_profile, contentFrameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_next);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        toggle.setHomeAsUpIndicator(R.drawable.menu);

        // setting click listener on hamburger icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(START);
            }

        });

        // setting listerner on drawer
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        update_uname = findViewById(R.id.etupdate_username);
        update_age = findViewById(R.id.etupdate_age);
        update_height = findViewById(R.id.etupadte_height);
        update_weight = findViewById(R.id.etupdate_weight);
        update_bmi = findViewById(R.id.etupdate_bmi);
        save_detail = findViewById(R.id.button);
        profile_image = findViewById(R.id.update_pic);
        back_btn = findViewById(R.id.tvback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null)
        {
            email = mFirebaseUser.getEmail();
        }

        mFirestore.collection("Libra Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        update_uname.setText(documentSnapshot.getString("Username"));
                        update_age.setText(documentSnapshot.getString("Age"));
                        update_height.setText(documentSnapshot.getString("Height"));
                        update_weight.setText(documentSnapshot.getString("Weight"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(UpdateProfile.this,"Error while fetching data!",Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toasty.info(UpdateProfile.this,"Please Enter Height",Toast.LENGTH_SHORT).show();
                    // Abort the onClick if null
                    return;
                }
                if (update_weight.getText().toString().length() < 1)
                {
                    Toasty.info(UpdateProfile.this,"You must enter your weight",Toast.LENGTH_SHORT).show();
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
                                Toasty.success(UpdateProfile.this,"Profile Photo Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(UpdateProfile.this,"Error!! Profile Not Updated",Toast.LENGTH_SHORT).show();

                    }
                });
//
                if(imagepath != null){
                    StorageReference imageref = storageReference.child(mFirebaseAuth.getUid()).child("Images").child("Profile Pic");
                    UploadTask uploadTask =imageref.putFile(imagepath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(UpdateProfile.this,"Ooops! Something's Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toasty.success(UpdateProfile.this,"Profile Photo Uploaded",Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toasty.success(UpdateProfile.this,"Everything Looks good",Toast.LENGTH_SHORT).show();
                }



            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.success(UpdateProfile.this,"Profile Updated",Toast.LENGTH_SHORT).show();
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

    }


}
