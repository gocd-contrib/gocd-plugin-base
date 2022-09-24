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

package cd.go.plugin.base.dispatcher.artifact;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.AbstractExecutor;
import cd.go.plugin.base.executors.artifact.FetchArtifactRequest;
import cd.go.plugin.base.executors.artifact.PublishArtifactRequest;
import cd.go.plugin.base.test_helper.annotations.JsonSource;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class ArtifactBuilderV1Test {
    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void shouldSupportGetIconCall() throws Exception {
        when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_GET_ICON);
        final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                .icon("/gocd.png", "image/png")
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Nested
    class ArtifactStore {
        @Test
        void shouldSupportGetMetadata() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_ARTIFACT_STORE_METADATA);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .artifactStoreConfig(DummyArtifactStore.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportGetView() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_ARTIFACT_STORE_VIEW);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .artifactStoreConfig(DummyArtifactStore.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportValidate() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_ARTIFACT_STORE_VALIDATE);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .artifactStoreConfig(DummyArtifactStore.class, "/dummy-template.html", validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }

        @Test
        void shouldValidateUsingDefaultValidatorIfNoneConfigured() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_ARTIFACT_STORE_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .artifactStoreConfig(DummyArtifactStore.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[{" +
                    "\"key\":\"DockerRegistryURL\"," +
                    "\"message\":\"DockerRegistryURL must not be blank.\"}" +
                    "]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }

        @Test
        void shouldNotPerformDefaultValidationWhenExplicitlySetToFalse() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_ARTIFACT_STORE_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .artifactStoreConfig(DummyArtifactStore.class, "/dummy-template.html", false)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }
    }

    @Nested
    class PublishArtifact {
        @Test
        void shouldSupportGetMetadata() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT_METADATA);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .publishArtifactConfig(PublishArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportGetView() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT_VIEW);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .publishArtifactConfig(PublishArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportValidate() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT_VALIDATE);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .publishArtifactConfig(PublishArtifactConfig.class, "/dummy-template.html", validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }

        @Test
        void shouldValidateUsingDefaultValidatorIfNoneConfigured() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .publishArtifactConfig(PublishArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[{" +
                    "\"key\":\"DockerImage\"," +
                    "\"message\":\"DockerImage must not be blank.\"}" +
                    "]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }

        @Test
        void shouldNotPerformDefaultValidationWhenExplicitlySetToFalse() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .publishArtifactConfig(PublishArtifactConfig.class, "/dummy-template.html", false)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }
    }

    @Nested
    class FetchArtifact {
        @Test
        void shouldSupportGetMetadata() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT_METADATA);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .fetchArtifactConfig(FetchArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportGetView() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT_VIEW);
            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .fetchArtifactConfig(FetchArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportValidate() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT_VALIDATE);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .fetchArtifactConfig(FetchArtifactConfig.class, "/dummy-template.html", validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }

        @Test
        void shouldValidateUsingDefaultValidatorIfNoneConfigured() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .fetchArtifactConfig(FetchArtifactConfig.class, "/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[{" +
                    "\"key\":\"DockerImageWithTag\"," +
                    "\"message\":\"DockerImageWithTag must not be blank.\"}" +
                    "]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }

        @Test
        void shouldNotPerformDefaultValidationWhenExplicitlySetToFalse() throws Exception {
            when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT_VALIDATE);

            final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                    .fetchArtifactConfig(FetchArtifactConfig.class, "/dummy-template.html", false)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            String expectedOutput = "[]";
            JSONAssert.assertEquals(expectedOutput, response.responseBody(), true);
        }
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/artifact/publish-artifact-request.json")
    void shouldSupportPublishArtifact(String inputJSON) throws Exception {
        when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_PUBLISH_ARTIFACT);
        when(request.requestBody()).thenReturn(inputJSON);

        final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                .publish(new DummyPublishExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("Artifact published");
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/artifact/fetch-artifact-request.json")
    void shouldSupportFetchArtifact(String inputJSON) throws Exception {
        when(request.requestName()).thenReturn(ArtifactBuilderV2.REQUEST_FETCH_ARTIFACT);
        when(request.requestBody()).thenReturn(inputJSON);

        final RequestDispatcher dispatcher = new ArtifactBuilderV2()
                .fetch(new DummyFetchExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("Artifact fetched");
    }
}

class FetchRequest extends FetchArtifactRequest<ArtifactMetadata, FetchArtifactConfig, DummyArtifactStore> {

}

class DummyFetchExecutor extends AbstractExecutor<FetchRequest> {
    @Override
    protected GoPluginApiResponse execute(FetchRequest request) {
        assertThat(request.getArtifactMetadata())
                .isInstanceOf(ArtifactMetadata.class);
        assertThat(request.getFetchArtifactConfig())
                .isInstanceOf(FetchArtifactConfig.class);
        assertThat(request.getArtifactStoreConfig())
                .isInstanceOf(DummyArtifactStore.class);
        assertThat(request.getWorkingDir()).isEqualTo("/Users/varshavs/gocd/agent/pipelines/build");
        return DefaultGoPluginApiResponse.success("Artifact fetched");
    }

    @Override
    protected FetchRequest parseRequest(String requestBody) {
        return GsonTransformer.fromJson(requestBody, FetchRequest.class);
    }
}

class PublishRequest extends PublishArtifactRequest<PublishArtifactConfig, DummyArtifactStore> {

}

class DummyPublishExecutor extends AbstractExecutor<PublishRequest> {
    @Override
    protected GoPluginApiResponse execute(PublishRequest request) {
        assertThat(request.getEnvironmentVariables())
                .hasSize(3)
                .containsEntry("Foo", "Bar")
                .containsEntry("GO_PIPELINE_NAME", "build")
                .containsEntry("GO_TRIGGER_USER", "admin");
        assertThat(request.getArtifactPlan().getConfiguration())
                .isInstanceOf(PublishArtifactConfig.class);
        assertThat(request.getArtifactStore().getConfiguration())
                .isInstanceOf(DummyArtifactStore.class);
        assertThat(request.getWorkingDir()).isEqualTo("/Users/varshavs/gocd/agent/pipelines/build");
        return DefaultGoPluginApiResponse.success("Artifact published");
    }

    @Override
    protected PublishRequest parseRequest(String requestBody) {
        return GsonTransformer.fromJson(requestBody, PublishRequest.class);
    }
}

class DummyArtifactStore {
    @Expose
    @SerializedName("DockerRegistryURL")
    @Property(name = "DockerRegistryURL", required = true)
    private String url;
}

class PublishArtifactConfig {
    @Expose
    @SerializedName("DockerImage")
    @Property(name = "DockerImage", required = true)
    private String image;
}

class FetchArtifactConfig {
    @Expose
    @SerializedName("DockerImageWithTag")
    @Property(name = "DockerImageWithTag", required = true)
    private String imageWithTag;
}

class ArtifactMetadata {
}
