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

package cd.go.plugin.base.executors;

import cd.go.plugin.base.ResourceReader;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static cd.go.plugin.base.GsonTransformer.toJson;

public class ViewRequestExecutor implements Executor {
    private final JsonObject responseJson = new JsonObject();

    public ViewRequestExecutor(String resourcePath) {
        add("template", ResourceReader.readResource(resourcePath));
    }

    public ViewRequestExecutor add(String key, String value) {
        responseJson.addProperty(key, value);
        return this;
    }

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        return DefaultGoPluginApiResponse.success(toJson(responseJson));
    }
}
