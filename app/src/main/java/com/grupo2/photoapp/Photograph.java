package com.grupo2.photoapp;

public class Photograph {
    private int id;
    private byte[] image;
    private String description;

    public Photograph(int id, byte[] image, String description) {
        this.id = id;
        this.image = image;
        this.description = description;
    }

    public int getId() { return id; }
    public byte[] getImage() { return image; }
    public String getDescription() { return description; }
}

