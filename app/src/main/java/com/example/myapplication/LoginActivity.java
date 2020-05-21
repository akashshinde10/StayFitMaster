package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView userRegister;
    private FirebaseAuth mFirebaseAuth;
    private TextView forget_pass;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.etEmail);
        Password = findViewById(R.id.eTPassword);
        Login= findViewById(R.id.btnLogin);
        userRegister = (TextView)findViewById(R.id.tvsignup);
        mFirebaseAuth = FirebaseAuth.getInstance();
        forget_pass = findViewById(R.id.tvForget);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mProgressDialog = new ProgressDialog(this);
        if(user != null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tname = Name.getText().toString().trim();
                String tpass = Password.getText().toString().trim();
                if(tname.isEmpty() || tpass.isEmpty())
                {
                    Toasty.warning(LoginActivity.this, "Please Enter EmailID & Password", Toast.LENGTH_SHORT).show();
                }else{
                    mProgressDialog.setMessage("Doing Push-Ups");
                    mProgressDialog.show();
                    validate(Name.getText().toString().trim(), Password.getText().toString());
                }

            }
        });

        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });


    }


    private void validate(String uName,String uPassword){


        mFirebaseAuth.signInWithEmailAndPassword(uName, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    EmailVerification();
                }else
                {
                    mProgressDialog.dismiss();
                    Toasty.info(LoginActivity.this, "Email Or Password Is Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void EmailVerification()
    {

        FirebaseUser firebaseUser = mFirebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();


        if(emailflag){
            finish();
            mProgressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else
        {
            mProgressDialog.dismiss();
            Toasty.warning(this,"Email Not Verified",Toast.LENGTH_LONG).show();
            mFirebaseAuth.signOut();
        }



    }

}
