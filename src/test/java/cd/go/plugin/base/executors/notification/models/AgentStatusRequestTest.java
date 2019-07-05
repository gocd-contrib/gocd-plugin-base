package cd.go.plugin.base.executors.notification.models;

import cd.go.plugin.base.test_helper.annotations.JsonSource;
import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class AgentStatusRequestTest {

    @ParameterizedTest
    @JsonSource(jsonFiles = "/agent-status.json")
    void shouldCreateAgentStatusRequestFromRequestBody(String agentStatusJson) throws JSONException {
        final AgentStatusRequest agentStatusRequest = AgentStatusRequest.fromJSON(agentStatusJson);

        assertEquals(agentStatusJson, agentStatusRequest.toJSON(), true);
    }
}