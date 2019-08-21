package com.mobilfabrikator.goaltivibrowser.DataPack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeriesInfoModel {
    @SerializedName("num")
    @Expose
    public Integer num;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("series_id")
    @Expose
    public Integer seriesId;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("plot")
    @Expose
    public String plot;
    @SerializedName("cast")
    @Expose
    public String cast;
    @SerializedName("director")
    @Expose
    public String director;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("releaseDate")
    @Expose
    public String releaseDate;
    @SerializedName("last_modified")
    @Expose
    public String lastModified;
   /* @SerializedName("rating")
    @Expose
    public double rating;*/
    @SerializedName("rating_5based")
    @Expose
    public double rating5based;
   /* @SerializedName("backdrop_path")
    @Expose
    public List<String> backdropPath = null;*/
    @SerializedName("youtube_trailer")
    @Expose
    public String youtubeTrailer;
    @SerializedName("episode_run_time")
    @Expose
    public String episodeRunTime;
    @SerializedName("category_id")
    @Expose
    public String categoryId;

  
}
