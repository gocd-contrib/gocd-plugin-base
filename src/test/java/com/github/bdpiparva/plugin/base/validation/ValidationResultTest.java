package com.github.bdpiparva.plugin.base.validation;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationResultTest {
    @Test
    void shouldReturnTrueIfHasErrorWithKey() {
        ValidationResult result = new ValidationResult();
        result.add("test", "error message goes here");

        assertThat(result.hasKey("test")).isTrue();
    }

    @Test
    void shouldReturnErrorForGivenKey() {
        ValidationResult result = new ValidationResult();
        result.add("test", "error message goes here");

        assertThat(result.find("test"))
                .isEqualTo(Optional.of(new ValidationError("test", "error message goes here")));
    }
}