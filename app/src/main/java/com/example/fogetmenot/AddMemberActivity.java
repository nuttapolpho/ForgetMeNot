package com.example.fogetmenot;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class AddMemberActivity extends AppCompatActivity {

    private ImageView selectedImage;
    private static final int PICK_IMAGE = 100;
    private final String IMAGE_DIRECTORY = "contents://media/internal/images/media";
    private EditText photoName;
    private Database db;
    private Bitmap cImage;
    private String cPersonName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        selectedImage = (ImageView) findViewById(R.id.add_person_selectedImage);
        photoName = (EditText) findViewById(R.id.photoName);
        Button openGallery = (Button) findViewById(R.id.opengallery);
        Button addMemberBtn = (Button) findViewById(R.id.add_member_btn);
        Button cancelBtn = (Button) findViewById(R.id.cancel_add_member_btn);
        db = new Database(this);

        openGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isReadStoragePermissionGranted()){
                    ActivityCompat.requestPermissions(AddMemberActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isWriteStoragePermissionGranted()){
                    ActivityCompat.requestPermissions(AddMemberActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }

                try{
                    cPersonName = photoName.getText().toString();
                    if(cPersonName.isEmpty()){
                        throw new MyCustomException("Name is Expty");
                    }
                    if (db.insertImage(cImage, cPersonName)){

                        new AlertDialog.Builder(AddMemberActivity.this)
                                .setTitle("สำเร็จ!")
                                .setMessage("เพิ่มข้อมูลของ " + cPersonName + " สำเร็จ :)")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("เพิ่มข้อมูลต่อ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        return;
                                    }})
                                .setNegativeButton("กลับหน้าหลัก", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        finish();
                                    }}).show();
                    }
                }catch (MyCustomException mex){
                    new AlertDialog.Builder(AddMemberActivity.this)
                            .setTitle("ผิดพลาด")
                            .setMessage("กรุณาใส่ชื่อ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("ตกลง", null).show();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddMemberActivity.this)
                        .setMessage("คุณต้องการจะยกเลิกการเพิ่มข้อมูลหรือไม่")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("ดำเนินการต่อ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }})
                        .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }}).show();
            }

        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri uri = data.getData();
            try{
                cImage = getBitmap(uri);
                selectedImage.setImageBitmap(cImage);
            }catch (FileNotFoundException fex){
                Toast.makeText(getApplicationContext(), "Error File not found Exception",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Bitmap getBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();

        o.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 72;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;

        int scale = 1;

        while (true)
        {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
            {
                break;
            }
            width_tmp /= 2;

            height_tmp /= 2;

            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o2);

        return bitmap;

    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Read External storage   Permission is granted1");
                return true;
            } else {
                Log.v("Permission","Read External storage Permission is revoked1");
                return false;
            }
        }
        else {
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Write External storage Permission is granted");
                return true;
            } else {

                Log.v("Permission","Write External storage Permission is revoked2");
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Write External storage Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("Permission", "External storage2");
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v("Permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    return;
                }else{
                    finish();
                }
                break;

            case 3:
                Log.d("Permission", "External storage1");
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v("Permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    return;
                }else{
                    finish();
                }
                break;
        }    }
}
