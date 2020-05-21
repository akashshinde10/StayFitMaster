package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class FeedbackActivity extends MainActivity {
    private EditText ouremail,subject,message;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_feedback, contentFrameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_next);
        toolbar.setTitle("Feedback");
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



        ouremail=findViewById(R.id.email);
        subject=findViewById(R.id.subject);
        message=findViewById(R.id.message);
        send=findViewById(R.id.btnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ourSubjectFeedback=subject.getText().toString();
                String ourMessageFeedback=message.getText().toString();
                String ourEmail=ouremail.getText().toString();

                String[] email_divide=ourEmail.split(",");

                Intent send=new Intent(Intent.ACTION_SEND);

                send.putExtra(Intent.EXTRA_EMAIL,email_divide);
                send.putExtra(Intent.EXTRA_SUBJECT,ourSubjectFeedback);
                send.putExtra(Intent.EXTRA_TEXT,ourMessageFeedback);


                send.setType("message/rfc822");
                send.setPackage("com.google.android.gm");
                startActivity(send);



            }
        });



    }
}
