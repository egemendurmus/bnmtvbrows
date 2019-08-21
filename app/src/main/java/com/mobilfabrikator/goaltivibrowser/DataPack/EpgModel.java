package com.mobilfabrikator.goaltivibrowser.DataPack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EpgModel {
    @SerializedName("user_info")
    @Expose
    public UserInfo userInfo;
    @SerializedName("server_info")
    @Expose
    public ServerInfo serverInfo;

    public class UserInfo {
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("auth")
        @Expose
        public Integer auth;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("exp_date")
        @Expose
        public String expDate;
        @SerializedName("is_trial")
        @Expose
        public String isTrial;
        @SerializedName("active_cons")
        @Expose
        public String activeCons;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("max_connections")
        @Expose
        public String maxConnections;
        @SerializedName("allowed_output_formats")
        @Expose
        public List<String> allowedOutputFormats;
    }

    public class ServerInfo {
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("port")
        @Expose
        public String port;
        @SerializedName("https_port")
        @Expose
        public String httpsPort;
        @SerializedName("server_protocol")
        @Expose
        public String serverProtocol;
        @SerializedName("rtmp_port")
        @Expose
        public String rtmpPort;
        @SerializedName("timezone")
        @Expose
        public String timezone;
        @SerializedName("timestamp_now")
        @Expose
        public Integer timestampNow;
        @SerializedName("time_now")
        @Expose
        public String timeNow;
    }
}
