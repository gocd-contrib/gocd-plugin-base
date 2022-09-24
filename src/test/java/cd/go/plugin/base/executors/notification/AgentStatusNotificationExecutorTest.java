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

package cd.go.plugin.base.executors.notification;

import cd.go.plugin.base.executors.notification.models.AgentStatusRequest;
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