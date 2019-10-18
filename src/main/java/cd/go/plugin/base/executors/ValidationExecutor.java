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

package cd.go.plugin.base.executors;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cd.go.plugin.base.GsonTransformer.fromJson;
import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.success;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

public class ValidationExecutor implements Executor {
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, String>>() {
    }.getType();

    private static Logger LOGGER = Logger.getLoggerFor(ValidationExecutor.class);
    private final List<Validator> validators = new ArrayList<>();
    private final boolean forPluginSettings;

    public ValidationExecutor(Validator... validators) {
        this(false, validators);
    }

    /***
     * @param forPluginSettings set to true used for plugin settings object. Defaults to false.
     * @param validators        additional validators to apply on give request body
     */
    public ValidationExecutor(boolean forPluginSettings, Validator... validators) {
        this.forPluginSettings = forPluginSettings;
        this.validators.addAll(asList(validators));
    }

    public void addAll(Validator... validators) {
        this.validators.addAll(asList(validators));
    }

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        final ValidationResult validationResult = new ValidationResult();
        if (validators.isEmpty()) {
            LOGGER.debug(format("No validator(s) are provided. Skipping the validation for request %s", request.requestName()));
            return success(GsonTransformer.toJson(validationResult));
        }

        validators.forEach(validator -> {
            if (validator != null) {
                validationResult.merge(validator.validate(asMap(request.requestBody())));
            }
        });

        if (validationResult.isEmpty()) {
            LOGGER.debug("Validation successful.");
            return success(GsonTransformer.toJson(validationResult));
        }

        LOGGER.debug(format("Validation failed %s.", validationResult));
        return DefaultGoPluginApiResponse.success(GsonTransformer.toJson(validationResult));
    }

    Map<String, String> asMap(String requestBody) {
        if (this.forPluginSettings) {
            final JsonObject object = fromJson(requestBody, JsonObject.class);
            final JsonElement pluginSettings = object.get("plugin-settings");
            if (!pluginSettings.isJsonObject()) {
                throw new IllegalArgumentException("Please make sure that given request body is valid json object!");
            }

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
}
