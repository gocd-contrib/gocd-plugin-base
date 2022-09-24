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

import java.util.Date;

public class Job {
    @SerializedName("name")
    private String name;

    @SerializedName("schedule-time")
    private Date scheduleTime;

    @SerializedName("complete-time")
    private Date completeTime;

    @SerializedName("assign-time")
    private Date assignTime;

    @SerializedName("state")
    private String state;

    @SerializedName("result")
    private String result;

    @SerializedName("agent-uuid")
    private String agentUuid;

    public String getName() {
        return name;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public String getState() {
        return state;
    }

    public String getResult() {
        return result;
    }

    public String getAgentUuid() {
        return agentUuid;
    }

    public Date getAssignTime() {
        return assignTime;
    }
}
