/*
 * Copyright 2019 ThoughtWorks, Inc.
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

package cd.go.plugin.base.executors.scm.request;

import cd.go.plugin.base.executors.scm.model.Revision;
import cd.go.plugin.base.executors.scm.model.ScmData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutRequest<T> {
    @Expose
    @SerializedName("revision")
    private Revision revision;

    @Expose
    @SerializedName("scm-data")
    private ScmData scmData;

    @Expose
    @SerializedName("destination-folder")
    private String destinationFolder;
    private T scmConfiguration;

    public Revision getRevision() {
        return revision;
    }

    public ScmData getScmData() {
        return scmData;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public T getScmConfiguration() {
        return scmConfiguration;
    }

    public void setScmConfiguration(T scmConfiguration) {
        this.scmConfiguration = scmConfiguration;
    }
}
