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

package cd.go.plugin.base.executors.authorization;

import cd.go.plugin.base.dispatcher.authorization.SupportedAuthType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Capabilities {
    @Expose
    @SerializedName("supported_auth_type")
    private SupportedAuthType supportedAuthType;

    @Expose
    @SerializedName("can_search")
    private boolean canSearch;

    @Expose
    @SerializedName("can_authorize")
    private boolean canAuthorize;

    @Expose
    @SerializedName("can_get_user_roles")
    private boolean canGetUserRoles;

    Capabilities(SupportedAuthType supportedAuthType, boolean canSearch, boolean canAuthorize, boolean canGetUserRoles) {
        this.supportedAuthType = supportedAuthType;
        this.canSearch = canSearch;
        this.canAuthorize = canAuthorize;
        this.canGetUserRoles = canGetUserRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capabilities that = (Capabilities) o;
        return canSearch == that.canSearch &&
                canAuthorize == that.canAuthorize &&
                canGetUserRoles == that.canGetUserRoles &&
                supportedAuthType == that.supportedAuthType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(supportedAuthType, canSearch, canAuthorize, canGetUserRoles);
    }
}
