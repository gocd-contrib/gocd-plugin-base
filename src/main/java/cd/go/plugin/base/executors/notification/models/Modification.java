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
import java.util.Map;

public class Modification {
    @SerializedName("revision")
    private String revision;

    @SerializedName("modified-time")
    private Date modifiedTime;

    @SerializedName("data")
    private Map data;

    public String getRevision() {
        return revision;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public Map getData() {
        return data;
    }
}
