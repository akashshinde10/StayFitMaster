package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    String id;
    public static final String DATABASE_NAME = "gymwork5.db";

    public static final String TABLE_NAME = "Gym_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "EX1";
    public static final String COL_3 = "EX2";
    public static final String COL_4 = "EX3";
    public static final String COL_5 = "EX4";
    public static final String COL_6 = "EX5";
    public static final String COL_7 = "EX6";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , EX1 TEXT,EX2 TEXT,EX3 TEXT,EX4 TEXT,EX5 TEXT,EX6 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME  );
        onCreate(db);
    }

    public Boolean insertData(String mon, String tue,String wed ,String thu, String fri ,String sat){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2 , mon);
        contentValues.put(COL_3 , tue);
        contentValues.put(COL_4 , wed);
        contentValues.put(COL_5 , thu);
        contentValues.put(COL_6 , fri);
        contentValues.put(COL_7 , sat);

        long result = db.insert(TABLE_NAME ,null,contentValues);

        if(result == -1)
            return false;
        else
            return true;


    }
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from "+ TABLE_NAME,null);
        return res;
    }

    public  Boolean updateData(String uid,String mon, String tue,String wed ,String thu, String fri ,String sat){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1 , uid);
        contentValues.put(COL_2 , mon);
        contentValues.put(COL_3 , tue);
        contentValues.put(COL_4 , wed);
        contentValues.put(COL_5 , thu);
        contentValues.put(COL_6 , fri);
        contentValues.put(COL_7 , sat);

        db.update(TABLE_NAME,contentValues,"ID = ?",new String[] {uid});
        return true;

    }
}
