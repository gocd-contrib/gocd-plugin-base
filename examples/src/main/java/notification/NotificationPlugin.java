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

package notification;

import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.dispatcher.BaseBuilder;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.dispatcher.notification.NotificationType;
import cd.go.plugin.base.executors.notification.AgentStatusNotificationExecutor;
import cd.go.plugin.base.executors.notification.StageStatusNotificationExecutor;
import cd.go.plugin.base.executors.notification.models.AgentStatusRequest;
import cd.go.plugin.base.executors.notification.models.StageStatusRequest;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Extension
public class NotificationPlugin implements GoPlugin {
    private RequestDispatcher dispatcher;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        dispatcher = BaseBuilder.forNotification().v4()
                .pluginSettings(PluginSettings.class)
                .validatePluginSettings(new PluginSettingsValidator())
                .notificationInterestedIn(NotificationType.STAGE_STATUS, NotificationType.AGENT_STATUS)
                .pluginSettingsView("/notification-plugin-settings.view.html")
                .stageStatus(
                        new StageStatusNotificationExecutor() {
                            @Override
                            protected GoPluginApiResponse execute(StageStatusRequest stageStatusRequest) {
                                return null;
                            }
                        }
                )
                .agentStatus(
                        new AgentStatusNotificationExecutor() {
                            @Override
                            protected GoPluginApiResponse execute(AgentStatusRequest agentStatusRequest) {
                                return null;
                            }
                        }
                ).build();
    }


    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        return dispatcher.dispatch(request);
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("notification", List.of("2.0"));
    }
}

class PluginSettings {
    @Expose
    @SerializedName("URL")
    @Property(name = "URL", required = true)
    private String url;

    @Expose
    @SerializedName("Token")
    @Property(name = "Token", required = true, secure = true)
    private String token;
}

class PluginSettingsValidator implements Validator {
    @Override
    public ValidationResult validate(Map<String, String> requestBody) {
        ValidationResult result = new ValidationResult();
        try {
            URI url = new URI(requestBody.get("URL"));
            if (url.getScheme() == null || !"https".equalsIgnoreCase(url.getScheme())) {
                result.add("URL", "Must be https url");
            }
        } catch (URISyntaxException e) {
            result.add("URL", "Failed to parse URL.");
        }
        return result;
    }
}
