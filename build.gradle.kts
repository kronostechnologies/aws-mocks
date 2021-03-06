import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    idea
    kotlin("jvm") version "1.3.71"
    id("io.gitlab.arturbosch.detekt") version "1.7.0-beta2"
    id("org.jmailen.kotlinter") version "2.3.2"
    id("com.github.johnrengelman.shadow") apply true
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
    val kotlinVersion = "1.3.71"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    val kotlinCoroutinesVersion = "1.3.5"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")

    val junitVersion = "5.6.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:3.15.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(kotlin("reflect"))

    val ktorVersion = "1.3.2"
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")

    val awsSdkVersion = "1.11.783"
    implementation("com.amazonaws:aws-java-sdk-acm:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-autoscaling:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-ec2:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-ecs:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-elasticloadbalancingv2:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-kms:$awsSdkVersion")
    implementation("com.amazonaws:aws-java-sdk-route53:$awsSdkVersion")

    val koinVersion = "2.1.5"
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    implementation("com.auth0:java-jwt:3.10.1")
    implementation("com.nimbusds:nimbus-jose-jwt:8.10")

    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.uchuhimo:konf:0.22.1")

    val jacksonVersion = "2.11.0"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    val testContainersVersion = "1.13.0"
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
    indentSize = 4
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
}
