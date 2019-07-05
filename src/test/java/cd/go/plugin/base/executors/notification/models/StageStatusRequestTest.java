package cd.go.plugin.base.executors.notification.models;

import cd.go.plugin.base.test_helper.annotations.JsonSource;
import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class StageStatusRequestTest {

    @ParameterizedTest
    @JsonSource(jsonFiles = "/stage-status.json")
    void shouldConvertRequestBodyToStageStatusRequest(String stageStatusJson) throws JSONException {
        final StageStatusRequest stageStatusRequest = StageStatusRequest.fromJSON(stageStatusJson);

        assertEquals(stageStatusJson, stageStatusRequest.toJSON(), true);
    }
}