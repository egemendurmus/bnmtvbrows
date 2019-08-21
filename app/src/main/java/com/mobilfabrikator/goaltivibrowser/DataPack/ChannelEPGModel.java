package com.mobilfabrikator.goaltivibrowser.DataPack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelEPGModel {
    @SerializedName("epg_listings")
    @Expose
    public List<EpgListing> epgListings;

    public class EpgListing {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("epg_id")
        @Expose
        public String epgId;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("lang")
        @Expose
        public String lang;
        @SerializedName("start")
        @Expose
        public String start;
        @SerializedName("end")
        @Expose
        public String end;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("channel_id")
        @Expose
        public String channelId;
        @SerializedName("start_timestamp")
        @Expose
        public String startTimestamp;
        @SerializedName("stop_timestamp")
        @Expose
        public String stopTimestamp;
    }
}
