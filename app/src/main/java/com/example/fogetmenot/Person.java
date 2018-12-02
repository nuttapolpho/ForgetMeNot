package com.example.fogetmenot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Person {
    private String name;
    private byte[] image;

    public Person(String name, byte[] image){
        this.name = name;
        this.image = image;
    }

    public Person(byte[] image){
        this.image = image;
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(this.image, 0, this.image.length);
        return bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

