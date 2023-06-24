/*
 * Copyright 2019 Thoughtworks, Inc.
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

package cd.go.plugin.base.executors.scm;

import cd.go.plugin.base.ConfigurationParser;
import cd.go.plugin.base.GsonTransformer;
import cd.go.plugin.base.executors.Executor;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.lang.reflect.ParameterizedType;

import static cd.go.plugin.base.GsonTransformer.fromJson;
import static cd.go.plugin.base.GsonTransformer.toJson;
import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.success;

public abstract class ScmExecutor<T, R> implements Executor {
    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        String filteredConfig = toJson(ConfigurationParser.asMap(request.requestBody(), true));
        R response = execute(fromJson(filteredConfig, getGenericClassType()));
        return success(GsonTransformer.toJson(response));
    }

    protected abstract R execute(T t);

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClassType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
