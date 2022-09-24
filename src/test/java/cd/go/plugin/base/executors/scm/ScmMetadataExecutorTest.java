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

import cd.go.plugin.base.annotations.Property;
import cd.go.plugin.base.test_helper.annotations.JsonSource;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ScmMetadataExecutorTest {
    @ParameterizedTest
    @JsonSource(jsonFiles = "/scm/scm-configuration-metadata.json")
    void shouldExtractMedata(String expectedJSON) throws JSONException {
        GoPluginApiResponse response = new ScmMetadataExecutor(DummyScmConfig.class)
                .execute(null);

        assertThat(response.responseCode()).isEqualTo(200);
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }

    @Test
    void shouldErrorOutWhenFieldHasExposeAnnotationWithoutPropertyAnnotation() {
        final ScmMetadataExecutor executor = new ScmMetadataExecutor(ExposedWithoutProperty.class);

        assertThatCode(() -> executor.execute(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Must have @Property annotation along with @Expose.");
    }

    @Test
    void shouldErrorOutIfExposedFieldNameIsDifferentThenTheNameSpecifiedInPropertyInAbsenceOfSerializeName() {
        final ScmMetadataExecutor executor = new ScmMetadataExecutor(DifferentFieldName.class);

        assertThatCode(() -> executor.execute(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Must have @SerializeName annotation along with @Expose.");
    }

    @Test
    void shouldErrorOutIfSerializeNameAndPropertyNameIsNotSame() {
        final ScmMetadataExecutor executor = new ScmMetadataExecutor(SerializeNameIsNotSameAsPropertyName.class);

        assertThatCode(() -> executor.execute(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@Property name is not same as @SerializeName value for field 'name'.");
    }


    class SerializeNameIsNotSameAsPropertyName {
        @Expose
        @Property(name = "username", required = true)
        @SerializedName("name")
        private String name = "bob";
    }

    class DifferentFieldName {
        @Expose
        @Property(name = "username", required = true)
        private String name = "bob";
    }

    class ExposedWithoutProperty {
        @Expose
        private String name = "bob";
    }

}
