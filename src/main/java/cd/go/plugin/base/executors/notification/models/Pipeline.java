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

import java.util.List;

public class Pipeline {
    @SerializedName("name")
    private String name;

    @SerializedName("counter")
    private String counter;

    @SerializedName("group")
    private String group;

    @SerializedName("build-cause")
    private List<BuildCause> buildCause;

    @SerializedName("stage")
    private Stage stage;

    @SerializedName("label")
    private String label;

    public String getName() {
        return name;
    }

    public String getCounter() {
        return counter;
    }

    public String getGroup() {
        return group;
    }

    public List<BuildCause> getBuildCause() {
        return buildCause;
    }

    public Stage getStage() {
        return stage;
    }

    public String getLabel() {
        return label;
    }
}
