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

package cd.go.plugin.base.validation;

import java.util.ArrayList;
import java.util.Optional;

public class ValidationResult extends ArrayList<ValidationError> {
    public void merge(ValidationResult validationResult) {
        this.addAll(validationResult);
    }

    public boolean add(String key, String message) {
        return add(new ValidationError(key, message));
    }

    public boolean hasKey(String key) {
        return find(key).isPresent();
    }

    public Optional<ValidationError> find(String key) {
        return this.stream()
                .filter(error -> key.equals(error.getKey()))
                .findFirst();
    }
}
