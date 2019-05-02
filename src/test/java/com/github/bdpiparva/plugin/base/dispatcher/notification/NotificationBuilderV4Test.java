package com.github.bdpiparva.plugin.base.dispatcher.notification;

import com.github.bdpiparva.plugin.base.dispatcher.RequestDispatcher;
import com.github.bdpiparva.plugin.base.validation.ValidationError;
import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import com.github.bdpiparva.plugin.base.validation.Validator;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static com.github.bdpiparva.plugin.base.GsonTransformer.fromJson;
import static com.github.bdpiparva.plugin.base.dispatcher.notification.NotificationBuilderV4.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class NotificationBuilderV4Test {
    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void shouldSupportGetConfiguration() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_GET_CONFIGURATION);
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .configuration(NotificationConfig.class)
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldAddDefaultValidator() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_VALIDATE_CONFIG);
        when(request.requestBody()).thenReturn("{\"key\":\"value\"}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .configuration(NotificationConfig.class)
                .build();
        final Type type = new TypeToken<ArrayList<ValidationError>>() {
        }.getType();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        ArrayList<ValidationError> errors = fromJson(response.responseBody(), type);

        assertThat(response.responseCode()).isEqualTo(412);
        assertThat(errors.size()).isEqualTo(1);
    }

    @Test
    void shouldNotAddDefaultValidator() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_VALIDATE_CONFIG);
        when(request.requestBody()).thenReturn("{\"key\":\"value\"}");
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .configuration(NotificationConfig.class, false)
                .build();

        final Type type = new TypeToken<ArrayList<ValidationError>>() {
        }.getType();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        ArrayList<ValidationError> errors = fromJson(response.responseBody(), type);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(errors.size()).isEqualTo(0);
    }

    @Test
    void shouldSupportGetView() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_GET_CONFIG_VIEW);
        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .configView("/dummy-template.html")
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportValidateConfig() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_VALIDATE_CONFIG);
        Validator validator = mock(Validator.class);
        when(validator.validate(request)).thenReturn(new ValidationResult());

        RequestDispatcher requestDispatcher = new NotificationBuilderV4()
                .validateConfig(validator)
                .build();

        GoPluginApiResponse response = requestDispatcher.dispatch(request);
        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }
}

class NotificationConfig {

}
