import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.40"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC15"
    id("org.jmailen.kotlinter") version "1.26.0"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

repositories {
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/ktor") }
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
}

version = "1.0-SNAPSHOT"

configure<JavaApplication> {
    mainClassName = "com.equisoft.awsmocks.MainKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")

    val junitVersion = "5.4.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(kotlin("reflect"))

    val ktorVersion = "1.2.1"
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    val awsSdkVersion = "1.11.550"
    implementation("com.amazonaws:aws-java-sdk-acm:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-ec2:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-kms:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-route53:$awsSdkVersion")

    val koinVersion = "2.0.1"
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    implementation("com.auth0:java-jwt:3.8.1")
    implementation("com.nimbusds:nimbus-jose-jwt:7.2.1")

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.uchuhimo:konf:0.13.3")

    val jacksonVersion = "2.9.9"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}

detekt {
    config = files(file(rootProject.file("detekt.yml")))
    input = files(file("src/main/kotlin"), file("src/test/kotlin"))

    failFast = true
}

tasks {
    withType<ShadowJar> {
        archiveBaseName.set("aws-mocks")
        archiveClassifier.set("")
        archiveVersion.set("")

        mergeServiceFiles()

        relocate("com.amazonaws", "shadow.amazonaws") {
            exclude("com.amazonaws.services.lambda.*")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    register("ci-compile") {
        group = "ci"
        dependsOn(testClasses)
    }

    register("ci-check") {
        group = "ci"
        dependsOn(lintKotlin)
        dependsOn(detekt)
    }

    register("ci-tests") {
        group = "ci"
        dependsOn(test)
    }
}
