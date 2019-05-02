package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public class Modification {
    @SerializedName("revision")
    private String revision;

    @SerializedName("modified-time")
    private Date modifiedTime;

    @SerializedName("data")
    private Map data;

    public String getRevision() {
        return revision;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public Map getData() {
        return data;
    }
}
