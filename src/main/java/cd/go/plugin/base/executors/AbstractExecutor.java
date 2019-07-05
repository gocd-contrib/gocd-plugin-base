package cd.go.plugin.base.executors;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public abstract class AbstractExecutor<T> implements Executor {

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        final T parsedRequest = parseRequest(request.requestBody());
        return execute(parsedRequest);
    }

    protected abstract GoPluginApiResponse execute(T request);

    protected abstract T parseRequest(String requestBody);
}
