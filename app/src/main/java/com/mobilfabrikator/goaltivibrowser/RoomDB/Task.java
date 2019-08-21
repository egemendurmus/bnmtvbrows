package com.mobilfabrikator.goaltivibrowser.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "ekli")
    public boolean eklimi;

    @ColumnInfo(name = "tipi")
    public String tipi;

    @ColumnInfo(name = "streamID")
    public int streamID;

    @ColumnInfo(name = "cover")
    public String cover;

    @ColumnInfo(name = "header")
    public String header;

    @ColumnInfo(name = "extension")
    public String extension;
/*
    @ColumnInfo(name = "seriesInfoModel")
    public SeriesInfoModel seriesInfoModel;*/


}