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

package cd.go.plugin.base.dispatcher.authorization;

import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.test_helper.annotations.JsonSource;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import cd.go.plugin.base.executors.AbstractExecutor;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class AuthorizationBuilderV2Test {
    @Mock
    private GoPluginApiRequest request;
    private AuthorizationBuilderV2 authorizationBuilderV2;

    @BeforeEach
    void setUp() {
        initMocks(this);
        authorizationBuilderV2 = new AuthorizationBuilderV2();
    }

    @Test
    void shouldSupportGetIconCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_ICON);
        final RequestDispatcher dispatcher = authorizationBuilderV2
                .icon("/gocd.png", "image/png")
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/authorization/capabilities.json")
    void shouldSupportGetCapabilitiesCall(String expectedResponse) throws UnhandledRequestTypeException, JSONException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_CAPABILITIES);
        final RequestDispatcher dispatcher = authorizationBuilderV2
                .capabilities(SupportedAuthType.Password, false, false, true)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertEquals(expectedResponse, response.responseBody(), true);
    }

    @Nested
    class AuthConfig {
        @Test
        void shouldSupportValidateCall() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_VALIDATE_AUTH_CONFIG);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = authorizationBuilderV2
                    .validateAuthConfig(validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }

        @Test
        void shouldSupportMetadata() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_AUTH_CONFIG_METADATA);
            final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                    .authConfigMetadata(DummyAuthConfig.class)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }


        @Test
        void shouldSupportGetViewCall() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_AUTH_CONFIG_VIEW);
            final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                    .authConfigView("/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        private class DummyAuthConfig {
        }

    }

    @Nested
    class RoleConfig {
        @Test
        void shouldSupportValidateCall() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_VALIDATE_ROLE_CONFIG);
            final Validator validator = mock(Validator.class);
            when(validator.validate(any())).thenReturn(new ValidationResult());

            final RequestDispatcher dispatcher = authorizationBuilderV2
                    .validateRoleConfig(validator)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            verify(validator).validate(any());
        }

        @Test
        void shouldSupportMetadata() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_ROLE_CONFIG_METADATA);
            final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                    .roleConfigMetadata(DummyRoleConfig.class)
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }

        @Test
        void shouldSupportGetViewCall() throws UnhandledRequestTypeException {
            when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_ROLE_CONFIG_VIEW);
            final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                    .roleConfigView("/dummy-template.html")
                    .build();

            final GoPluginApiResponse response = dispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
        }


        private class DummyRoleConfig {
        }
    }


    @Test
    void shouldSupportGetUserCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_GET_USER_ROLES);
        when(request.requestBody()).thenReturn("get-role");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .getUserRoles(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: get-role");
    }

    @Test
    void shouldSupportIsValidUserCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_IS_VALID_USER);
        when(request.requestBody()).thenReturn("validate-user");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .isValidUser(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: validate-user");
    }

    @Test
    void shouldSupportAuthenticateCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_AUTHENTICATE_USER);
        when(request.requestBody()).thenReturn("authenticate-user");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .authenticateUser(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: authenticate-user");
    }

    @Test
    void shouldSupportSearchUserCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_SEARCH_USERS);
        when(request.requestBody()).thenReturn("search-user");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .searchUser(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: search-user");
    }

    @Test
    void shouldSupportGetAccessTokenCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_ACCESS_TOKEN);
        when(request.requestBody()).thenReturn("access-token");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .getAccessToken(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: access-token");
    }

    @Test
    void shouldSupportAuthorizationUrlCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_AUTHORIZATION_SERVER_URL);
        when(request.requestBody()).thenReturn("authorization-server-url");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .authorizationServerUrl(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: authorization-server-url");
    }

    @Test
    void shouldSupportVerifyConnectionCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(AuthorizationBuilderV2.REQUEST_VERIFY_CONNECTION);
        when(request.requestBody()).thenReturn("verify-connection");

        final RequestDispatcher dispatcher = new AuthorizationBuilderV2()
                .verifyConnection(new DummyExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("response for request body: verify-connection");
    }

    class DummyExecutor extends AbstractExecutor<String> {
        @Override
        protected GoPluginApiResponse execute(String request) {
            return DefaultGoPluginApiResponse.success(String.format("response for request body: %s", request));
        }

        @Override
        protected String parseRequest(String body) {
            return body;
        }
    }
}