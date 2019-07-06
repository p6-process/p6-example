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

package org.lorislab.p6.example.rs;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("SimpleProcess/1.0.0")
public class SimpleProcessRestController {

    @POST
    @Path("service1")
    public Response service1(ServicePameter data) {
        log.info("Execute service1 method of the simple process version 1. Input parameter: {}", data);
        data.setCount(100L);
        data.setKey(data.getKey() + "/service1");
        return Response.ok(data).build();
    }

    @POST
    @Path("service2")
    public Response service2(ServicePameter data) {
        log.info("Execute service2 method of the simple process version 1. Input parameter: {}", data);
        data.setCount(data.getCount() + 10L);
        data.setKey(data.getKey() + "/service2");
        return Response.ok(data).build();
    }

    @POST
    @Path("service3")
    public Response service3(ServicePameter data) {
        log.info("Execute service3 method of the simple process version 1. Input parameter: {}", data);
        data.setCount(data.getCount() + 10L);
        data.setKey(data.getKey() + "/service3");
        return Response.ok(data).build();
    }

    @POST
    @Path("service4")
    public Response service4(ServicePameter data) {
        log.info("Execute service4 method of the simple process version 1. Input parameter: {}", data);
        data.setCount(data.getCount() + 10L);
        data.setKey(data.getKey() + "/service4");
        return Response.ok(data).build();
    }

    @Data
    @ToString
    public static class ServicePameter {

        private String key;

        private Long count;
    }
}
