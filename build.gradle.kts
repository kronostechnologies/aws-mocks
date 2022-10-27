import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    idea
    kotlin("jvm") version "1.7.20"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.jmailen.kotlinter") version "3.12.0"
    id("com.github.johnrengelman.shadow") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

version = "1.0-SNAPSHOT"

configure<JavaApplication> {
    mainClass.set("com.equisoft.awsmocks.MainKt")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.1.2")
    implementation("io.ktor:ktor-server-double-receive-jvm:2.1.2")
    val kotlinVersion = "1.7.20"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    val kotlinCoroutinesVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")

    val junitVersion = "5.6.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:3.15.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(kotlin("reflect"))

    val ktorVersion = "2.1.2"
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    val awsSdkVersion = "1.12.238"
    implementation("com.amazonaws:aws-java-sdk-acm:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-autoscaling:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-ec2:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-ecs:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-elasticloadbalancingv2:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-kms:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-route53:$awsSdkVersion")

    val koinVersion = "3.2.2"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-core-jvm:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    implementation("com.auth0:java-jwt:3.10.1")
    implementation("com.nimbusds:nimbus-jose-jwt:8.10")

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.uchuhimo:konf:0.22.1")

    val jacksonVersion = "2.13.4"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:$jacksonVersion")
//    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.1")

    val testContainersVersion = "1.17.5"
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
}

configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    config = files(file(rootProject.file("detekt.yml")))
    input = files(file("src/main/kotlin"), file("src/test/kotlin"))

    failFast = true
}

kotlinter {
    disabledRules = arrayOf("import-ordering", "no-wildcard-imports")
}

tasks {
    check {
        dependsOn("e2e")
    }

    withType<ShadowJar> {
        archiveBaseName.set("aws-mocks")
        archiveClassifier.set("")
        archiveVersion.set("")

        mergeServiceFiles()
        transform(RewriteServiceHandlers::class.java)

        relocate("com.amazonaws", "shadow.amazonaws")
    }

    withType<Test> {
        useJUnitPlatform {
            excludeTags("e2e")
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    register<Test>("e2e") {
        group = "verification"

        mustRunAfter(detekt)
        mustRunAfter(lintKotlin)
        mustRunAfter("test")

        useJUnitPlatform {
            includeTags("e2e")
        }
    }

    register("ci-classes") {
        group = "ci"
        dependsOn(testClasses)
    }

    register("ci-check") {
        group = "ci"

        dependsOn("ci-classes")

        dependsOn(detekt)
        dependsOn(lintKotlin)
    }

    register("ci-unit-tests") {
        group = "ci"

        dependsOn("ci-classes")

        dependsOn(test)
    }

    register("ci-e2e") {
        group = "ci"

        dependsOn("ci-classes")

        dependsOn("e2e")
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = "7.5.1"
    }
}
