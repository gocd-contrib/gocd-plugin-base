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

package secrets;

import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.dispatcher.BaseBuilder;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.secrets.LookupExecutor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.List;

import static java.util.Collections.singletonList;

@Extension
public class SecretsPlugin implements GoPlugin {
    private RequestDispatcher requestDispatcher;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        requestDispatcher = BaseBuilder.forSecrets()
                .v1()
                .icon("/plugin-icon.png", "image/png")  // Icon file path and content type
                .configMetadata(ExampleSecretConfig.class)     // Secret config class
                .configView("/secrets.template.html")   // Angular html template for the secret config view
                .validateSecretConfig()              // You can add additional validators to validate your secret configs
                .lookup(new SecretConfigLookupExecutor()) // lookup executor
                .build();
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            return requestDispatcher.dispatch(request); // use previously built request dispatcher to handle server requests
        } catch (Exception e) {
            //Handle it
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("secrets", singletonList("1.0"));
    }
}

class ExampleSecretConfig {
    @Expose
    @SerializedName("VaultURL")
    @Property(name = "VaultURL", required = true)
    private String vaultURL;

    @Expose
    @SerializedName("Token")
    @Property(name = "Token", required = true, secure = true)
    private String token;
}

class SecretsLookupRequest {
    @Expose
    @SerializedName("configuration")
    private ExampleSecretConfig secretConfig;

    @Expose
    @SerializedName("keys")
    private List<String> keys;

}

class SecretConfigLookupExecutor extends LookupExecutor<SecretsLookupRequest> {

    @Override
    protected GoPluginApiResponse execute(SecretsLookupRequest request) {
        return DefaultGoPluginApiResponse.success("Return the response as mentioned in https://plugin-api.gocd.org/19.10.0/secrets/#lookup-secrets");
    }

    @Override
    protected SecretsLookupRequest parseRequest(String body) {
        return GsonTransformer.fromJson(body, SecretsLookupRequest.class);
    }
}
