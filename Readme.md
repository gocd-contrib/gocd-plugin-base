# GoCD plugin helper utility

This is a helper utility to write GoCD plugin with minimal code.

## Secret v1

```java
import com.github.bdpiparva.plugin.base.dispatcher.RequestDispatcher;
import com.github.bdpiparva.plugin.base.dispatcher.RequestDispatcherBuilder;
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
    requestDispatcher = RequestDispatcherBuilder
      .forSecret()
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