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

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;

import static com.google.common.net.MediaType.JSON_UTF_8;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AbstractTest {

    /**
     * The mock server container.
     */
    private static final MockServerContainer MOCK_SERVER_CONTAINER;

    /**
     * The mock server client
     */
    private static MockServerClient MOCK_SERVER_CLIENT;

    //Configure the containers for the test
    static {

        Consumer<CreateContainerCmd> cmd2 = e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(1080), new ExposedPort(1080)));
        MOCK_SERVER_CONTAINER = new MockServerContainer()
//                .withLogConsumer(ContainerLogger.create("mockserver"))
                .withCreateContainerCmdModifier(cmd2);
    }

    /**
     * Starts the containers before the tests
     */
    @BeforeAll
    public static void init() {
        // star the mock server container
        MOCK_SERVER_CONTAINER.start();
        // create mock server client to configure the responses
        MOCK_SERVER_CLIENT = new MockServerClient(MOCK_SERVER_CONTAINER.getContainerIpAddress(), MOCK_SERVER_CONTAINER.getServerPort());

        MOCK_SERVER_CLIENT.when(request("/v1/gateway/deployment/task/deploy").withMethod(HttpMethod.POST))
                .respond(r -> response()
                        .withStatusCode(Response.Status.ACCEPTED.getStatusCode())
                        .withBody("{\"deploymentId\":\"1234\"}", JSON_UTF_8)
                );
    }

    protected MockServerClient getMockServerClient() {
        return MOCK_SERVER_CLIENT;
    }
}
