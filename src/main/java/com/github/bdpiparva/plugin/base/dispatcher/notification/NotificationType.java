package com.github.bdpiparva.plugin.base.dispatcher.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public enum NotificationType {
    @Expose
    @SerializedName("stage-status")
    STAGE_STATUS,
    
    @Expose
    @SerializedName("agent-status")
    AGENT_STATUS
}
