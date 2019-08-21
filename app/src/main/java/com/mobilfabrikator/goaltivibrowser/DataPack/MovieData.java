package com.mobilfabrikator.goaltivibrowser.DataPack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieData {

    @SerializedName("stream_id")
    @Expose
    public Integer streamId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("added")
    @Expose
    public String added;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("container_extension")
    @Expose
    public String containerExtension;
    @SerializedName("custom_sid")
    @Expose
    public String customSid;
    @SerializedName("direct_source")
    @Expose
    public String directSource;
}
