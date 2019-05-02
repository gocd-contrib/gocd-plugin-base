package com.github.bdpiparva.plugin.base.dispatcher.notification;

import com.github.bdpiparva.plugin.base.dispatcher.VersionedExtensionBuilder;
import com.github.bdpiparva.plugin.base.executors.MetadataExecutor;
import com.github.bdpiparva.plugin.base.executors.ValidationExecutor;
import com.github.bdpiparva.plugin.base.executors.ViewRequestExecutor;
import com.github.bdpiparva.plugin.base.validation.DefaultValidator;
import com.github.bdpiparva.plugin.base.validation.Validator;

import static com.github.bdpiparva.plugin.base.GsonTransformer.toJson;
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
}
