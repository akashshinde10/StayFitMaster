package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.MainActivity.drawer;


public class SetGoalActivity extends MainActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "Target Type";
    private static final String KEY_DESCRIPTION = "Target Weight";
    private static final String KEY_DURATION = "Target Duration";
    private TextView back_btn;
    private Spinner editTextTitle;
    private EditText editTextDescription;
    private EditText etduration;
    private Button mButton;
    private FirebaseAuth mFirebaseAuth;
    String email;
    String target_type;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_set_goal, contentFrameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_next);
        toolbar.setTitle("Set A Goal");
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

        editTextTitle = findViewById(R.id.set_type);
        editTextDescription = findViewById(R.id.set_weight);
        etduration = findViewById(R.id.set_Duration);
        back_btn = findViewById(R.id.tv_goal_back);

        mButton = findViewById(R.id.btn_set);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser mFireUser = mFirebaseAuth.getCurrentUser();
            email =mFireUser.getEmail();


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
                startActivity(new Intent(SetGoalActivity.this,MainActivity.class));
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetGoalActivity.this,MainActivity.class));
            }
        });
    }

    public void saveNote(View v) {
        target_type = editTextTitle.getSelectedItem().toString();
        String description = editTextDescription.getText().toString();
        String duration = etduration.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, target_type);
        note.put(KEY_DESCRIPTION, description);
        note.put(KEY_DURATION,duration);



        db.collection("Libra Users").document(email).update(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SetGoalActivity.this, "Target Set!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetGoalActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}
