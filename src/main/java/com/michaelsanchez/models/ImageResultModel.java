package com.michaelsanchez.models;

import java.util.ArrayList;

public class ImageResultModel {

    private ArrayList<ImageModel> imageModels = new ArrayList<>();

    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(ArrayList<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public ImageResultModel addImage(ImageModel imageModel){
        imageModels.add(imageModel);
        return this;
    }
}
