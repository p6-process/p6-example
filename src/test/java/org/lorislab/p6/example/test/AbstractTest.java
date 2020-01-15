/*
 * Copyright 2019 lorislab.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lorislab.p6.example.test;

import io.restassured.RestAssured;
import org.lorislab.jel.testcontainers.docker.DockerComposeService;
import org.lorislab.jel.testcontainers.docker.DockerTestEnvironment;
import org.mockserver.client.MockServerClient;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import static com.google.common.net.MediaType.JSON_UTF_8;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public abstract class AbstractTest {

    public static DockerTestEnvironment ENVIRONMENT = new DockerTestEnvironment();

    /**
     * The mock server client
     */
    private static String mockServerHost;

    private static int mockServerPort;

    /**
     * Starts the containers before the tests
     */
    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        ENVIRONMENT.start();

        DockerComposeService mockService = ENVIRONMENT.getService("p6-example-mockserver");
        mockServerHost = mockService.getHost();
        mockServerPort = mockService.getPort(1080);

        createMockServerClient()
                .when(request("/v1/deployment")
                        .withMethod(HttpMethod.POST))
                .respond(r -> response()
                        .withStatusCode(Response.Status.ACCEPTED.getStatusCode())
                        .withBody("{\"deploymentId\":\"1234\"}", JSON_UTF_8)
                );

        DockerComposeService service = ENVIRONMENT.getService("p6-example");
        if (service != null) {
            RestAssured.port = service.getPort(8080);
        }
    }

    protected static MockServerClient createMockServerClient() {
        return new MockServerClient(mockServerHost, mockServerPort);
    }

}
