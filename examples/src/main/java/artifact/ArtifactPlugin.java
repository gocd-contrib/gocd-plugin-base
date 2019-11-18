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

package artifact;

import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.dispatcher.BaseBuilder;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.AbstractExecutor;
import cd.go.plugin.base.executors.artifact.FetchArtifactRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static java.util.Collections.singletonList;

@Extension
public class ArtifactPlugin implements GoPlugin {
    private RequestDispatcher requestDispatcher;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        requestDispatcher = BaseBuilder.forArtifact()
                .v2()
                .icon("/plugin-icon.png", "image/png")  // Icon file path and content type
                .artifactStoreConfig(ArtifactStore.class, "/artifact-store-view.html")  // Artifact store configuration
                .publishArtifactConfig(PublishArtifactConfig.class, "/publish-artifact-view.html")  // Publish artifact view configuration
                .fetchArtifactConfig(FetchArtifactConfig.class, "/fetch-artifact-view.html")  // Fetch artifact view configuration
                .fetch(new FetchExecutor())
                .publish(new PublishExecutor())
                .build();
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            return requestDispatcher.dispatch(request); // use previously built request dispatcher to handle server requests
        } catch (Exception e) {
            //Handle it
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("artifact", singletonList("2.0"));
    }
}

class ArtifactStore {
    @Expose
    @SerializedName("RegistryURL")
    @Property(name = "RegistryURL", required = true)
    private String registryURL;

    @Expose
    @SerializedName("Token")
    @Property(name = "Token", required = true, secure = true)
    private String token;
}

class PublishArtifactConfig {
    @Expose
    @SerializedName("Image")
    @Property(name = "Image", required = true)
    private String registryURL;

    @Expose
    @SerializedName("Tag")
    @Property(name = "Tag", required = true)
    private String tag;
}

class FetchArtifactConfig {
    @Expose
    @SerializedName("Image")
    @Property(name = "Image", required = true)
    private String registryURL;

    @Expose
    @SerializedName("Tag")
    @Property(name = "Tag", required = true)
    private String tag;

    @Expose
    @SerializedName("Digest")
    @Property(name = "Digest", required = true)
    private String digest;
}

class ArtifactMetadata {
    @Expose
    @SerializedName("Foo")
    @Property(name = "Foo", required = true)
    private String foo;
}

class FetchExecutor extends AbstractExecutor<FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore>> {

    @Override
    protected GoPluginApiResponse execute(FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore> request) {
        //Implement Me!
        return null;
    }

    @Override
    protected FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore> parseRequest(String body) {
        //Implement Me!
        return null;
    }
}

class PublishExecutor extends AbstractExecutor<FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore>> {

    @Override
    protected GoPluginApiResponse execute(FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore> request) {
        //Implement Me!
        return null;
    }

    @Override
    protected FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, ArtifactStore> parseRequest(String body) {
        //Implement Me!
        return null;
    }
}
