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

package cd.go.plugin.base.dispatcher.scm;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.scm.*;
import cd.go.plugin.base.executors.scm.model.*;
import cd.go.plugin.base.executors.scm.request.CheckoutRequest;
import cd.go.plugin.base.executors.scm.request.LatestRevisionSinceRequest;
import cd.go.plugin.base.test_helper.annotations.JsonSource;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static cd.go.plugin.base.GsonTransformer.asMap;
import static cd.go.plugin.base.ResourceReader.readResource;
import static cd.go.plugin.base.dispatcher.scm.ScmBuilderV1Test.MODIFICATION_TIME;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ScmBuilderV1Test {
    public static final Date MODIFICATION_TIME = Date.from(ZonedDateTime.of(2019, 11, 18, 10, 53, 34, 490000000, ZoneId.systemDefault()).toInstant());

    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Nested
    class ScmConfig {
        @ParameterizedTest
        @JsonSource(jsonFiles = "/scm/scm-configuration-metadata.json")
        void shouldSupportGetScmConfiguration(String expectedJson) throws Exception {
            when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_GET_SCM_CONFIGURATION);
            final RequestDispatcher dispatcher = new ScmBuilderV1()
                    .scmConfig(DummyScmConfig.class, "/dummy-template.html", "GitPR")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            JSONAssert.assertEquals(expectedJson, response.responseBody(), true);
        }

        @Test
        void shouldSupportGetScmConfigurationView() throws Exception {
            when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_GET_SCM_VIEW);
            final RequestDispatcher dispatcher = new ScmBuilderV1()
                    .scmConfig(DummyScmConfig.class, "/dummy-template.html", "GitPR")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            Map<String, Object> responseBody = asMap(response.responseBody());

            assertThat(response.responseCode()).isEqualTo(200);
            assertThat(responseBody)
                    .hasSize(2)
                    .containsEntry("template", readResource("/dummy-template.html"))
                    .containsEntry("displayValue", "GitPR");
        }

        @ParameterizedTest
        @JsonSource(jsonFiles = "/scm/scm-validation-error.json")
        void shouldSupportDefaultValidation(String errorJson) throws Exception {
            when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_VALIDATE_SCM_CONFIGURATION);

            final RequestDispatcher dispatcher = new ScmBuilderV1()
                    .scmConfig(DummyScmConfig.class, "/dummy-template.html", "GitPR")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            JSONAssert.assertEquals(errorJson, response.responseBody(), true);
        }

        @Test
        void shouldSupportCustomValidation() throws Exception {
            when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_VALIDATE_SCM_CONFIGURATION);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = new ScmBuilderV1()
                    .scmConfig(DummyScmConfig.class, "/dummy-template.html", "GitPR", validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/scm/valid-scm-config.json")
    void shouldSupportCheckConnection(String scmConfig) throws Exception {
        when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_CHECK_CONNECTION);
        when(request.requestBody()).thenReturn(scmConfig);

        final RequestDispatcher dispatcher = new ScmBuilderV1()
                .checkConnection(new DummyCheckConnectionExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(GsonTransformer.asMap(response.responseBody()))
                .containsEntry("status", "success")
                .containsEntry("messages", singletonList("Done"));
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = {
            "/scm/valid-scm-config.json",
            "/scm/latest-revision-response.json",
    })
    void shouldGetLatestRevision(String scmConfig, String expectedResponse) throws Exception {
        when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_LATEST_REVISION);
        when(request.requestBody()).thenReturn(scmConfig);

        final RequestDispatcher dispatcher = new ScmBuilderV1()
                .latestRevision(new DummyLatestRevisionExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        JSONAssert.assertEquals(expectedResponse, response.responseBody(), true);
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = {
            "/scm/latest-revision-since-request.json",
            "/scm/latest-revision-since-response.json"
    })
    void shouldGetLatestRevisionSince(String requestBody, String expectedResponse) throws Exception {
        when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_LATEST_REVISION_SINCE);
        when(request.requestBody()).thenReturn(requestBody);

        final RequestDispatcher dispatcher = new ScmBuilderV1()
                .latestRevisionSince(new DummyLatestRevisionSinceExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        JSONAssert.assertEquals(expectedResponse, response.responseBody(), true);
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = {
            "/scm/checkout-request.json",
            "/scm/checkout-response.json"
    })
    void shouldCheckoutToRevision(String requestBody, String expectedResponse) throws Exception {
        when(request.requestName()).thenReturn(ScmBuilderV1.REQUEST_CHECKOUT);
        when(request.requestBody()).thenReturn(requestBody);

        final RequestDispatcher dispatcher = new ScmBuilderV1()
                .checkout(new DummyCheckoutExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        JSONAssert.assertEquals(expectedResponse, response.responseBody(), true);
    }
}

class DummyCheckoutExecutor extends CheckoutExecutor<DummyScmConfig> {
    @Override
    protected StatusResponse execute(CheckoutRequest<DummyScmConfig> request) {
        return StatusResponse.success("Successfully checked out to SCM revision provided");
    }
}

class DummyLatestRevisionSinceExecutor extends LatestRevisionSinceExecutor<DummyScmConfig> {

    @Override
    protected LatestRevisionSinceResponse execute(LatestRevisionSinceRequest<DummyScmConfig> request) {
        ModifiedFile file = ModifiedFile.builder().action("added").fileName("readme.md").build();

        return LatestRevisionSinceResponse.builder()
                .revisions(Collections.singletonList(
                        Revision.builder()
                                .revision("revision-1")
                                .modifiedFiles(singletonList(file))
                                .revisionComment("comment")
                                .user("Sheldon Cooper")
                                .data(ScmData.builder().add("Foo", "bar").build())
                                .timestamp(MODIFICATION_TIME)
                                .build()
                ))
                .scmData(ScmData.builder().add("Bar", "Baz").build())
                .build();
    }
}

class DummyLatestRevisionExecutor extends LatestRevisionExecutor<DummyScmConfig> {
    @Override
    protected LatestRevisionResponse execute(DummyScmConfig dummyScmConfig) {
        ModifiedFile file = ModifiedFile.builder().action("added").fileName("readme.md").build();

        return LatestRevisionResponse.builder()
                .revision(Revision.builder()
                        .revision("revision-1")
                        .modifiedFiles(singletonList(file))
                        .revisionComment("comment")
                        .user("Sheldon Cooper")
                        .data(ScmData.builder().add("Foo", "bar").build())
                        .timestamp(MODIFICATION_TIME)
                        .build()
                )
                .scmData(ScmData.builder().add("Bar", "Baz").build())
                .build();
    }
}

class DummyCheckConnectionExecutor extends CheckConnectionExecutor<DummyScmConfig> {
    @Override
    protected StatusResponse execute(DummyScmConfig dummyScmConfig) {
        assertThat(dummyScmConfig.url).isEqualTo("http://localhost.com");
        assertThat(dummyScmConfig.username).isEqualTo("user");
        assertThat(dummyScmConfig.password).isEqualTo("password");
        return StatusResponse.success("Done");
    }
}
