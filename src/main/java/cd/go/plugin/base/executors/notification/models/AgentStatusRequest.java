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

import cd.go.plugin.base.executors.notification.DefaultDateTypeAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class AgentStatusRequest {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(DATE_PATTERN))
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public Agent agent;

    private AgentStatusRequest(Agent agent) {
        this.agent = agent;
    }

    public static AgentStatusRequest fromJSON(String json) {
        return new AgentStatusRequest(GSON.fromJson(json, Agent.class));
    }

    public String toJSON() {
        return GSON.toJson(agent);
    }
}