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

package cd.go.plugin.base.executors.secrets;

import cd.go.plugin.base.executors.Executor;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public abstract class LookupExecutor<T> implements Executor {

    @Override
    public final GoPluginApiResponse execute(GoPluginApiRequest request) {
        final T parsedRequest = parseRequest(request.requestBody());
        return execute(parsedRequest);
    }

    protected abstract GoPluginApiResponse execute(T request);

    protected abstract T parseRequest(String body);
}
