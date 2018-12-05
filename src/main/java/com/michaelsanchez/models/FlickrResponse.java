package com.michaelsanchez.models;

public class FlickrResponse {
    private FlickrPhoto photos;
    private String stat;

    public FlickrPhoto getPhotos() {
        return photos;
    }

    public void setPhotos(FlickrPhoto photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
