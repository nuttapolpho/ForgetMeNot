package com.example.fogetmenot;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database";
    private static final String TABLE_NAME = "person";
    private static final String COLUMN_1 = "id";
    private static final String COLUMN_2 = "name";
    private static final String COLUMN_3 = "img";
    private static Context cContext;

    private static final String HSCORE_TABLE_NAME = "hscore";
    private static final String COL_1_HSCORE_TABLE_NAME = "id";
    private static final String COL_2_HSCORE_TABLE_NAME = "score";

    private static final String CREATE_TABLE_PERSON = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_2 + " TEXT NOT NULL, " + COLUMN_3 + " BLOB NOT NULL)";
    private static final String CREATE_TABLE_HSCORE = "CREATE TABLE " + HSCORE_TABLE_NAME + " (" + COL_1_HSCORE_TABLE_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2_HSCORE_TABLE_NAME + " INTEGER NOT NULL)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cContext = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PERSON);
        db.execSQL(CREATE_TABLE_HSCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HSCORE_TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertImage(Bitmap image, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] img;
        try{
            img = getBytes(image);
            if(img == null){
                return false;
            }
            ContentValues values = new ContentValues();
            values.put(COLUMN_2, name);
            values.put(COLUMN_3, img);
            db.insert(TABLE_NAME, null, values);
            return true;
        }catch (NullPointerException nux){
            new AlertDialog.Builder(cContext)
                    .setTitle("ผิดพลาด")
                    .setMessage("กรุณาเลือกรูปภาพ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ตกลง", null).show();
            return false;
        }

    }

    public ArrayList<Person> getPersonList() throws DatabaseException {
        ArrayList<Person> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if(cursor.getCount() == 0){
            throw new DatabaseException("Nothing Found!");
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            Person p  = new Person(name, image, id);
            list.add(p);
            cursor.moveToNext();
        }
        Collections.shuffle(list);
        return list;
    }

    public boolean deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "1", null) > 0;
    }

    public boolean deteleMember(int id){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_1 + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_1 + " = " + id, null) > 0;

    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if(cursor.getCount() == 0){
            return true;
        }
        return false;
    }

    private byte[] getBytes(Bitmap bitmap) throws NullPointerException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        } catch (NullPointerException nux){
            new AlertDialog.Builder(cContext)
                    .setTitle("ผิดพลาด")
                    .setMessage("กรุณาเลือกรูปภาพ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ตกลง", null).show();
            return null;
        }
        return stream.toByteArray();
    }

    public void insertHScore(int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2_HSCORE_TABLE_NAME, score);
        long a = db.insert(HSCORE_TABLE_NAME, null, values);
        Log.i("SOM" , a + "");
    }

    public int getHScore(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_2_HSCORE_TABLE_NAME + " FROM " + HSCORE_TABLE_NAME +
                " WHERE 1 ORDER BY " + COL_2_HSCORE_TABLE_NAME + " DESC", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}

