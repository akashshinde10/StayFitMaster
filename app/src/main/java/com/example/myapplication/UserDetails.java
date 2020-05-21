package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class UserDetails extends AppCompatActivity {

    final Context context = this;
    String form_fname,form_lname,form_age,form_gender,
            form_height,form_weight,form_bmi,genderText,form_email;
    private TextView user_email,bmi_result,user_name;
    private EditText fname,lname, height,weight, age;
    private ImageView profile_image;
    private Spinner gender;
    private Button save_deatil;

    private FirebaseAuth mFirebaseAuth ;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseUser mFirebaseUser;
    private StorageReference storageReference;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private static int PICK_IMAGE = 123;
    private static final String KEY_FNAME = "First Name";
    private static final String KEY_LNAME = "Last Name";
    private static final String KEY_AGE = "Age";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_HEIGHT = "Height";
    private static final String KEY_WEIGHT = "Weight";
    private static final String KEY_BMI ="BMI";

    Uri imagepath;

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
        setContentView(R.layout.activity_user_details);
        setupUIViews();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        storageReference = mFirebaseStorage.getReference();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            form_email = mFirebaseUser.getEmail();
        }

        mFirestore.collection("Libra Users").document(form_email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            user_name.setText(documentSnapshot.getString("Username"));
                            user_email.setText(form_email);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(UserDetails.this,"Document Doesn't Exist",LENGTH_SHORT).show();
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

        bmi_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                form_height = height.getText().toString();
                form_weight = weight.getText().toString();

                if (height.getText().toString().length() < 1)
                {
                    Toasty.info(UserDetails.this,"You must enter your height!", LENGTH_SHORT).show();
                    // Abort the onClick if null
                    return;
                }
                if (weight.getText().toString().length() < 1)
                {
                    Toasty.info(UserDetails.this,"You must enter your weight", LENGTH_SHORT).show();
                    // Abort the onClick if null
                    return;
                }
                // Convert height from string to float
                float h = Float.valueOf(height.getText().toString());
                float w = Float.valueOf(weight.getText().toString());
                float BMI = w / (h * h) * 10000;

                bmi_result.setText(String.valueOf(BMI));
            }
        });

        save_deatil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {

                    sendUserData();
                    mFirebaseAuth.signOut();
                    startActivity(new Intent(UserDetails.this, LoginActivity.class));

                }

            }
        });


    }


    private void sendUserData(){


        genderText = gender.getSelectedItem().toString();
        form_fname = fname.getText().toString().trim();
        form_lname = lname.getText().toString().trim();
        form_gender = gender.toString();
        form_age = age.getText().toString();
        form_height = height.getText().toString();
        form_weight = weight.getText().toString();
        form_bmi = bmi_result.getText().toString();

        Map<String ,Object> field = new HashMap<>();
        field.put(KEY_FNAME,form_fname);
        field.put(KEY_LNAME,form_lname);
        field.put(KEY_AGE,form_age);
        field.put(KEY_GENDER,genderText);
        field.put(KEY_HEIGHT,form_height);
        field.put(KEY_WEIGHT,form_weight);
        field.put(KEY_BMI,form_bmi);

        /*------------------------------Store Data On Cloud-----------------------------*/
        mFirestore.collection("Libra Users").document(form_email).update(field)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(UserDetails.this, "You're A Member of Libra Fitness", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(UserDetails.this,"Oops!! Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });


        /*--------------------------------Store Profile Image On Cloud-------------------------------------------------------*/
        //Toast.makeText(UserDetails.this,"Profile photo Uploded",Toast.LENGTH_LONG).show();
        StorageReference imageref = storageReference.child(mFirebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask =imageref.putFile(imagepath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toasty.error(UserDetails.this,"That wasn't supposed to happen!!",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toasty.success(UserDetails.this,"Email Verification Send",Toast.LENGTH_LONG).show();

            }
        });

        /*-----------------------------------------------------------------------------------------------------*/
//        ProfileDetails profileDetails = new ProfileDetails(form_email,form_fname,
//                form_lname,form_age,genderText,
//                form_height,form_weight,form_bmi,form_username);
//        reference.setValue(profileDetails);
    }


    private void setupUIViews(){
        fname = (EditText)findViewById(R.id.etUserfname);
        lname = (EditText)findViewById(R.id.etUserlname);
        age = (EditText)findViewById(R.id.etUserAge);
        gender = (Spinner) findViewById(R.id.SpUserGender);
        save_deatil = (Button) findViewById(R.id.btnSaveDetails);
        height = (EditText) findViewById(R.id.etUserHeight);
        weight = (EditText) findViewById(R.id.etUserWeight);
        bmi_result = (TextView) findViewById(R.id.tvUserBMI);
        user_email = findViewById(R.id.tvUseremail);
        user_name = findViewById(R.id.tvUsername);
        profile_image = findViewById(R.id.UserImage);
    }


    private Boolean validate(){
        Boolean result = false;

        form_fname = fname.getText().toString();
        form_lname = lname.getText().toString();
        form_age = age.getText().toString();
        form_bmi = bmi_result.getText().toString();

        if(form_fname.isEmpty() || form_lname.isEmpty() || form_age.isEmpty()){
            Toasty.info(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else if (form_bmi.isEmpty()){
            Toasty.info(UserDetails.this,"Click On Get My BMI", LENGTH_SHORT).show();
        }else if (imagepath == null)
        {
            Toasty.info(UserDetails.this,"Upload A Profile Image",LENGTH_SHORT).show();
        }
        else{
            result = true;
        }

        return result;
    }
}

