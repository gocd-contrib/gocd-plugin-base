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

package com.github.bdpiparva.plugin.base.validation;

import com.github.bdpiparva.plugin.base.annotations.Property;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultValidatorTest {

    private DefaultValidator validator;
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        request = mock(GoPluginApiRequest.class);
        validator = new DefaultValidator(SomeConfig.class);
    }

    @Test
    void shouldReturnResultWithErrorsIfConfigHasUnknownFields() {
        when(request.requestBody()).thenReturn("{\"Url\":\"http://foo\",\"Permission\":\"unknown field\"}");

        final ValidationResult result = validator.validate(request);

        assertThat(result)
                .hasSize(1)
                .contains(new ValidationError("Permission", "Is an unknown property"));
    }

    @Test
    void shouldReturnResultWithErrorsIfMandatoryFieldHasNoValue() {
        when(request.requestBody()).thenReturn("{\"Path\":\"/foo\"}");

        final ValidationResult result = validator.validate(request);

        assertThat(result)
                .hasSize(1)
                .contains(new ValidationError("Url", "Url must not be blank."));
    }

    @Test
    void shouldReturnResultWithoutErrorsIfConfigIsValid() {
        when(request.requestBody()).thenReturn("{\"Url\":\"/foo\"}");

        final ValidationResult result = validator.validate(request);

        assertThat(result).isEmpty();
    }

    private class SomeConfig {
        @Expose
        @SerializedName("Url")
        @Property(name = "Url", required = true)
        private String url;

        @Expose
        @SerializedName("Path")
        @Property(name = "Path")
        private String path;
    }
}