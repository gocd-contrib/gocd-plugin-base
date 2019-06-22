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

package com.github.bdpiparva.plugin.base.validation;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class ValidationError {
    @Expose
    private String key;
    @Expose
    private String message;

    ValidationError(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationError)) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, message);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "key='" + key + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
