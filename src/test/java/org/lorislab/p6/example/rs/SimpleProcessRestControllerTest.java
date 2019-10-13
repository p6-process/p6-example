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

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.lorislab.p6.example.test.AbstractTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Testcontainers
@QuarkusTest
public class SimpleProcessRestControllerTest extends AbstractTest {

    public static String TEST_URL = "SimpleProcess/1.0.0/";

    @Test
    public void simpleProcessTest() {

        SimpleProcessRestController.ServiceParameter service1 = new SimpleProcessRestController.ServiceParameter();
        service1.setCount(100L);
        service1.setKey("asd");

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service1)
                .post(TEST_URL + "service1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("count",is(100)).body("key", is("asd/service1"));

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service1)
                .post(TEST_URL + "service2")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("count",is(110)).body("key", is("asd/service2"));

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service1)
                .post(TEST_URL + "service3")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("count",is(110)).body("key", is("asd/service3"));

        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service1)
                .post(TEST_URL + "service4")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("count",is(110)).body("key", is("asd/service4"));
    }
}
