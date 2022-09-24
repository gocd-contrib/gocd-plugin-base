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
