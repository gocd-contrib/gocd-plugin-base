package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.github.bdpiparva.plugin.base.executors.notification.DefaultDateTypeAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class StageStatusRequest {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(DATE_PATTERN))
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private Pipeline pipeline;

    public static StageStatusRequest fromJSON(String json) {
        return GSON.fromJson(json, StageStatusRequest.class);
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public Pipeline getPipeline() {
        return pipeline;
    }
}
