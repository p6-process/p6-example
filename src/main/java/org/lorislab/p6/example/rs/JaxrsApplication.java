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

import io.smallrye.reactive.messaging.amqp.AmqpMessage;
import org.lorislab.quarkus.jel.log.interceptor.LoggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class JaxrsApplication extends Application {

    @LoggerParam(classes = {AmqpMessage.class})
    public static String logMessage(Object message) {
        AmqpMessage<?> a = (AmqpMessage<?>) message;
        return "Message[" + a.getMessageId() + "," + a.getApplicationProperties() + "," + a.getCorrelationId() + "," + a.getDeliveryCount() + "]";
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
