package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser mFirebaseUser;
    public static DrawerLayout drawer;
    public static ActionBarDrawerToggle toggle;
    private TextView username,type,weight,duration,weekly_schedule;
    private ImageView msg_image,image_2;
    private ImageView edit;
    private String email;
    private static final String KEY_TITLE = "Target Type";
    private static final String KEY_DESCRIPTION = "Target Weight";
    private static final String KEY_DURATION = "Target Duration";
    private CardView mCardView1,mCardView2,mCardView3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCardView1 = findViewById(R.id.card_1);
        mCardView2 = findViewById(R.id.card_2);
        mCardView3 = findViewById(R.id.card_3);
//        msg_image = findViewById(R.id.imageView2);
//        image_2 = findViewById(R.id.tile_2);
        edit = findViewById(R.id.edit_2);
        type = findViewById(R.id.goal_type);
        weight = findViewById(R.id.goal_weight);
        duration = findViewById(R.id.goal_Duration);
        weekly_schedule = findViewById(R.id.tv_weekly_schedule);
//
//        setRoundImage();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email = mFirebaseUser.getEmail();

        /*------------------------------------Data Retrieve-------------------------------------------*/

        db.collection("Libra Users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            String ttype = documentSnapshot.getString(KEY_TITLE);
                            String tweight = documentSnapshot.getString(KEY_DESCRIPTION);
                            String tdur = documentSnapshot.getString(KEY_DURATION);
                            username.setText(documentSnapshot.getString("Username"));
                            type.setText("Type :" + ttype);
                            weight.setText("Weight: " + tweight + " Kg");
                            duration.setText("Duration: " + tdur + " Months");
                        }else{
                            Toasty.info(MainActivity.this,"Oops! Can't Retrieve Your Data",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: ");
            }
        });


        /*-----------------------------Activity View--------------------------------------------*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
         toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        /* ------------------------------------------------------------------------------------------*/
        username = findViewById(R.id.mgs_uname);
        mFirebaseDatabase = FirebaseDatabase.getInstance();



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SetGoalActivity.class));
            }
        });

        /*-------------------------------Notification Activity--------------------------------------*/


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,05);
        calendar.set(Calendar.MINUTE,14);
        calendar.set(Calendar.SECOND,00);
        Intent intent =  new Intent(getApplicationContext(),Notification_Reciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);


        /*-------------------------------Weekly Schedule-----------------------------------------*/

        weekly_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*_______________________________________________________________________________*/

                /*-----------------------------------Shinde Tera Weekly Schedule ka ider kar----------*/

                /*________________________________________________________________________________*/

            }
        });


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mFirebaseAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_home) {

            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(this,ScheduleActivity.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this,MapsActivity.class));

        } else if(id==R.id.nav_feedback){
            startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
        } else if( id==R.id.nav_exercise){

            startActivity(new Intent(this,MuscleActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    public void setRoundImage()
//    {
//        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.mix_grad)).getBitmap();
//        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.mix_grad)).getBitmap();
//        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
//        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
//        Canvas canvas = new Canvas(imageRounded);
//        Canvas canvas2 = new Canvas(imageRounded2);
//        Paint mpaint = new Paint();
//        mpaint.setAntiAlias(true);
//        Paint mpaint2 = new Paint();
//        mpaint2.setAntiAlias(true);
//        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 50, 50, mpaint);// Round Image Corner 100 100 100 100
//        msg_image.setImageBitmap(imageRounded);
//        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 50, 50, mpaint2);// Round Image Corner 100 100 100 100
//        image_2.setImageBitmap(imageRounded2);
//    }
}
