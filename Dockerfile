##############
# Build cache
##############
FROM openjdk:8-alpine AS cache
RUN mkdir -p /home/gradle/
ENV GRADLE_USER_HOME /home/gradle/.gradle

COPY settings.gradle.kts /home/gradle/
COPY gradlew /home/gradle/
COPY gradle/wrapper/ /home/gradle/gradle/wrapper/
COPY buildSrc /home/gradle/buildSrc
COPY build.gradle.kts /home/gradle/

WORKDIR /home/gradle
RUN ./gradlew --quiet -xcheck --no-daemon clean build

##############
# Build Stage
##############
FROM openjdk:8-jdk-alpine as gradle
LABEL stage=gradle-build

WORKDIR /home/gradle

COPY --from=cache /home/gradle /home/gradle
COPY src /home/gradle/src
COPY scripts /home/gradle/scripts

RUN ./gradlew --quiet -Dorg.gradle.daemon=false shadowJar -xcheck

##############
# Application Stage
##############
FROM localstack/localstack:0.11.2 as app

COPY --from=gradle /home/gradle/build/libs/aws-mocks.jar aws-mocks.jar
COPY ./config ./config
COPY ./scripts ./

RUN chown -R $USER: . /tmp/localstack
USER $USER

ENV AWSMOCKS_JAVA_OPTS="-Dlogback.configurationFile=logback-docker.xml"
ENV START_WEB=0

ENTRYPOINT ["./docker_run.sh"]
