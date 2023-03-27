# Extract the layered JAR
FROM eclipse-temurin:17-jre-focal as appbuilder_stage
WORKDIR application
COPY app.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-focal
EXPOSE 8080

RUN groupadd -g 999 appuser \
    && useradd -r -u 999 -g appuser appuser \
    && mkdir -p /app \
    && chown appuser:appuser /app

USER appuser

COPY --chown=appuser:appuser startup.sh                /app/startup.sh

# Set up the application and all it's dependencies
COPY --from=appbuilder_stage --chown=appuser:appuser application/dependencies/          /app/
COPY --from=appbuilder_stage --chown=appuser:appuser application/snapshot-dependencies/ /app/
COPY --from=appbuilder_stage --chown=appuser:appuser application/spring-boot-loader/    /app/
COPY --from=appbuilder_stage --chown=appuser:appuser application/application/           /app/

WORKDIR /app

ENTRYPOINT ["/usr/bin/bash", "/app/startup.sh"]
