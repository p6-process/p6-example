package org.lorislab.p6.example.rs;

import org.lorislab.p6.quarkus.process.ProcessService;
import org.lorislab.p6.quarkus.servicetask.ServiceTaskInput;
import org.lorislab.p6.quarkus.servicetask.ServiceTaskOutput;
import org.lorislab.p6.quarkus.servicetask.runtime.ProcessExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

@ApplicationScoped
@Path("/process")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessRestController {

    private static final Logger log = LoggerFactory.getLogger(ProcessRestController.class);

    @Inject
    ProcessExecutor executor;

    @Inject
    ProcessService processService;

    @POST
    @Path("/{id}/{version}")
    public Response startProcess(@PathParam("id") String id, @PathParam("version") String version, Map<String, Object> data) {
        String processInstanceId = processService.startProcess(id, version, data);
        return Response.ok(processInstanceId).build();
    }

    @GET
    public Response test() {
        ServiceTaskInput input = new ServiceTaskInput("SimpleProcess", "1.0", "service1", Collections.singletonMap("I1", "V1"));
        ServiceTaskOutput output = executor.execute(input);
        log.info("OUTPUT: {}", output.getData());
        return Response.ok().build();
    }

}
