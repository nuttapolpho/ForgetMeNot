package com.example.fogetmenot;

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

    public byte[] getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

