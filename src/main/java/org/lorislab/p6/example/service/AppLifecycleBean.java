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
import org.lorislab.p6.example.client.gateway.DeploymentRestClient;
import org.lorislab.p6.example.client.gateway.model.DeploymentResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ApplicationScoped
public class AppLifecycleBean {

    @ConfigProperty(name = "DEPLOY_PROCESS", defaultValue = "true")
    Boolean deployProcess;

    @ConfigProperty(name = "DIR_PROCESS", defaultValue = "p6")
    String dirProcess;

    @Inject
    @RestClient
    DeploymentRestClient deploymentRestClient;

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        if (deployProcess) {
            log.info("Start deploy processes from {}", dirProcess);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dirProcess), path -> path.toString().endsWith(".json"))) {
                directoryStream.forEach(this::deployProcess);
            } catch (Exception ex) {
                throw new RuntimeException("Error deploy the process", ex);
            }
        }
    }

    private void deployProcess(Path path) {
        try {
            log.info("Deployment of process resource {} started.", path);
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Response response = deploymentRestClient.deployment(content);
            DeploymentResponse deploy = response.readEntity(DeploymentResponse.class);
            log.info("Deployment of process resource {} finished. Deployment id {}", path, deploy.getDeploymentId());
        } catch (Exception ex) {
            throw new RuntimeException("Error deploy the process", ex);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }

}
