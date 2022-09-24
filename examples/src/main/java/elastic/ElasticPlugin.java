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

package elastic;

import cd.go.plugin.base.dispatcher.BaseBuilder;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.AbstractExecutor;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

import static java.util.Collections.singletonList;

@Extension
public class ElasticPlugin implements GoPlugin {
    private RequestDispatcher requestDispatcher;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        requestDispatcher = BaseBuilder
                .forElastic()
                .v5()
                .icon("/icon.png", "image/png")
                .capabilities(false, true, true)
                .clusterProfileMetadata(ClusterProfile.class)
                .clusterProfileView("/cluster-profile-view.html")
                .validateClusterProfile(new DockerUrlValidator())
                .clusterProfileChanged(new ClusterProfileChangedExecutor())
                .clusterStatusReport(new StatusReportExecutor())
                .elasticProfileMetadata(ElasticProfile.class)
                .elasticProfileView("/elastic-profile-view.html")
                .validateElasticProfile(new DockerImageValidator())
                .agentStatusReport(new AgentStatusReportExecutor())
                .migrateConfiguration(new MigrationExecutor())
                .build();
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            return requestDispatcher.dispatch(request);
        } catch (Exception e) {
            //Handle it
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("elastic-agent", singletonList("5.0"));
    }
}

class ClusterProfile {
    @Expose
    @SerializedName("DockerURI")
    private String dockerURI;
}

class ElasticProfile {
    @Expose
    @SerializedName("Image")
    private String image;
}

class DockerUrlValidator implements Validator {
    @Override
    public ValidationResult validate(Map<String, String> requestBody) {
        // Implement me!!
        return null;
    }
}

class DockerImageValidator implements Validator {
    @Override
    public ValidationResult validate(Map<String, String> requestBody) {
        // Implement me!!
        return null;
    }
}

class StatusReportExecutor extends AbstractExecutor {
    @Override
    protected GoPluginApiResponse execute(Object request) {
        // Implement me!!
        return null;
    }

    @Override
    protected Object parseRequest(String requestBody) {
        // Implement me!!
        return null;
    }
}

class AgentStatusReportExecutor extends AbstractExecutor {
    @Override
    protected GoPluginApiResponse execute(Object request) {
        // Implement me!!
        return null;
    }

    @Override
    protected Object parseRequest(String requestBody) {
        // Implement me!!
        return null;
    }
}

class ClusterProfileChangedExecutor extends AbstractExecutor {
    @Override
    protected GoPluginApiResponse execute(Object request) {
        // Implement me!!
        return null;
    }

    @Override
    protected Object parseRequest(String requestBody) {
        // Implement me!!
        return null;
    }
}

class MigrationExecutor extends AbstractExecutor {
    @Override
    protected GoPluginApiResponse execute(Object request) {
        // Implement me!!
        return null;
    }

    @Override
    protected Object parseRequest(String requestBody) {
        // Implement me!!
        return null;
    }
}
