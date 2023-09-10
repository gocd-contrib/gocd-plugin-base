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

package cd.go.plugin.base.dispatcher.elastic;

import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.AbstractExecutor;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class ElasticBuilderV5Test {
    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldSupportGetIconCall() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_GET_ICON);
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .icon("/gocd.png", "image/png")
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportGetCapabilitiesCall() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_CAPABILITIES);
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .capabilities(false, false, true)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        final String expectedResponse = "{\n" +
                "  \"supports_agent_status_report\": true,\n" +
                "  \"supports_cluster_status_report\": false,\n" +
                "  \"supports_plugin_status_report\": false\n" +
                "}";
        assertEquals(expectedResponse, response.responseBody(), true);
    }

    @Nested
    class ClusterProfile {
        @Test
        void shouldSupportMetadata() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_GET_CLUSTER_PROFILE_METADATA);
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .clusterProfileMetadata(ClusterProfileConfig.class)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportView() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_GET_CLUSTER_PROFILE_VIEW);
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .clusterProfileView("/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        class ClusterProfileConfig {
        }
    }

    @Nested
    class ElasticAgentProfile {
        @Test
        void shouldSupportMetadata() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_GET_ELASTIC_AGENT_PROFILE_METADATA);
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .elasticProfileMetadata(ElasticAgentProfileConfig.class)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportView() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_GET_ELASTIC_AGENT_PROFILE_VIEW);
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .elasticProfileView("/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        class ElasticAgentProfileConfig {
        }
    }

    @Test
    void shouldSupportCreateAgent() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_CREATE_AGENT);
        when(request.requestBody()).thenReturn("create agent");

        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .createAgent(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body create agent");
    }

    @Test
    void shouldSupportServerPing() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_SERVER_PING);
        when(request.requestBody()).thenReturn("server ping");
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .serverPing(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body server ping");
    }

    @Test
    void shouldSupportShouldAssignWork() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_SHOULD_ASSIGN_WORK);
        when(request.requestBody()).thenReturn("assign work");
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .shouldAssignWork(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body assign work");
    }


    @Nested
    class StatusReports {
        @Test
        void shouldSupportPluginStatusReport() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_PLUGIN_STATUS_REPORT);
            when(request.requestBody()).thenReturn("plugin status report");
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .pluginStatusReport(new DummyExecutor())
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            assertThat(response.responseBody()).isEqualTo("response for request body plugin status report");
        }

        @Test
        void shouldSupportClusterStatusReport() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_CLUSTER_STATUS_REPORT);
            when(request.requestBody()).thenReturn("cluster status report");
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .clusterStatusReport(new DummyExecutor())
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            assertThat(response.responseBody()).isEqualTo("response for request body cluster status report");
        }

        @Test
        void shouldSupportAgentStatusReport() throws Exception {
            when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_AGENT_STATUS_REPORT);
            when(request.requestBody()).thenReturn("agent status report");
            final RequestDispatcher dispatcher = new ElasticBuilderV5()
                    .agentStatusReport(new DummyExecutor())
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            assertThat(response.responseBody()).isEqualTo("response for request body agent status report");
        }
    }

    @Test
    void shouldSupportJobCompletion() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_JOB_COMPLETION);
        when(request.requestBody()).thenReturn("job completion");
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .jobCompletion(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body job completion");
    }

    @Test
    void shouldSupportMigrateConfiguration() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_MIGRATE_CONFIGURATION);
        when(request.requestBody()).thenReturn("migrate config");
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .migrateConfiguration(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body migrate config");
    }

    @Test
    void shouldSupportClusterConfigChanged() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_CLUSTER_PROFILE_CHANGED);
        when(request.requestBody()).thenReturn("cluster profile changed");
        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .clusterProfileChanged(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body cluster profile changed");
    }

    @Test
    void shouldSupportValidateClusterProfile() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_VALIDATE_CLUSTER_PROFILE);
        final Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenReturn(new ValidationResult());

        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .validateClusterProfile(validator)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }

    @Test
    void shouldSupportValidateElasticAgentProfile() throws Exception {
        when(request.requestName()).thenReturn(ElasticBuilderV5.REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE);
        final Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenReturn(new ValidationResult());

        final RequestDispatcher dispatcher = new ElasticBuilderV5()
                .validateElasticProfile(validator)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }

    class DummyExecutor extends AbstractExecutor<String> {
        @Override
        protected GoPluginApiResponse execute(String request) {
            return DefaultGoPluginApiResponse.success(String.format("response for request body %s", request));
        }

        @Override
        protected String parseRequest(String body) {
            return body;
        }
    }
}
