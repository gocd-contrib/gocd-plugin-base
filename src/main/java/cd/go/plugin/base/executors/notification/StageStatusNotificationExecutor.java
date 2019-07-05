package cd.go.plugin.base.executors.notification;

import cd.go.plugin.base.executors.Executor;
import cd.go.plugin.base.executors.notification.models.StageStatusRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public abstract class StageStatusNotificationExecutor implements Executor {
    @Override
    public final GoPluginApiResponse execute(GoPluginApiRequest request) {
        return execute(StageStatusRequest.fromJSON(request.requestBody()));
    }

    protected abstract GoPluginApiResponse execute(StageStatusRequest stageStatusRequest);
}
