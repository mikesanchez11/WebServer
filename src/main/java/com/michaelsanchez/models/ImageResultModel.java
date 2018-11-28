package com.michaelsanchez.models;

import java.util.ArrayList;

public class ImageResultModel {
    private ArrayList<ImageModel> images;

    public ImageResultModel() {
        images = new ArrayList<ImageModel>();
    }

    public ArrayList<ImageModel> getImages() {

        return images;
    }

    public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public ImageResultModel addImage(ImageModel imageModel) {
        images.add(imageModel);
        return this;
    }
}
