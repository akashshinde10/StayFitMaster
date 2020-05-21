package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class Back extends MainActivity {

    ListView listView;
    String mTitle[] = {"Band Bent-Over Row","Dumbbell Single Arm Row","Chest-Supported Dumbbell Row","Inverted Row","Lat Pulldown","Bent-Over Barbell Rows","Seated Cable Row w/ Pause","Pullup or Chinup Variations"};
    String mDescription[] = {"4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps","4 sets of 15 reps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_exercise, contentFrameLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_next);
        toolbar.setTitle("Back Exercise");
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

        listView = findViewById(R.id.listView);
        // now create an adapter class

        Back.MyAdapter adapter = new Back.MyAdapter(this, mTitle, mDescription);
        listView.setAdapter(adapter);
        // there is my mistake...
        // now again check this..

        // now set item click on list view

        // so item click is done now check list view
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];

        MyAdapter (Context c, String title[], String description[]) {
            super(c, R.layout.index_content, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;

        }

        //   @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View index_content = layoutInflater.inflate(R.layout.index_content, parent, false);
            TextView myTitle = index_content.findViewById(R.id.textView1);
            TextView myDescription = index_content.findViewById(R.id.textView2);

            // now set our resources on views

            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);




            return index_content;
        }
    }
}


