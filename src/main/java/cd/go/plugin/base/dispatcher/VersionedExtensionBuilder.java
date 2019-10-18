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

package cd.go.plugin.base.dispatcher;

import cd.go.plugin.base.executors.Executor;
import cd.go.plugin.base.executors.IconRequestExecutor;
import cd.go.plugin.base.executors.ValidationExecutor;
import cd.go.plugin.base.executors.ViewRequestExecutor;
import cd.go.plugin.base.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public abstract class VersionedExtensionBuilder<T extends VersionedExtensionBuilder> {
    private final Map<String, Executor> registry = new HashMap<>();
    protected final T self;

    protected VersionedExtensionBuilder() {
        self = (T) this;
    }

    public RequestDispatcher build() {
        return new RequestDispatcher(registry, null);
    }

    protected T icon(String requestName, String iconPath, String contentType) {
        return register(requestName, new IconRequestExecutor(iconPath, contentType));
    }

    protected Executor getExecutor(String requestName) {
        return registry.get(requestName);
    }

    protected T register(String requestName, Executor executor) {
        registry.put(requestName, executor);
        return self;
    }

    protected T view(String requestName, String viewPath) {
        return register(requestName, new ViewRequestExecutor(viewPath));
    }

    protected T validators(String requestName, Validator... validators) {
        registry.compute(requestName, (reqName, executor) -> {
            if (executor == null) {
                return new ValidationExecutor(validators);
            }

            ((ValidationExecutor) executor).addAll(validators);
            return executor;
        });

        return self;
    }
}
