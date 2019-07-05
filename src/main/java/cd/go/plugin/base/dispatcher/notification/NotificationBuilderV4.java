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

package cd.go.plugin.base.dispatcher.notification;

import cd.go.plugin.base.dispatcher.VersionedExtensionBuilder;
import cd.go.plugin.base.executors.MetadataExecutor;
import cd.go.plugin.base.executors.ValidationExecutor;
import cd.go.plugin.base.executors.ViewRequestExecutor;
import cd.go.plugin.base.executors.notification.AgentStatusNotificationExecutor;
import cd.go.plugin.base.executors.notification.StageStatusNotificationExecutor;
import cd.go.plugin.base.validation.DefaultValidator;
import cd.go.plugin.base.validation.Validator;

import static cd.go.plugin.base.GsonTransformer.toJson;
import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.success;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

public class NotificationBuilderV4 extends VersionedExtensionBuilder<NotificationBuilderV4> {
    protected static final String REQUEST_NOTIFY_PLUGIN_SETTINGS_CHANGE = "go.plugin-settings.plugin-settings-changed";
    protected static final String REQUEST_GET_PLUGIN_SETTINGS_METADATA = "go.plugin-settings.get-configuration";
    protected static final String REQUEST_GET_PLUGIN_SETTINGS_VIEW = "go.plugin-settings.get-view";
    protected static final String REQUEST_VALIDATE_PLUGIN_SETTINGS = "go.plugin-settings.validate-configuration";

    protected static final String REQUEST_NOTIFICATIONS_INTERESTED_IN = "notifications-interested-in";
    public static final String REQUEST_STAGE_STATUS = "stage-status";
    public static final String REQUEST_AGENT_STATUS = "agent-status";

    public NotificationBuilderV4() {
        registry.put(REQUEST_VALIDATE_PLUGIN_SETTINGS, new ValidationExecutor(true));
    }

    public NotificationBuilderV4 pluginSettings(Class<?> notificationConfigClass) {
        return pluginSettings(notificationConfigClass, true);
    }

    public NotificationBuilderV4 pluginSettings(Class<?> notificationConfigClass, boolean addDefaultValidation) {
        if (addDefaultValidation) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_PLUGIN_SETTINGS)).addAll(new DefaultValidator(notificationConfigClass));
        }
        return register(REQUEST_GET_PLUGIN_SETTINGS_METADATA, new MetadataExecutor(notificationConfigClass));
    }

    public NotificationBuilderV4 pluginSettingsView(String pluginSettingsViewTemplatePath) {
        return register(REQUEST_GET_PLUGIN_SETTINGS_VIEW, new ViewRequestExecutor(pluginSettingsViewTemplatePath));
    }

    public NotificationBuilderV4 validatePluginSettings(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_PLUGIN_SETTINGS)).addAll(validators);
        return this;
    }

    public NotificationBuilderV4 notificationInterestedIn(NotificationType... notificationTypes) {
        if (notificationTypes == null || notificationTypes.length == 0) {
            throw new IllegalArgumentException("Provide at least one notification type!");
        }

        final String responseBody = toJson(of(notificationTypes).collect(toList()));
        return register(REQUEST_NOTIFICATIONS_INTERESTED_IN, (request) -> success(responseBody));
    }

    public NotificationBuilderV4 stageStatus(StageStatusNotificationExecutor executor) {
        return register(REQUEST_STAGE_STATUS, executor);
    }

    public NotificationBuilderV4 agentStatus(AgentStatusNotificationExecutor executor) {
        return register(REQUEST_AGENT_STATUS, executor);
    }
}
