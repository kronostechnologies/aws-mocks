plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
}
