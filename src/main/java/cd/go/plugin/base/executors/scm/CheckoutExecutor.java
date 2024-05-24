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

import cd.go.plugin.base.executors.Executor;
import cd.go.plugin.base.executors.scm.model.StatusResponse;
import cd.go.plugin.base.executors.scm.request.CheckoutRequest;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static cd.go.plugin.base.ConfigurationParser.asMap;
import static cd.go.plugin.base.GsonTransformer.fromJson;
import static cd.go.plugin.base.GsonTransformer.toJson;
import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.success;

public abstract class CheckoutExecutor<T> implements Executor {

    @Override
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        return success(toJson(execute(toCheckoutRequest(request))));
    }

    protected abstract StatusResponse execute(CheckoutRequest<T> request);

    private CheckoutRequest<T> toCheckoutRequest(GoPluginApiRequest request) {
        Type type = TypeToken.getParameterized(CheckoutRequest.class, getGenericClassType()).getType();

        CheckoutRequest<T> req = fromJson(request.requestBody(), type);
        req.setScmConfiguration(parseScmConfiguration(request.requestBody()));
        return req;
    }


    @SuppressWarnings("unchecked")
    private Class<T> getGenericClassType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private T parseScmConfiguration(String requestBody) {
        return fromJson(toJson(asMap(requestBody, true)), getGenericClassType());
    }
}
