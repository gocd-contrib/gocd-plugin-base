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

package com.github.bdpiparva.plugin.base.dispatcher.elastic;

import com.github.bdpiparva.plugin.base.dispatcher.VersionedExtensionBuilder;
import com.github.bdpiparva.plugin.base.executors.*;
import com.github.bdpiparva.plugin.base.executors.elastic.CapabilitiesExecutor;
import com.github.bdpiparva.plugin.base.validation.DefaultValidator;
import com.github.bdpiparva.plugin.base.validation.Validator;

public class ElasticBuilderV5 extends VersionedExtensionBuilder<ElasticBuilderV5> {
    protected static final String REQUEST_GET_ICON = "cd.go.elastic-agent.get-icon";

    protected static final String REQUEST_CAPABILITIES = "cd.go.elastic-agent.get-capabilities";
    protected static final String REQUEST_GET_ELASTIC_AGENT_PROFILE_METADATA = "cd.go.elastic-agent.get-elastic-agent-profile-metadata";
    protected static final String REQUEST_GET_ELASTIC_AGENT_PROFILE_VIEW = "cd.go.elastic-agent.get-elastic-agent-profile-view";
    protected static final String REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE = "cd.go.elastic-agent.validate-elastic-agent-profile";

    protected static final String REQUEST_GET_CLUSTER_PROFILE_METADATA = "cd.go.elastic-agent.get-cluster-profile-metadata";
    protected static final String REQUEST_GET_CLUSTER_PROFILE_VIEW = "cd.go.elastic-agent.get-cluster-profile-view";
    protected static final String REQUEST_VALIDATE_CLUSTER_PROFILE = "cd.go.elastic-agent.validate-cluster-profile";

    protected static final String REQUEST_CREATE_AGENT = "cd.go.elastic-agent.create-agent";
    protected static final String REQUEST_SERVER_PING = "cd.go.elastic-agent.server-ping";
    protected static final String REQUEST_SHOULD_ASSIGN_WORK = "cd.go.elastic-agent.should-assign-work";

    protected static final String REQUEST_AGENT_STATUS_REPORT = "cd.go.elastic-agent.agent-status-report";
    protected static final String REQUEST_CLUSTER_STATUS_REPORT = "cd.go.elastic-agent.cluster-status-report";
    protected static final String REQUEST_PLUGIN_STATUS_REPORT = "cd.go.elastic-agent.plugin-status-report";

    protected static final String REQUEST_JOB_COMPLETION = "cd.go.elastic-agent.job-completion";
    protected static final String REQUEST_MIGRATE_CONFIGURATION = "cd.go.elastic-agent.migrate-config";
    protected static final String REQUEST_CLUSTER_PROFILE_CHANGED = "cd.go.elastic-agent.cluster-profile-changed";

    protected ElasticBuilderV5() {
        registry.put(REQUEST_VALIDATE_CLUSTER_PROFILE, new ValidationExecutor());
        registry.put(REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE, new ValidationExecutor());
    }

    public ElasticBuilderV5 icon(String iconPath, String contentType) {
        return register(REQUEST_GET_ICON, new IconRequestExecutor(iconPath, contentType));
    }

    public ElasticBuilderV5 capabilities(boolean supportsPluginStatusReport, boolean supportsClusterStatusReport, boolean supportsAgentStatusReport) {
        return register(REQUEST_CAPABILITIES, new CapabilitiesExecutor(supportsPluginStatusReport, supportsClusterStatusReport, supportsAgentStatusReport));
    }


    public ElasticBuilderV5 clusterProfileMetadata(Class<?> clusterProfileClass) {
        return clusterProfileMetadata(clusterProfileClass, true);
    }

    public ElasticBuilderV5 clusterProfileMetadata(Class<?> clusterProfileClass, boolean addDefaultValidator) {
        if (addDefaultValidator) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CLUSTER_PROFILE)).addAll(new DefaultValidator(clusterProfileClass));
        }
        return register(REQUEST_GET_CLUSTER_PROFILE_METADATA, new MetadataExecutor(clusterProfileClass));
    }

    public ElasticBuilderV5 validateClusterProfile(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CLUSTER_PROFILE)).addAll(validators);
        return this;
    }


    public ElasticBuilderV5 clusterProfileView(String clusterProfileTemplatePath) {
        return register(REQUEST_GET_CLUSTER_PROFILE_VIEW, new ViewRequestExecutor(clusterProfileTemplatePath));
    }

    public ElasticBuilderV5 elasticProfileMetadata(Class<?> agentProfileConfigClass) {
        return elasticProfileMetadata(agentProfileConfigClass, true);
    }

    public ElasticBuilderV5 elasticProfileMetadata(Class<?> agentProfileConfigClass, boolean addDefaultValidator) {
        if (addDefaultValidator) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE)).addAll(new DefaultValidator(agentProfileConfigClass));
        }
        return register(REQUEST_GET_ELASTIC_AGENT_PROFILE_METADATA, new MetadataExecutor(agentProfileConfigClass));
    }

    public ElasticBuilderV5 validateElasticProfile(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE)).addAll(validators);
        return this;
    }

    public ElasticBuilderV5 elasticProfileView(String agentProfileTemplatePath) {
        return register(REQUEST_GET_ELASTIC_AGENT_PROFILE_VIEW, new ViewRequestExecutor(agentProfileTemplatePath));
    }

    public ElasticBuilderV5 createAgent(AbstractExecutor agentExecutor) {
        return register(REQUEST_CREATE_AGENT, agentExecutor);
    }

    public ElasticBuilderV5 serverPing(AbstractExecutor executor) {
        return register(REQUEST_SERVER_PING, executor);
    }

    public ElasticBuilderV5 shouldAssignWork(AbstractExecutor executor) {
        return register(REQUEST_SHOULD_ASSIGN_WORK, executor);
    }

    public ElasticBuilderV5 pluginStatusReport(AbstractExecutor executor) {
        return register(REQUEST_PLUGIN_STATUS_REPORT, executor);
    }

    public ElasticBuilderV5 clusterStatusReport(AbstractExecutor executor) {
        return register(REQUEST_CLUSTER_STATUS_REPORT, executor);
    }

    public ElasticBuilderV5 agentStatusReport(AbstractExecutor executor) {
        return register(REQUEST_AGENT_STATUS_REPORT, executor);
    }

    public ElasticBuilderV5 jobCompletion(AbstractExecutor executor) {
        return register(REQUEST_JOB_COMPLETION, executor);
    }

    public ElasticBuilderV5 migrateConfiguration(AbstractExecutor executor) {
        return register(REQUEST_MIGRATE_CONFIGURATION, executor);
    }

    public ElasticBuilderV5 clusterProfileChanged(AbstractExecutor executor) {
        return register(REQUEST_CLUSTER_PROFILE_CHANGED, executor);
    }
}
