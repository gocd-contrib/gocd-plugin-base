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

package cd.go.plugin.base;

import cd.go.plugin.base.test_helper.annotations.JsonSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ConfigurationParserTest {
    @ParameterizedTest
    @JsonSource(jsonFiles = "/configuration-parser/key-value.json")
    void shouldParseKeyValueConfiguration(String configuration) {
        Map<String, String> map = ConfigurationParser.asMap(configuration);

        assertThat(map)
                .containsEntry("Key1", "Value-1")
                .containsEntry("Key2", "Value-2")
                .containsEntry("Key3", "Value-3");
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/configuration-parser/scm-json-configuration.json")
    void shouldParseOldScmJsonConfiguration(String configuration) {
        Map<String, String> map = ConfigurationParser.asMap(configuration, true);

        assertThat(map)
                .containsEntry("SCM_URL", "http://localhost.com")
                .containsEntry("USERNAME", "user")
                .containsEntry("PASSWORD", "password");
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/configuration-parser/plugin-settings-json-configuration.json")
    void shouldParseOldPluginSettingsJsonConfiguration(String configuration) {
        Map<String, String> map = ConfigurationParser.asMap(configuration, true);

        assertThat(map)
                .containsEntry("GoServerUrl", "http://my.gocd.server.com")
                .containsEntry("AccessKey", "foo")
                .containsEntry("SecretAccessKey", "bar");
    }

    @ParameterizedTest
    @JsonSource(jsonFiles = "/configuration-parser/invalid-old-style-config.json")
    void shouldErrorOutWhenPluginSettingsOrScmConfigurationNotFound(String configuration) {
        assertThatCode( () -> ConfigurationParser.asMap(configuration, true))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Json does not contain \"plugin-settings\" or \"scm-configuration\". Please make sure that given request body is valid json object!");
    }
}
