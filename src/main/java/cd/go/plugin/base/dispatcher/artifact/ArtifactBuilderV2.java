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

package cd.go.plugin.base.dispatcher.artifact;

import cd.go.plugin.base.dispatcher.VersionedExtensionBuilder;
import cd.go.plugin.base.executors.*;
import cd.go.plugin.base.executors.artifact.CapabilitiesExecutor;
import cd.go.plugin.base.validation.DefaultValidator;
import cd.go.plugin.base.validation.Validator;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;


public final class ArtifactBuilderV2 extends VersionedExtensionBuilder<ArtifactBuilderV2> {
    protected static final String REQUEST_GET_ICON = "cd.go.artifact.get-icon";
    protected static final String REQUEST_GET_CAPABILITIES = "cd.go.artifact.get-capabilities";

    protected static final String REQUEST_ARTIFACT_STORE_METADATA = "cd.go.artifact.store.get-metadata";
    protected static final String REQUEST_ARTIFACT_STORE_VIEW = "cd.go.artifact.store.get-view";
    protected static final String REQUEST_ARTIFACT_STORE_VALIDATE = "cd.go.artifact.store.validate";

    protected static final String REQUEST_PUBLISH_ARTIFACT_METADATA = "cd.go.artifact.publish.get-metadata";
    protected static final String REQUEST_PUBLISH_ARTIFACT_VIEW = "cd.go.artifact.publish.get-view";
    protected static final String REQUEST_PUBLISH_ARTIFACT_VALIDATE = "cd.go.artifact.publish.validate";

    protected static final String REQUEST_FETCH_ARTIFACT_METADATA = "cd.go.artifact.fetch.get-metadata";
    protected static final String REQUEST_FETCH_ARTIFACT_VIEW = "cd.go.artifact.fetch.get-view";
    protected static final String REQUEST_FETCH_ARTIFACT_VALIDATE = "cd.go.artifact.fetch.validate";

    protected static final String REQUEST_PUBLISH_ARTIFACT = "cd.go.artifact.publish-artifact";
    protected static final String REQUEST_FETCH_ARTIFACT = "cd.go.artifact.fetch-artifact";

    public ArtifactBuilderV2() {
        register(REQUEST_ARTIFACT_STORE_VALIDATE, new ValidationExecutor());

        //TODO: Currently plugin does not support any capabilities
        capabilities();
    }

    public ArtifactBuilderV2 icon(@NonNull String iconPath, @NonNull String contentType) {
        return register(REQUEST_GET_ICON, new IconRequestExecutor(iconPath, contentType));
    }

    public ArtifactBuilderV2 capabilities() {
        return register(REQUEST_GET_CAPABILITIES, new CapabilitiesExecutor());
    }

    public ArtifactBuilderV2 artifactStoreConfig(@NonNull Class<?> artifactStoreConfigClass,
                                                 @NonNull String viewPath,
                                                 Validator... validators) {
        return artifactStoreConfig(artifactStoreConfigClass, viewPath, true, validators);
    }

    public ArtifactBuilderV2 artifactStoreConfig(@NonNull Class<?> artifactStoreConfigClass,
                                                 @NonNull String viewPath,
                                                 boolean addDefaultValidator,
                                                 Validator... validators) {
        register(REQUEST_ARTIFACT_STORE_VALIDATE, getValidationExecutor(artifactStoreConfigClass, addDefaultValidator, validators));
        register(REQUEST_ARTIFACT_STORE_VIEW, new ViewRequestExecutor(viewPath));
        return register(REQUEST_ARTIFACT_STORE_METADATA, new MetadataExecutor(artifactStoreConfigClass));
    }

    public ArtifactBuilderV2 publishArtifactConfig(@NonNull Class<?> publishArtifactConfigClass,
                                                   @NonNull String viewPath,
                                                   Validator... validators) {
        return publishArtifactConfig(publishArtifactConfigClass, viewPath, true, validators);
    }

    public ArtifactBuilderV2 publishArtifactConfig(@NonNull Class<?> publishArtifactConfigClass,
                                                   @NonNull String viewPath,
                                                   boolean addDefaultValidator,
                                                   Validator... validators) {
        register(REQUEST_PUBLISH_ARTIFACT_VALIDATE, getValidationExecutor(publishArtifactConfigClass, addDefaultValidator, validators));
        register(REQUEST_PUBLISH_ARTIFACT_VIEW, new ViewRequestExecutor(viewPath));
        return register(REQUEST_PUBLISH_ARTIFACT_METADATA, new MetadataExecutor(publishArtifactConfigClass));
    }

    public ArtifactBuilderV2 fetchArtifactConfig(@NonNull Class<?> fetchArtifactConfigClass,
                                                 @NonNull String viewPath,
                                                 Validator... validators) {
        return fetchArtifactConfig(fetchArtifactConfigClass, viewPath, true, validators);
    }

    public ArtifactBuilderV2 fetchArtifactConfig(@NonNull Class<?> fetchArtifactConfigClass,
                                                 @NonNull String viewPath,
                                                 boolean addDefaultValidator,
                                                 Validator... validators) {
        register(REQUEST_FETCH_ARTIFACT_VALIDATE, getValidationExecutor(fetchArtifactConfigClass, addDefaultValidator, validators));
        register(REQUEST_FETCH_ARTIFACT_VIEW, new ViewRequestExecutor(viewPath));
        return register(REQUEST_FETCH_ARTIFACT_METADATA, new MetadataExecutor(fetchArtifactConfigClass));
    }

    public ArtifactBuilderV2 publish(AbstractExecutor executor) {
        return register(REQUEST_PUBLISH_ARTIFACT, executor);
    }

    public ArtifactBuilderV2 fetch(AbstractExecutor executor) {
        return register(REQUEST_FETCH_ARTIFACT, executor);
    }

    private ValidationExecutor getValidationExecutor(@NonNull Class<?> artifactStoreConfigClass, boolean addDefaultValidator, Validator[] validators) {
        ValidationExecutor executor = new ValidationExecutor();
        if (addDefaultValidator) {
            executor.addAll(new DefaultValidator(artifactStoreConfigClass));
        }
        if (ArrayUtils.isNotEmpty(validators)) {
            executor.addAll(validators);
        }
        return executor;
    }
}
