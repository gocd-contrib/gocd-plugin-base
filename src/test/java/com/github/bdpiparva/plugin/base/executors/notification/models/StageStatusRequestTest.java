package com.github.bdpiparva.plugin.base.executors.notification.models;

import com.github.bdpiparva.plugin.base.test_helper.annotations.JsonSource;
import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;

import static com.github.bdpiparva.plugin.base.executors.notification.models.StageStatusRequest.fromJSON;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class StageStatusRequestTest {

    @ParameterizedTest
    @JsonSource(jsonFiles = "/stage-status.json")
    void shouldConvertRequestBodyToStageStatusRequest(String stageStatusJson) throws JSONException {
        final StageStatusRequest stageStatusRequest = fromJSON(stageStatusJson);

        assertEquals(stageStatusJson, stageStatusRequest.toJSON(), true);
    }
}