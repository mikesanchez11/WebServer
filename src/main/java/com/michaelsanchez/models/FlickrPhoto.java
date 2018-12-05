package com.michaelsanchez.models;

import java.util.ArrayList;

public class FlickrPhoto {

    private int page;
    private int pages;
    private int perpage;
    private long total;
    private ArrayList<FlickrPhotoItem> photo;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ArrayList<FlickrPhotoItem> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<FlickrPhotoItem> photo) {
        this.photo = photo;
    }
}
