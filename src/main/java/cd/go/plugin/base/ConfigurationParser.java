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

package cd.go.plugin.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static cd.go.plugin.base.GsonTransformer.fromJson;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

public class ConfigurationParser {
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    public static Map<String, String> asMap(String requestBody) {
        return asMap(requestBody, false);
    }

    public static Map<String, String> asMap(String requestBody, boolean oldJsonStructure) {
        if (oldJsonStructure) {
            final JsonObject object = fromJson(requestBody, JsonObject.class);
            final JsonElement pluginSettings = getPluginSettingsOrScmConfiguration(object);

            final HashMap<String, String> asMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : pluginSettings.getAsJsonObject().entrySet()) {
                asMap.put(entry.getKey(), entry.getValue().getAsJsonObject().get("value").getAsString());
            }
            return asMap;
        } else {
            final Map<String, String> asMap = fromJson(requestBody, CONFIG_TYPE);
            return ofNullable(asMap).orElse(emptyMap());
        }
    }

    private static JsonElement getPluginSettingsOrScmConfiguration(JsonObject object) {
        JsonElement jsonElement = object.get("plugin-settings");
        if (jsonElement != null && jsonElement.isJsonObject()) {
            return jsonElement;
        }

        jsonElement = object.get("scm-configuration");
        if (jsonElement != null && jsonElement.isJsonObject()) {
            return jsonElement;
        }

        throw new IllegalArgumentException("Json does not contain \"plugin-settings\" or \"scm-configuration\". Please make sure that given request body is valid json object!");
    }
}
