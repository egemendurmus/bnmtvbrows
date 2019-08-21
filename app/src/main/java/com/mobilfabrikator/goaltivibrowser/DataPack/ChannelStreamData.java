package com.mobilfabrikator.goaltivibrowser.DataPack;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class ChannelStreamData {
    private String channelName, channelID,imageURL;

    public ChannelStreamData(String categoryName, String categoryID,String imageURL) {
        super();
        this.channelName = categoryName;
        this.channelID = categoryID;
        this.imageURL=imageURL;
    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}


