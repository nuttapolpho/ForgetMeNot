package com.example.fogetmenot;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MemberListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Person> personList;
    private PersonListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        Database db = new Database(this);

        listView = (ListView) findViewById(R.id.member_listView);
        personList = new ArrayList<>();
        try {
            personList = db.getPersonList();
        } catch (DatabaseException e) {


            Drawable d = (Drawable) ContextCompat.getDrawable(this, R.drawable.user_default);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();

            Person p = new Person("ไม่มีข้อมูล", bitmapdata);
            personList.add(p);
        }
        adapter = new PersonListAdapter(this, R.layout.member_item, personList);
        listView.setAdapter(adapter);

    }
}
