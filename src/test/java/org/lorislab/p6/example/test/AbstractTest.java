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
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lorislab.jel.testcontainers.InjectLoggerExtension;
import org.lorislab.jel.testcontainers.docker.DockerComposeService;
import org.lorislab.jel.testcontainers.docker.DockerTestEnvironment;
import org.mockserver.client.MockServerClient;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import static com.google.common.net.MediaType.JSON_UTF_8;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(InjectLoggerExtension.class)
public abstract class AbstractTest {

    public static DockerTestEnvironment ENVIRONMENT = new DockerTestEnvironment();

    @Inject
    protected Logger log;

    protected static RequestSpecification SPEC_PROCESS;

    /**
     * Starts the containers before the tests
     */
    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        ENVIRONMENT.start();

        DockerComposeService executor = ENVIRONMENT.getService("p6-process");

        SPEC_PROCESS = new RequestSpecBuilder()
                .setPort(executor.getPort(8080))
                .setBaseUri("http://" + executor.getHost())
                .build();

        DockerComposeService service = ENVIRONMENT.getService("p6-example");
        if (service != null) {
            RestAssured.port = service.getPort(8080);
        }
    }

}
