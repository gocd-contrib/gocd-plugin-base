package com.github.bdpiparva.plugin.base.test_helper.system_extensions.annotations;

import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

class SystemExtensionsTest {

    @Nested
    @TestInstance(PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EnvironmentVariables {
        private Map<String, String> originalCopy = new HashMap<>();

        @BeforeAll
        void setUp() {
            originalCopy.putAll(System.getenv());
        }

        @Test
        @Order(1)
        @EnvironmentVariable(key = "Variable1", value = "This is set by test executed at order 1")
        void shouldSetSystemEnvironmentVariables() {
            assertThat(originalCopy.get("Variable1")).isNull();
            assertThat(System.getenv("Variable1")).isEqualTo("This is set by test executed at order 1");
        }

        @Test
        @Order(2)
        void shouldHaveUnsetTheSystemEnvironmentVariablesCreatedByTest1() {
            assertThat(System.getenv("Variable1")).isNull();
        }

        @Test
        @Order(3)
        @EnvironmentVariable(key = "Variable3.1", value = "This Variable3.1 is set by test executed at order 3")
        @EnvironmentVariable(key = "Variable3.2", value = "This Variable3.2 is set by test executed at order 3")
        void shouldSetMultipleVariableByRepeatingTheSameAnnotation() {
            assertThat(originalCopy.get("Variable3.1")).isNull();
            assertThat(originalCopy.get("Variable3.2")).isNull();

            assertThat(System.getenv("Variable3.1")).isEqualTo("This Variable3.1 is set by test executed at order 3");
            assertThat(System.getenv("Variable3.2")).isEqualTo("This Variable3.2 is set by test executed at order 3");
        }

        @Test
        @Order(4)
        void shouldHaveUnsetTheSystemEnvironmentVariablesCreatedByTest3() {
            assertThat(System.getenv("Variable3.1")).isNull();
            assertThat(System.getenv("Variable3.2")).isNull();
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SystemProperties {
        private Properties originalProperties;

        @BeforeAll
        void setUp() {
            originalProperties = System.getProperties();
        }

        @AfterAll
        void tearDown() {
            assertThat(System.getProperties()).isEqualTo(originalProperties);
        }

        @Test
        @Order(1)
        @SystemProperty(key = "Property1", value = "This is set by test executed at order 1")
        void shouldSetSystemProperties() {
            assertThat(originalProperties.get("Property1")).isNull();
            assertThat(System.getProperty("Property1")).isEqualTo("This is set by test executed at order 1");
        }

        @Test
        @Order(2)
        void shouldHaveUnsetTheSystemPropertiesCreatedByTest1() {
            assertThat(System.getProperty("Property1")).isNull();
        }

        @Test
        @Order(3)
        @SystemProperty(key = "Property3.1", value = "This Property3.1 is set by test executed at order 3")
        @SystemProperty(key = "Property3.2", value = "This Property3.2 is set by test executed at order 3")
        void shouldSetMultipleVariableByRepeatingTheSameAnnotation() {
            assertThat(originalProperties.get("Property3.1")).isNull();
            assertThat(originalProperties.get("Property3.2")).isNull();

            assertThat(System.getProperty("Property3.1")).isEqualTo("This Property3.1 is set by test executed at order 3");
            assertThat(System.getProperty("Property3.2")).isEqualTo("This Property3.2 is set by test executed at order 3");
        }

        @Test
        @Order(4)
        void shouldHaveUnsetTheSystemPropertiesCreatedByTest3() {
            assertThat(System.getProperty("Property3.1")).isNull();
            assertThat(System.getProperty("Property3.2")).isNull();
        }
    }
}