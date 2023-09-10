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

package cd.go.plugin.base.dispatcher.notification;

import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.notification.AgentStatusNotificationExecutor;
import cd.go.plugin.base.executors.notification.StageStatusNotificationExecutor;
import cd.go.plugin.base.executors.notification.models.AgentStatusRequest;
import cd.go.plugin.base.executors.notification.models.StageStatusRequest;
import cd.go.plugin.base.validation.ValidationError;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static cd.go.plugin.base.GsonTransformer.fromJson;
import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class NotificationBuilderV4Test {
    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldSupportGetPluginSettingsMetadata() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_GET_PLUGIN_SETTINGS_METADATA);
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .pluginSettings(NotificationConfig.class)
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldAddDefaultValidator() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_VALIDATE_PLUGIN_SETTINGS);
        when(request.requestBody()).thenReturn("{\"plugin-settings\":{\"key-one\":{\"value\":\"value-one\"},\"key-two\":{\"value\":\"value-two\"}}}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .pluginSettings(NotificationConfig.class)
                .build();
        final Type type = new TypeToken<ArrayList<ValidationError>>() {
        }.getType();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        ArrayList<ValidationError> errors = fromJson(response.responseBody(), type);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(errors.size()).isEqualTo(2);
    }

    @Test
    void shouldNotAddDefaultValidator() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_VALIDATE_PLUGIN_SETTINGS);
        when(request.requestBody()).thenReturn("{\"key\":\"value\"}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .pluginSettings(NotificationConfig.class, false)
                .build();

        final Type type = new TypeToken<ArrayList<ValidationError>>() {
        }.getType();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        ArrayList<ValidationError> errors = fromJson(response.responseBody(), type);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(errors.size()).isEqualTo(0);
    }

    @Test
    void shouldSupportGetPluginSettingsView() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_GET_PLUGIN_SETTINGS_VIEW);
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .pluginSettingsView("/dummy-template.html")
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportValidatePluginSettings() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_VALIDATE_PLUGIN_SETTINGS);
        when(request.requestBody()).thenReturn("{\"plugin-settings\":{\"key-one\":{\"value\":\"value-one\"},\"key-two\":{\"value\":\"value-two\"}}}");
        Validator validator = mock(Validator.class);
        when(validator.validate(anyMap())).thenReturn(new ValidationResult());

        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .validatePluginSettings(validator)
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }

    @Nested
    class notificationInterestedIn {
        @BeforeEach
        void setUp() {
            when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_NOTIFICATIONS_INTERESTED_IN);
            when(request.requestBody()).thenReturn("{}");
        }

        @Test
        void shouldSupportNotificationInterestedIn() throws Exception {
            RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                    .notificationInterestedIn(NotificationType.AGENT_STATUS)
                    .build();

            GoPluginApiResponse response = requestDispatcher.dispatch(request);

            assertThat(response.responseCode()).isEqualTo(200);
            assertThat(response.responseBody()).isEqualTo("[\"agent-status\"]");
        }

        @Test
        void shouldErrorOutIfNoNotificationTypeIsSpecified() {
            assertThatCode(() -> new NotificationBuilderV4().notificationInterestedIn())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provide at least one notification type!");
        }
    }

    @Test
    void shouldSupportStageStatusNotificationExecutor() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_STAGE_STATUS);
        when(request.requestBody()).thenReturn("{}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .stageStatus(new StageStatusNotificationExecutor() {
                    @Override
                    protected GoPluginApiResponse execute(StageStatusRequest stageStatusRequest) {
                        return success("stage-status-response");
                    }
                })
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("stage-status-response");
    }

    @Test
    void shouldSupportAgentStatusNotificationExecutor() throws Exception {
        when(request.requestName()).thenReturn(NotificationBuilderV4.REQUEST_AGENT_STATUS);
        when(request.requestBody()).thenReturn("{}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .agentStatus(new AgentStatusNotificationExecutor() {
                    @Override
                    protected GoPluginApiResponse execute(AgentStatusRequest stageStatusRequest) {
                        return success("agent-status-response");
                    }
                })
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("agent-status-response");
    }
}

class NotificationConfig {

}
