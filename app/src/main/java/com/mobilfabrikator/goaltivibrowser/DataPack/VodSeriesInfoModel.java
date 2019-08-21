package com.mobilfabrikator.goaltivibrowser.DataPack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VodSeriesInfoModel {

    @SerializedName("info")
    @Expose
    public Info info;

    public class Info {

        @SerializedName("imdb_id")
        @Expose
        public String imdbId;
        @SerializedName("movie_image")
        @Expose
        public String movieImage;
        @SerializedName("genre")
        @Expose
        public String genre;
        @SerializedName("plot")
        @Expose
        public String plot;
        @SerializedName("cast")
        @Expose
        public String cast;
        @SerializedName("rating")
        @Expose
        public String rating;
        @SerializedName("director")
        @Expose
        public String director;
        @SerializedName("releasedate")
        @Expose
        public String releasedate;
        @SerializedName("duration_secs")
        @Expose
        public Integer durationSecs;
        @SerializedName("duration")
        @Expose
        public String duration;
        @Expose
        public Integer bitrate;
        @SerializedName("tmdb_id")
        @Expose
        public String tmdbId;
        @SerializedName("backdrop_path")
        @Expose
        public List<Object> backdropPath = null;
        @SerializedName("youtube_trailer")
        @Expose
        public String youtubeTrailer;
    }
}

