FROM fabric8/java-alpine-openjdk8-jre:1.6.3

ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV AB_ENABLED=jmx_exporter

COPY src/main/resources/p6/* /deployments/p6/

COPY target/lib/* /deployments/lib/
COPY target/*-runner.jar /deployments/app.jar

EXPOSE 8080

ENTRYPOINT [ "/deployments/run-java.sh" ]