package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    // EditText userday,userexercise;
    Button userbtn, weekbtn, updatebutton;
    // private String mon_ex,tue_ex,wed_ex,thu_ex,fri_ex,sat_ex;
    private EditText monday, tuesday, wednesday, thursday, friday, saturday, id;
    private String mon, tue, wed, thu, fri, sat;
    private TextView usertxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDb = new DatabaseHelper(this);

        monday = (EditText) findViewById(R.id.EtMon);
        tuesday = (EditText) findViewById(R.id.ettue);
        userbtn = (Button) findViewById(R.id.upbtn);
       // id = (EditText) findViewById(R.id.editText7);
        wednesday = (EditText) findViewById(R.id.etwed);
        thursday = (EditText) findViewById(R.id.etthur);
        friday = (EditText) findViewById(R.id.etfri);
        saturday = (EditText) findViewById(R.id.etsat);
        usertxt =  (TextView) findViewById(R.id.textView7);
        weekbtn = (Button) findViewById(R.id.weekbtn);
        updatebutton = (Button) findViewById(R.id.updatebtn);

        AddData();
        viewAll();
        UpdateData();
    }

    public void UpdateData() {

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdated = myDb.updateData(usertxt.getText().toString(),
                        monday.getText().toString(),
                        tuesday.getText().toString(),
                        wednesday.getText().toString(),
                        thursday.getText().toString(),
                        friday.getText().toString(),
                        saturday.getText().toString());

                if (isUpdated = true)
                    Toast.makeText(ScheduleActivity.this, " Data Updated ", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ScheduleActivity.this, " Data Not Updated ", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void AddData() {

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isInserted = (boolean) myDb.insertData(monday.getText().toString(),
                        tuesday.getText().toString(),
                        wednesday.getText().toString(),
                        thursday.getText().toString(),
                        friday.getText().toString(),
                        saturday.getText().toString());


                //   boolean isInserted = (boolean) myDb.insertData(mon,tue,wed,thu,fri,sat);


                if (isInserted = true)
                    Toast.makeText(ScheduleActivity.this, " Data Inserted ", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ScheduleActivity.this, " Data Not Inserted ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void viewAll() {

        weekbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDb.getAllData();
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append(" ID    \t\t\t\t\t\t\t\t\t\t\t  " + "\t\t\t" + res.getString(0) + "\n\n");
                    buffer.append(" MONDAY\t\t\t\t\t\t\t\t\t\t\t " + "\t\t\t" + res.getString(1) + "\n\n");
                    buffer.append(" TUESDAY\t\t\t\t\t\t\t\t\t\t" + "\t\t\t" + res.getString(2) + "\n\n");
                    buffer.append(" WEDNESDAY\t\t\t\t\t\t\t\t\t\t  " + "\t\t\t" + res.getString(3) + "\n\n");
                    buffer.append(" THURSDAY\t\t\t\t\t\t\t\t\t\t " + "\t\t\t" + res.getString(4) + "\n\n");
                    buffer.append(" FRIDAY\t\t\t\t\t\t\t\t\t\t\t " + "\t\t\t" + res.getString(5) + "\n\n");
                    buffer.append(" SATURDAY\t\t\t\t\t\t\t\t\t\t" + "\t\t\t" + res.getString(6) + "\n\n");


                }

                showMessage("\t\t\t\t\tWEEKLY SCHEDULE", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
