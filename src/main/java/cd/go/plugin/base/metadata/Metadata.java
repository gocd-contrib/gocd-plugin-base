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

package cd.go.plugin.base.metadata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Metadata {
    @Expose
    @SerializedName("display_name")
    private final String displayName;
    @Expose
    @SerializedName("required")
    private final boolean required;
    @Expose
    @SerializedName("secure")
    private final boolean secure;

    Metadata(String displayName, boolean required, boolean secure) {
        this.displayName = displayName;
        this.required = required;
        this.secure = secure;
    }
}
