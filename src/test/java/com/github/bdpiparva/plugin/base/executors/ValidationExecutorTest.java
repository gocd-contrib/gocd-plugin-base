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

package com.github.bdpiparva.plugin.base.executors;

import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import com.github.bdpiparva.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class ValidationExecutorTest {

    @Test
    void shouldValidateRequestAgainstAllValidators() {
        final Validator validator1 = mock(Validator.class);
        final Validator validator2 = mock(Validator.class);
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        when(validator1.validate(anyMap())).thenReturn(new ValidationResult());
        when(validator2.validate(anyMap())).thenReturn(new ValidationResult());

        new ValidationExecutor(validator1, validator2).execute(request);

        final InOrder inOrder = inOrder(validator1, validator2);
        inOrder.verify(validator1).validate(anyMap());
        inOrder.verify(validator2).validate(anyMap());
    }

    @Test
    void shouldReturnSuccessResponseWhenThereIsNoValidationErrors() {
        final Validator validator = mock(Validator.class);
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        when(validator.validate(anyMap())).thenReturn(new ValidationResult());

        final GoPluginApiResponse response = new ValidationExecutor(validator).execute(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("[]");
    }

    @Test
    void shouldReturnValidationFailedResponseWhenThereIsValidationErrors() throws JSONException {
        final Validator validator = mock(Validator.class);
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);

        final ValidationResult validationResult = new ValidationResult();
        validationResult.add("Path", "Path must not be black.");
        when(validator.validate(anyMap())).thenReturn(validationResult);

        final GoPluginApiResponse response = new ValidationExecutor(validator).execute(request);

        assertThat(response.responseCode()).isEqualTo(200);
        final String expectedResponse = "[\n" +
                "  {\n" +
                "    \"key\": \"Path\",\n" +
                "    \"message\": \"Path must not be black.\"\n" +
                "  }\n" +
                "]";

        assertEquals(expectedResponse, response.responseBody(), true);
    }

    @Nested
    class asMap {
        @Test
        void shouldConvertPluginSettingsRequestBodyToMap() {
            final Map<String, String> requestBodyAsMap = new ValidationExecutor(true)
                    .asMap("{\"plugin-settings\":{\"key-one\":{\"value\":\"value-one\"},\"key-two\":{\"value\":\"value-two\"}}}");

            assertThat(requestBodyAsMap)
                    .hasSize(2)
                    .containsEntry("key-one", "value-one")
                    .containsEntry("key-two", "value-two");
        }

        @Test
        void shouldConvertConfigsRequestBodyToMap() {
            final Map<String, String> requestBodyAsMap = new ValidationExecutor(false)
                    .asMap("{\"Url\":\"some-url\", \"Token\":\"some-token\"}");

            assertThat(requestBodyAsMap)
                    .hasSize(2)
                    .containsEntry("Url", "some-url")
                    .containsEntry("Token", "some-token");
        }
    }

    @Test
    void shouldCallValidatorsForPluginSettingsValidationRequest() {
        final Validator validator1 = mock(Validator.class);
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestBody()).thenReturn("{ \"Url\": \"some-url\"");

        final Map<String, String> requestBody = singletonMap("Url", "some-url");
    }
}