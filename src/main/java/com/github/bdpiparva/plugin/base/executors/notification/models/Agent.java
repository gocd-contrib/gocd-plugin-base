package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.google.gson.annotations.SerializedName;

public class Agent {
    @SerializedName("uuid")
    private String uuid;

    @SerializedName("host_name")
    private String hostName;

    @SerializedName("is_elastic")
    private boolean isElastic;

    @SerializedName("ip_address")
    private String ipAddress;

    @SerializedName("operating_system")
    private String operatingSystem;

    @SerializedName("free_space")
    private String freeSpace;

    @SerializedName("agent_config_state")
    private String configState;

    @SerializedName("transition_time")
    private String transitionTime;

    @SerializedName("build_state")
    private String buildState;

    @SerializedName("agent_state")
    private String agentState;

    public String getUuid() {
        return uuid;
    }

    public String getHostName() {
        return hostName;
    }

    public boolean isElastic() {
        return isElastic;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getFreeSpace() {
        return freeSpace;
    }

    public String getConfigState() {
        return configState;
    }

    public String getTransitionTime() {
        return transitionTime;
    }

    public String getBuildState() {
        return buildState;
    }

    public String getAgentState() {
        return agentState;
    }
}
