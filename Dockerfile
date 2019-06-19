# Build Stage
FROM openjdk:8-jdk-alpine as gradle
LABEL stage=gradle-build

WORKDIR /home/gradle/code
COPY . .

RUN ./gradlew -Dorg.gradle.daemon=false shadowJar -xcheck

# Application Stage
FROM localstack/localstack:0.9.5 as app

COPY --from=gradle /home/gradle/code/build/libs/aws-mocks.jar aws-mocks.jar
COPY ./config ./config
COPY ./scripts ./

RUN chown -R $USER: . /tmp/localstack
USER $USER

ENV AWSMOCKS_JAVA_OPTS="-Dlogback.configurationFile=logback-docker.xml"
ENV START_WEB=0

ENTRYPOINT ["./docker_run.sh"]
