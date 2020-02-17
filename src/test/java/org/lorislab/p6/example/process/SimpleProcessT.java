package org.lorislab.p6.example.process;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.lorislab.p6.example.test.AbstractTest;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class SimpleProcessT extends AbstractTest {

    @Test
    public void testSimpleProcess() {
        String processId = "SimpleProcess";
        String processVersion = "1.0.0";

        Map<String, Object> data = new HashMap<>();
        data.put("input", 10);
        data.put("test", "TestSimpleProcess");


        ValidatableResponse response = given()
                    .pathParam("id", processId)
                    .pathParam("version", processVersion)
                    .body(data)
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .post("/process/{id}/{version}")
                .prettyPeek()
                .then();
        response.statusCode(Response.Status.OK.getStatusCode());
        String processInstanceId = response.extract().asString();

        // wait for process instance finished
        waitProcessFinished(processId, processVersion, processInstanceId);

        given(SPEC_PROCESS)
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("guid", processInstanceId)
                .get("/v1/instance/{guid}/parameters")
                .prettyPeek()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("test", equalTo("TestSimpleProcess"))
                .body("input", equalTo(13))
                .body("service1", equalTo(true))
                .body("service3", equalTo(true))
                .body("service4", equalTo(true))
                .body("service5", equalTo(true));
    }

    private void waitProcessFinished(String processId, String processVersion, String processInstanceId) {

        log.info("Wait for the process {}/{} to finished execution guid {} ", processId, processVersion, processInstanceId);
        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> given(SPEC_PROCESS)
                        .when()
                        .contentType(MediaType.APPLICATION_JSON)
                        .pathParam("guid", processInstanceId)
                        .get("/v1/instance/{guid}")
                        .prettyPeek()
                        .then()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .body("status", equalTo("FINISHED"))
                );
    }
}
