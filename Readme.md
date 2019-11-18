# GoCD plugin helper utility

This is a helper utility to write GoCD plugin with minimal code.

## Setup developer environment

Project uses the lombok, please make sure to enable annotation processor in intellij.

# Table of Contents

1. [Notification Plugin](/examples/src/main/java/notification/NotificationPlugin.java)
2. [Secrets Plugin](/examples/src/main/java/notification/NotificationPlugin.java)

## Secret v1

```java
import RequestDispatcher;
import cd.go.plugin.base.dispatcher.RequestDispatcherBuilder;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static java.util.Collections.singletonList;

@Extension
public class ExamplePlugin implements GoPlugin {
  private RequestDispatcher requestDispatcher;
  @Override
  public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
    requestDispatcher = BaseBuilder
      .forSecret()
      .v1()
      .icon("/plugin-icon.png", "image/png")  // Icon file path and content type
      .configMetadata(SecretConfig.class)     // Secret config class
      .configView("/secrets.template.html")   // Angular html template for the secret config view
      .validateSecretConfig(...)              // You can add additional validators to validate your secret configs
      .lookup(new SecretConfigLookupExecutor()) // lookup executor
      .build();
  }

  @Override
  public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
    return requestDispatcher.dispatch(request); // use previously built request dispatcher to handle server requests 
  }

  @Override
  public GoPluginIdentifier pluginIdentifier() {
    return new GoPluginIdentifier("secrets", singletonList("1.0"));
  }
}

class SecretConfig {
   ...
}

class SecretConfigLookupExecutor {
    ...
}
```

## Elastic agent v5

You can also build elastic agent v5 request dispatcher as mentioned below -

```java
RequestDispatcher requestDispatcher = BaseBuilder
    .forElastic()
    .v5()
    .icon()
    .capabilities()
    .clusterProfileMetadata()
    .clusterProfileView()
    .validateClusterProfile()
    .clusterProfileChanged()
    .clusterStatusReport()
    .elasticProfileMetadata()
    .elasticProfileView()
    .validateElasticProfile()
    .agentStatusReport()
    .pluginStatusReport()
    .migrateConfiguration()
    .build()
```

### Authorization plugin example
```java
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

import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.dispatcher.BaseBuilder;
import cd.go.plugin.base.dispatcher.RequestDispatcher;
import cd.go.plugin.base.executors.AbstractExecutor;
import cd.go.plugin.base.validation.ValidationResult;
import cd.go.plugin.base.validation.Validator;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

import static cd.go.plugin.base.dispatcher.authorization.SupportedAuthType.Password;
import static java.util.Collections.singletonList;

@Extension
public class ExamplePlugin implements GoPlugin {
  private RequestDispatcher requestDispatcher;

  @Override
  public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
    requestDispatcher = BaseBuilder.forAuthorization().v2()
      .icon("/icon.png", "image/png") // Icon for the plugin, must be present in resources directory
      .capabilities(Password, true, false, false) // Define your plugins capabilities
      .authConfigMetadata(AuthConfig.class) // Class representing auth config
      .authConfigView("/auth-config.html")  // auth config view name, must be present in resources directory
      .validateAuthConfig(new DummyValidator()) // Additional validators
      .verifyConnection(new DummyExecutor()) // Given executor will get called on verify connection request
      .authenticateUser(new DummyExecutor()) // Given executor will get called on authenticate user request
      .searchUser(new DummyExecutor()) // Given executor will get called on search user request, In order to receive the callback user must set canSearch capability to true
      .isValidUser(new DummyExecutor()) // Given executor will get called on is valid user request
      .getUserRoles(new DummyExecutor()) // Given executor will get called on is get user roles request
      .build();
  }

  @Override
  public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
    return requestDispatcher.dispatch(request);
  }

  @Override
  public GoPluginIdentifier pluginIdentifier() {
    return new GoPluginIdentifier("authorization", singletonList("2.0"));
  }

  class DummyValidator implements Validator {
    @Override
    public ValidationResult validate(Map<String, String> requestBody) {
      return null;
    }
  }

  class DummyExecutor extends AbstractExecutor<AuthConfig> {
    @Override
    protected GoPluginApiResponse execute(AuthConfig request) {
      return null;
    }

    @Override
    protected AuthConfig parseRequest(String requestBody) {
      return null;
    }
  }

  class AuthConfig {
    @SerializedName("token")
    @Property(name = "token", required = true, secure = true)
    private String token;
  }
}

```

## License

```plain
Copyright 2019 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
