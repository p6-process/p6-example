FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/

COPY src/main/resources/p6/* /work/p6/
COPY target/*-runner /work/application
RUN chmod 775 /work

EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]