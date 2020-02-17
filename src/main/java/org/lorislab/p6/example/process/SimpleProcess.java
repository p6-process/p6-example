package org.lorislab.p6.example.process;

import org.lorislab.p6.quarkus.servicetask.Process;
import org.lorislab.p6.quarkus.servicetask.ServiceTask;
import org.lorislab.p6.quarkus.servicetask.ServiceTaskInput;
import org.lorislab.p6.quarkus.servicetask.ServiceTaskOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Process(id = "SimpleProcess", version = "1.0.0")
public class SimpleProcess {

    private static Logger log = LoggerFactory.getLogger(SimpleProcess.class);

    @ServiceTask(name = "service1")
    public ServiceTaskOutput service1(ServiceTaskInput data) {
        log.info("Service1 parameters {}", data.getParameters());
        long input = data.getLong("input") + 1;
        return ServiceTaskOutput.data("service1", true).add("input", input);
    }

    @ServiceTask(name = "service3")
    public ServiceTaskOutput service3(ServiceTaskInput data) {
        log.info("Service3 parameters {}", data.getParameters());
        long input = data.getLong("input") + 1;
        return ServiceTaskOutput.data("service3", true).add("input", input);
    }

    @ServiceTask(name = "service4")
    public ServiceTaskOutput service4(ServiceTaskInput data) {
        log.info("Service4 parameters {}", data.getParameters());
        long input = data.getLong("input") + 1;
        return ServiceTaskOutput.data("service4", true).add("input", input);
    }

    @ServiceTask(name = "service5")
    public ServiceTaskOutput service5(ServiceTaskInput data) {
        log.info("Service5 parameters {}", data.getParameters());
        long input = data.getLong("input") + 1;
        return ServiceTaskOutput.data("service5", true).add("input", input);
    }
}
