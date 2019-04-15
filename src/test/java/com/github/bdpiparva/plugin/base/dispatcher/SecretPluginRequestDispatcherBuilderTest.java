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

package com.github.bdpiparva.plugin.base.dispatcher;

import com.github.bdpiparva.plugin.base.annotations.Property;
import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import com.github.bdpiparva.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;

import static com.github.bdpiparva.plugin.base.DispatcherBuilder.REQUEST_GET_ICON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecretPluginRequestDispatcherBuilderTest {

    @Test
    void shouldBuildRequestDispatcherForSecretsPlugin() {
        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .build();
        assertThat(dispatcher).isNotNull();
    }

    @Test
    void shouldBuildDispatcherWhichHandlesGetIconRequest() throws UnhandledRequestTypeException {
        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .icon("/gocd.png", "image/png")
                .build();

        final GoPluginApiResponse response = dispatcher
                .dispatch(request(REQUEST_GET_ICON));

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldBuildDispatcherWhichHandlesGetSecretConfigMetadata() throws UnhandledRequestTypeException {
        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .configMetadata(SecretConfig.class)
                .build();

        final GoPluginApiResponse response = dispatcher
                .dispatch(request(SecretPluginRequestDispatcherBuilder.REQUEST_GET_CONFIG_METADATA));

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldBuildDispatcherWhichHandlesGetSecretConfigView() throws UnhandledRequestTypeException {
        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .configView("/dummy-template.html")
                .build();

        final GoPluginApiResponse response = dispatcher
                .dispatch(request(SecretPluginRequestDispatcherBuilder.REQUEST_GET_CONFIG_VIEW));

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldBuildDispatcherWhichHandlesValidateSecretConfigRequest() throws UnhandledRequestTypeException {
        final Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenReturn(new ValidationResult());

        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .validateSecretConfig(validator)
                .build();

        final GoPluginApiResponse response = dispatcher
                .dispatch(request(SecretPluginRequestDispatcherBuilder.REQUEST_VALIDATE_CONFIG));

        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }

    @Test
    void shouldBuildDispatcherWhichHandlesLookupSecretsRequest() throws UnhandledRequestTypeException {
        final RequestDispatcher dispatcher = new SecretPluginRequestDispatcherBuilder(null)
                .lookup(new DummyLookupExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher
                .dispatch(request(SecretPluginRequestDispatcherBuilder.REQUEST_SECRETS_LOOKUP));

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("result");
    }

    private GoPluginApiRequest request(String name) {
        return new DefaultGoPluginApiRequest("secrets", "1.0", name);
    }

    class DummyLookupExecutor extends LookupExecutor<String> {

        @Override
        protected GoPluginApiResponse execute(String request) {
            return DefaultGoPluginApiResponse.success("result");
        }

        @Override
        protected String parseRequest(String body) {
            return body;
        }
    }

    class SecretConfig {
        @Property(name = "Path", required = true)
        private String path;
    }
}