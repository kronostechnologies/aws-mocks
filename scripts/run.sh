#!/usr/bin/env sh

java \
    ${AWSMOCKS_JAVA_OPTS} \
    -server \
    -XX:+UnlockExperimentalVMOptions \
    -XX:InitialRAMFraction=2 \
    -XX:MinRAMFraction=2 \
    -XX:MaxRAMFraction=2 \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=100 \
    -XX:+UseStringDeduplication \
    -jar aws-mocks.jar
