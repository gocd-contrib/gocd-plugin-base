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
