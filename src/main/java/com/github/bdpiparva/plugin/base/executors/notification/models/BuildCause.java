package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BuildCause {
    @SerializedName("material")
    private Map material;

    @SerializedName("changed")
    private Boolean changed;

    @SerializedName("modifications")
    private List<Modification> modifications;

    public Map getMaterial() {
        return material;
    }

    public Boolean getChanged() {
        return changed;
    }

    public List<Modification> getModifications() {
        return modifications;
    }
}
