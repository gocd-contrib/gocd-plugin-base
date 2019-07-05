package cd.go.plugin.base.executors.authorization;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.dispatcher.authorization.SupportedAuthType;
import cd.go.plugin.base.executors.Executor;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class CapabilitiesExecutor implements Executor {
    private final Capabilities capabilities;

    public CapabilitiesExecutor(SupportedAuthType supportedAuthType, boolean canSearch, boolean canAuthorize, boolean canGetUserRoles) {
        capabilities = new Capabilities(supportedAuthType, canSearch, canAuthorize, canGetUserRoles);
    }

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        return DefaultGoPluginApiResponse.success(GsonTransformer.toJson(capabilities));
    }
}
