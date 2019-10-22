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

package cd.go.plugin.base.executors.scm;

import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.annotations.scm.PartOfIdentity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DummyScmConfig {
    @Expose
    @PartOfIdentity
    @SerializedName("SCM_URL")
    @Property(name = "SCM_URL", displayName = "SCM URL", required = true)
    public String url;

    @Expose
    @SerializedName("USERNAME")
    @Property(name = "USERNAME", displayName = "Username")
    public String username;

    @Expose
    @SerializedName("PASSWORD")
    @Property(name = "PASSWORD", displayName = "Password", secure = true)
    public String password;

    private String excludeMe = "Not considering field without @Expose annotation";
}
