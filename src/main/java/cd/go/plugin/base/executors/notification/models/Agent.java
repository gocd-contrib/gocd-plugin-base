/*
 * Copyright 2019 Thoughtworks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.plugin.base.executors.notification.models;

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
