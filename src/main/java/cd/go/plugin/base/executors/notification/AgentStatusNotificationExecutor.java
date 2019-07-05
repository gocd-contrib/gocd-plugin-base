package cd.go.plugin.base.executors.notification;

import cd.go.plugin.base.executors.Executor;
import cd.go.plugin.base.executors.notification.models.AgentStatusRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public abstract class AgentStatusNotificationExecutor implements Executor {
    @Override
    public final GoPluginApiResponse execute(GoPluginApiRequest request) {
        return execute(AgentStatusRequest.fromJSON(request.requestBody()));
    }

    protected abstract GoPluginApiResponse execute(AgentStatusRequest agentStatusRequest);
}
