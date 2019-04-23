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

package com.github.bdpiparva.plugin.base.dispatcher.secrets;

import com.github.bdpiparva.plugin.base.dispatcher.VersionedExtensionBuilder;
import com.github.bdpiparva.plugin.base.executors.IconRequestExecutor;
import com.github.bdpiparva.plugin.base.executors.MetadataExecutor;
import com.github.bdpiparva.plugin.base.executors.ValidationExecutor;
import com.github.bdpiparva.plugin.base.executors.ViewRequestExecutor;
import com.github.bdpiparva.plugin.base.executors.secrets.LookupExecutor;
import com.github.bdpiparva.plugin.base.validation.DefaultValidator;
import com.github.bdpiparva.plugin.base.validation.Validator;

public class SecretsBuilderV1 extends VersionedExtensionBuilder {
    public static final String REQUEST_GET_ICON = "go.cd.secrets.get-icon";
    public static final String REQUEST_GET_CONFIG_METADATA = "go.cd.secrets.secrets-config.get-metadata";
    protected static final String REQUEST_GET_CONFIG_VIEW = "go.cd.secrets.secrets-config.get-view";
    protected static final String REQUEST_VALIDATE_CONFIG = "go.cd.secrets.secrets-config.validate";
    protected static final String REQUEST_SECRETS_LOOKUP = "go.cd.secrets.secrets-lookup";

    protected SecretsBuilderV1() {
        registry.put(REQUEST_VALIDATE_CONFIG, new ValidationExecutor());
    }

    public SecretsBuilderV1 icon(String iconPath, String contentType) {
        registry.put(REQUEST_GET_ICON, new IconRequestExecutor(iconPath, contentType));
        return this;
    }

    public SecretsBuilderV1 configMetadata(Class<?> configClass) {
        return configMetadata(configClass, true);
    }

    public SecretsBuilderV1 configMetadata(Class<?> configClass, boolean addDefaultValidators) {
        registry.put(REQUEST_GET_CONFIG_METADATA, new MetadataExecutor(configClass));
        if (addDefaultValidators) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CONFIG)).addAll(new DefaultValidator(configClass));
        }
        return this;
    }

    public SecretsBuilderV1 configView(String configViewTemplatePath) {
        registry.put(REQUEST_GET_CONFIG_VIEW, new ViewRequestExecutor(configViewTemplatePath));
        return this;
    }


    public SecretsBuilderV1 validateSecretConfig(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CONFIG)).addAll(validators);
        return this;
    }

    public SecretsBuilderV1 lookup(LookupExecutor lookupExecutor) {
        registry.put(REQUEST_SECRETS_LOOKUP, lookupExecutor);
        return this;
    }
}
