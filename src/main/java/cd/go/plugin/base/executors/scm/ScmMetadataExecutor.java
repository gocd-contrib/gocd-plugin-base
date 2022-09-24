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

package cd.go.plugin.base.executors.scm;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.annotations.scm.PartOfIdentity;
import cd.go.plugin.base.executors.Executor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cd.go.plugin.base.executors.scm.InvalidConfigurationException.bomb;
import static cd.go.plugin.base.metadata.MetadataExtractor.isValid;
import static java.lang.String.format;

public class ScmMetadataExecutor implements Executor {
    private final Class<?> scmConfigClass;

    public ScmMetadataExecutor(Class<?> scmConfigClass) {
        this.scmConfigClass = scmConfigClass;
    }

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        final AtomicInteger index = new AtomicInteger();
        final Map<String, ScmMetadata> metadataMap = new HashMap<>();
        for (Field declaredField : scmConfigClass.getDeclaredFields()) {
            this.extractMetadataFromField(declaredField, index, metadataMap);
        }

        return DefaultGoPluginApiResponse.success(GsonTransformer.toJson(metadataMap));
    }

    private void extractMetadataFromField(Field declaredField, AtomicInteger index, Map<String, ScmMetadata> metadataMap) {
        if (isValid(declaredField)) {
            final Property property = declaredField.getAnnotation(Property.class);
            boolean partOfIdentity = declaredField.isAnnotationPresent(PartOfIdentity.class);

            if (metadataMap.containsKey(property.name())) {
                throw bomb(property.name(), scmConfigClass);
            }

            metadataMap.put(property.name(), new ScmMetadata(property, partOfIdentity, index.getAndIncrement()));
        }
    }
}

class InvalidConfigurationException extends RuntimeException {
    InvalidConfigurationException(String message) {
        super(message);
    }

    static InvalidConfigurationException bomb(String name, Class<?> clazz) {
        throw new RuntimeException(format("Found multiple declaration of @Property(name=\"%s\"...) in class %s", name, clazz));
    }
}

class ScmMetadata {
    @Expose
    @SerializedName("display-name")
    private final String displayName;
    @Expose
    @SerializedName("required")
    private final boolean required;
    @Expose
    @SerializedName("secure")
    private final boolean secure;
    @Expose
    @SerializedName("part-of-identity")
    private final boolean partOfIdentity;
    @Expose
    @SerializedName("display-order")
    private final String displayOrder;

    ScmMetadata(Property property, boolean partOfIdentity, int displayOrder) {
        required = property.required();
        secure = property.secure();
        this.partOfIdentity = partOfIdentity;
        this.displayOrder = String.valueOf(displayOrder);
        this.displayName = property.displayName();
    }
}
