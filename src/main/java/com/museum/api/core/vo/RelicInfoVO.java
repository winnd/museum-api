package com.museum.api.core.vo;

import com.museum.api.common.orm.model.Relic;
import com.museum.api.core.controller.RelicController;

import java.util.ArrayList;

public class RelicInfoVO extends Relic {


    private String imagesString;

    private ArrayList<ImageInfo> images;

    private String relicTypeStr;

    public ArrayList<ImageInfo> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageInfo> images) {
        this.images = images;
    }

    public String getImagesString() {
        return imagesString;
    }

    public void setImagesString(String imagesString) {
        this.imagesString = imagesString;
    }

    public String getRelicTypeStr() {
        return relicTypeStr;
    }

    public void setRelicTypeStr(String relicTypeStr) {
        this.relicTypeStr = relicTypeStr;
    }
}
