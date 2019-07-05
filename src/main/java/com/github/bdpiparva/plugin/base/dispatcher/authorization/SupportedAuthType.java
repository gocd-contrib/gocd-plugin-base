package com.github.bdpiparva.plugin.base.dispatcher.authorization;

import com.google.gson.annotations.SerializedName;

public enum SupportedAuthType {
    @SerializedName("password")
    Password,
    @SerializedName("web")
    Web
}
