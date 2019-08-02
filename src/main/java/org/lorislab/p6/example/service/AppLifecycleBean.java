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

package org.lorislab.p6.example.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@ApplicationScoped
public class AppLifecycleBean {

    @ConfigProperty(name = "DEPLOY_PROCESS", defaultValue = "true")
    Boolean deployProcess;

    @Inject
    @RestClient
    DeploymentProcessClient deploymentProcessClient;

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        if (deployProcess) {
            log.info("Start deploy processes");
            deployProcess("/p6/DummyProcess.json");
            deployProcess("/p6/SimpleProcess.json");
        }
    }

    private void deployProcess(String resource) {
        log.info("Deployment of process resource {} started.", resource);
        String content = loadResource(resource);
        Response response = deploymentProcessClient.deploy(content);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            DeploymentProcessClient.DeloypmentResponse deploy = response.readEntity(DeploymentProcessClient.DeloypmentResponse.class);
            log.info("Deployment of process resource {} finished. Deployment id {}", resource, deploy.getDeploymentId());
        } else {
            log.error("Deployment of process resource {} failed. Response code {}", resource, response.getStatus());
            throw new RuntimeException("Error deploy the process");
        }
    }

    private static String loadResource(String name) {
        try (InputStream in = AppLifecycleBean.class.getResourceAsStream(name);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            return out.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }

}
