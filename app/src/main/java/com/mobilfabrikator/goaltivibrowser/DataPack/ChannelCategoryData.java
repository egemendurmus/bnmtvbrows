package com.mobilfabrikator.goaltivibrowser.DataPack;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class ChannelCategoryData {
    private String  categoryName,categoryID,imageurl;

    public ChannelCategoryData(String categoryName, String categoryID) {
        super();
        this.categoryName = categoryName;
        this.categoryID = categoryID;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
