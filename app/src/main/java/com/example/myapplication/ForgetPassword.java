package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgetPassword extends AppCompatActivity {

    private Button Reset_btn;
    private EditText Email;
    private TextView back_btn;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Email = (EditText)findViewById(R.id.Reset_email);
        Reset_btn = (Button)findViewById(R.id.reset_btn);
        back_btn = findViewById(R.id.tv_back);
        mFirebaseAuth = FirebaseAuth.getInstance();



        Reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = Email.getText().toString().trim();
                mProgressDialog.setMessage("Please Wait");
                mProgressDialog.show();;
                mProgressDialog.setCancelable(false);

                if(useremail.equals(""))
                {
                    Toasty.info(ForgetPassword.this,"Please Enter Your Registered Email Address",Toast.LENGTH_LONG).show();
                }else
                {
                    mFirebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mProgressDialog.dismiss();
                                Toasty.success(ForgetPassword.this,"Password Reset Email Sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPassword.this,LoginActivity.class));
                            }else
                            {
                                mProgressDialog.dismiss();
                                Toasty.error(ForgetPassword.this,"Error While Sending Email Request",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassword.this,LoginActivity.class));
            }
        });


    }
}
