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

import cd.go.plugin.base.dispatcher.authorization.AuthorizationBaseBuilder;
import cd.go.plugin.base.dispatcher.elastic.ElasticBaseBuilder;
import cd.go.plugin.base.dispatcher.notification.NotificationBaseBuilder;
import cd.go.plugin.base.dispatcher.secrets.SecretsBaseBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseBuilderTest {
    @Test
    void shouldReturnBaseBuilderForSecrets() {
        Assertions.assertThat(BaseBuilder.forSecrets())
                .isNotNull()
                .isInstanceOf(SecretsBaseBuilder.class);
    }

    @Test
    void shouldReturnBaseBuilderForElastic() {
        Assertions.assertThat(BaseBuilder.forElastic())
                .isNotNull()
                .isInstanceOf(ElasticBaseBuilder.class);
    }

    @Test
    void shouldReturnBaseBuilderForNotification() {
        Assertions.assertThat(BaseBuilder.forNotification())
                .isNotNull()
                .isInstanceOf(NotificationBaseBuilder.class);
    }

    @Test
    void shouldReturnBaseBuilderForAuthorization() {
        Assertions.assertThat(BaseBuilder.forAuthorization())
                .isNotNull()
                .isInstanceOf(AuthorizationBaseBuilder.class);
    }
}
