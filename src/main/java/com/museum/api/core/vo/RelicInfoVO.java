package com.museum.api.core.vo;

import com.museum.api.common.orm.model.Relic;

import java.util.ArrayList;

public class RelicInfoVO extends Relic {


    private String imagesString;

    private ArrayList<String> images;

    private ArrayList<Integer> imageIds;

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getImagesString() {
        return imagesString;
    }

    public void setImagesString(String imagesString) {
        this.imagesString = imagesString;
    }

    public ArrayList<Integer> getImageIds() {
        return imageIds;
    }

    public void setImageIds(ArrayList<Integer> imageIds) {
        this.imageIds = imageIds;
    }
}
