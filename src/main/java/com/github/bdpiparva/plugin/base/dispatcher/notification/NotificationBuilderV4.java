package com.github.bdpiparva.plugin.base.dispatcher.notification;

import com.github.bdpiparva.plugin.base.dispatcher.VersionedExtensionBuilder;
import com.github.bdpiparva.plugin.base.executors.MetadataExecutor;
import com.github.bdpiparva.plugin.base.executors.ValidationExecutor;
import com.github.bdpiparva.plugin.base.executors.ViewRequestExecutor;
import com.github.bdpiparva.plugin.base.validation.DefaultValidator;
import com.github.bdpiparva.plugin.base.validation.Validator;

public class NotificationBuilderV4 extends VersionedExtensionBuilder<NotificationBuilderV4> {
    protected static final String REQUEST_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    protected static final String REQUEST_GET_CONFIG_VIEW = "go.plugin-settings.get-view";
    protected static final String REQUEST_VALIDATE_CONFIG = "go.plugin-settings.validate-configuration";


    protected static final String REQUEST_NOTIFICATIONS_INTERESTED_IN = "notifications-interested-in";
    protected static final String REQUEST_STAGE_STATUS = "stage-status";
    protected static final String REQUEST_AGENT_STATUS = "agent-status";

    public NotificationBuilderV4() {
        registry.put(REQUEST_VALIDATE_CONFIG, new ValidationExecutor());
    }

    public NotificationBuilderV4 configuration(Class<?> notificationConfigClass) {
        return configuration(notificationConfigClass, true);
    }

    public NotificationBuilderV4 configuration(Class<?> notificationConfigClass, boolean addDefaultValidation) {
        if (addDefaultValidation) {
            ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CONFIG)).addAll(new DefaultValidator(notificationConfigClass));
        }
        return register(REQUEST_GET_CONFIGURATION, new MetadataExecutor(notificationConfigClass));
    }

    public NotificationBuilderV4 configView(String configViewTemplatePath) {
        return register(REQUEST_GET_CONFIG_VIEW, new ViewRequestExecutor(configViewTemplatePath));
    }

    public NotificationBuilderV4 validateConfig(Validator... validators) {
        ((ValidationExecutor) registry.get(REQUEST_VALIDATE_CONFIG)).addAll(validators);
        return this;
    }
}
