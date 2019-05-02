package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Stage {
    @SerializedName("name")
    private String name;

    @SerializedName("counter")
    private String counter;

    @SerializedName("approval-type")
    private String approvalType;

    @SerializedName("approved-by")
    private String approvedBy;

    @SerializedName("state")
    private String state;

    @SerializedName("result")
    private String result;

    @SerializedName("create-time")
    private Date createTime;

    @SerializedName("last-transition-time")
    private Date lastTransitionTime;

    @SerializedName("jobs")
    private List<Job> jobs;

    @SerializedName("previous-stage-counter")
    private Integer previousStageCounter;

    @SerializedName("previous-stage-name")
    private String previousStageName;

    public String getName() {
        return name;
    }

    public String getCounter() {
        return counter;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public String getState() {
        return state;
    }

    public String getResult() {
        return result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastTransitionTime() {
        return lastTransitionTime;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public Integer getPreviousStageCounter() {
        return previousStageCounter;
    }

    public String getPreviousStageName() {
        return previousStageName;
    }
}
