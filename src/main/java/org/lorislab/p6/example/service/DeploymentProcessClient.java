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

import lombok.Data;
import lombok.ToString;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.lorislab.quarkus.jel.log.interceptor.LoggerExclude;
import org.lorislab.quarkus.jel.log.interceptor.LoggerService;
import org.lorislab.quarkus.jel.log.interceptor.RestClientLogInterceptor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterProvider(RestClientLogInterceptor.class)
@Path("v1/gateway/deployment/task/deploy")
@RegisterRestClient
@LoggerService
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DeploymentProcessClient {

    @POST
    Response deploy(@LoggerExclude String process);

    @Data
    @ToString
    public static class DeloypmentResponse {

        private String deploymentId;
    }
}
