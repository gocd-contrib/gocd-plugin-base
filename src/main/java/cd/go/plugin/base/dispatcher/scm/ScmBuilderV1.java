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

package cd.go.plugin.base.dispatcher.scm;

import cd.go.plugin.base.dispatcher.VersionedExtensionBuilder;
import cd.go.plugin.base.executors.ValidationExecutor;
import cd.go.plugin.base.executors.ViewRequestExecutor;
import cd.go.plugin.base.executors.scm.*;
import cd.go.plugin.base.validation.DefaultValidator;
import cd.go.plugin.base.validation.Validator;
import lombok.NonNull;

public final class ScmBuilderV1 extends VersionedExtensionBuilder<ScmBuilderV1> {
    public static final String REQUEST_GET_SCM_CONFIGURATION = "scm-configuration";
    public static final String REQUEST_GET_SCM_VIEW = "scm-view";
    public static final String REQUEST_VALIDATE_SCM_CONFIGURATION = "validate-scm-configuration";
    public static final String REQUEST_CHECK_CONNECTION = "check-scm-connection";
    public static final String REQUEST_LATEST_REVISION = "latest-revision";
    public static final String REQUEST_LATEST_REVISION_SINCE = "latest-revisions-since";
    public static final String REQUEST_CHECKOUT = "checkout";

    public ScmBuilderV1 scmConfig(@NonNull Class<?> scmConfigClass,
                                  @NonNull String viewPath,
                                  @NonNull String scmName,
                                  Validator... validators) {
        ValidationExecutor validationExecutor = new ValidationExecutor(new DefaultValidator(scmConfigClass));
        validationExecutor.addAll(validators);
        return register(REQUEST_GET_SCM_CONFIGURATION, new ScmMetadataExecutor(scmConfigClass))
                .register(REQUEST_GET_SCM_VIEW, new ViewRequestExecutor(viewPath).add("displayValue", scmName))
                .register(REQUEST_VALIDATE_SCM_CONFIGURATION, validationExecutor);
    }

    public ScmBuilderV1 checkConnection(CheckConnectionExecutor executor) {
        return register(REQUEST_CHECK_CONNECTION, executor);
    }

    public ScmBuilderV1 latestRevision(LatestRevisionExecutor executor) {
        return register(REQUEST_LATEST_REVISION, executor);
    }

    public ScmBuilderV1 latestRevisionSince(LatestRevisionSinceExecutor executor) {
        return register(REQUEST_LATEST_REVISION_SINCE, executor);
    }

    public ScmBuilderV1 checkout(CheckoutExecutor executor) {
        return register(REQUEST_CHECKOUT, executor);
    }
}
