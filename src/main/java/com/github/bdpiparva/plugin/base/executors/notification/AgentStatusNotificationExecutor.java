package com.github.bdpiparva.plugin.base.executors.notification;

import com.github.bdpiparva.plugin.base.executors.Executor;
import com.github.bdpiparva.plugin.base.executors.notification.models.AgentStatusRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.github.bdpiparva.plugin.base.executors.notification.models.AgentStatusRequest.fromJSON;

public abstract class AgentStatusNotificationExecutor implements Executor {
    @Override
    public final GoPluginApiResponse execute(GoPluginApiRequest request) {
        return execute(fromJSON(request.requestBody()));
    }

    protected abstract GoPluginApiResponse execute(AgentStatusRequest agentStatusRequest);
}
