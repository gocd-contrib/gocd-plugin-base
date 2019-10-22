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

import cd.go.plugin.base.annotations.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class MetadataExtractor {
    public List<MetadataHolder> forClass(Class<?> clazz) {
        final List<MetadataHolder> metadataHolderList = new ArrayList<>();

        return Arrays.stream(clazz.getDeclaredFields())
                .map(this::extractMetadataFromField)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MetadataHolder extractMetadataFromField(Field declaredField) {
        final Property property = declaredField.getAnnotation(Property.class);
        if (isValid(declaredField)) {
            return new MetadataHolder(property);
        }
        return null;
    }

    public static boolean isValid(Field declaredField) {
        final String TEMPLATE = "Must have %s annotation along with %s.";
        final Optional<Expose> expose = getAnnotation(declaredField, Expose.class);
        final Optional<Property> property = getAnnotation(declaredField, Property.class);

        if (property.isPresent() && expose.isPresent()) {
            final Optional<SerializedName> serializedName = getAnnotation(declaredField, SerializedName.class);
            if (serializedName.isPresent()) {
                if (!StringUtils.equals(serializedName.get().value(), property.get().name())) {
                    throw new RuntimeException(String.format("@Property name is not same as @SerializeName value for field '%s'.", declaredField.getName()));
                }
            } else if (!StringUtils.equals(property.get().name(), declaredField.getName())) {
                throw new RuntimeException(format(TEMPLATE, "@SerializeName", "@Expose"));
            }

            return true;
        }

        if (expose.isPresent() || property.isPresent()) {
            throw new RuntimeException(format(TEMPLATE, "@Property", "@Expose"));
        }

        return false;
    }

    private static <T extends Annotation> Optional<T> getAnnotation(Field declaredField, Class<T> annotationClass) {
        return ofNullable(declaredField.getAnnotation(annotationClass));
    }
}
