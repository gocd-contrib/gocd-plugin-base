package com.github.bdpiparva.plugin.base.executors.notification;

import com.github.bdpiparva.plugin.base.executors.notification.models.AgentStatusRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AgentStatusNotificationExecutorTest {
    @Test
    void shouldCallExecuteWithStageStatusRequest() {
        final GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestBody()).thenReturn("{}");

        final GoPluginApiResponse response = new DummyStageStatusNotificationExecutor().execute(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("foo");
    }

    class DummyStageStatusNotificationExecutor extends AgentStatusNotificationExecutor {

        @Override
        protected GoPluginApiResponse execute(AgentStatusRequest agentStatusRequest) {
            return DefaultGoPluginApiResponse.success("foo");
        }
    }
}