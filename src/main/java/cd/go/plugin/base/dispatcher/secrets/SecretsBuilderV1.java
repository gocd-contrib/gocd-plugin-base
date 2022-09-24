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

package cd.go.plugin.base.dispatcher.secrets;

import cd.go.plugin.base.dispatcher.VersionedExtensionBuilder;
import cd.go.plugin.base.executors.IconRequestExecutor;
import cd.go.plugin.base.executors.MetadataExecutor;
import cd.go.plugin.base.executors.ValidationExecutor;
import cd.go.plugin.base.executors.ViewRequestExecutor;
import cd.go.plugin.base.executors.secrets.LookupExecutor;
import cd.go.plugin.base.validation.DefaultValidator;
import cd.go.plugin.base.validation.Validator;

public final class SecretsBuilderV1 extends VersionedExtensionBuilder<SecretsBuilderV1> {
    public static final String REQUEST_GET_ICON = "go.cd.secrets.get-icon";
    public static final String REQUEST_GET_CONFIG_METADATA = "go.cd.secrets.secrets-config.get-metadata";
    protected static final String REQUEST_GET_CONFIG_VIEW = "go.cd.secrets.secrets-config.get-view";
    protected static final String REQUEST_VALIDATE_CONFIG = "go.cd.secrets.secrets-config.validate";
    protected static final String REQUEST_SECRETS_LOOKUP = "go.cd.secrets.secrets-lookup";

    protected SecretsBuilderV1() {
        register(REQUEST_VALIDATE_CONFIG, new ValidationExecutor());
    }

    public SecretsBuilderV1 icon(String iconPath, String contentType) {
        return register(REQUEST_GET_ICON, new IconRequestExecutor(iconPath, contentType));
    }

    public SecretsBuilderV1 configMetadata(Class<?> configClass) {
        return configMetadata(configClass, true);
    }

    public SecretsBuilderV1 configMetadata(Class<?> configClass, boolean addDefaultValidators) {
        if (addDefaultValidators) {
            ((ValidationExecutor) getExecutor(REQUEST_VALIDATE_CONFIG))
                    .addAll(new DefaultValidator(configClass));
        }
        return register(REQUEST_GET_CONFIG_METADATA, new MetadataExecutor(configClass));
    }

    public SecretsBuilderV1 configView(String configViewTemplatePath) {
        return register(REQUEST_GET_CONFIG_VIEW, new ViewRequestExecutor(configViewTemplatePath));
    }


    public SecretsBuilderV1 validateSecretConfig(Validator... validators) {
        ((ValidationExecutor) getExecutor(REQUEST_VALIDATE_CONFIG)).addAll(validators);
        return self;
    }

    public SecretsBuilderV1 lookup(LookupExecutor lookupExecutor) {
        return register(REQUEST_SECRETS_LOOKUP, lookupExecutor);
    }
}
