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
    implementation("com.github.jengelman.gradle.plugins:shadow:5.2.0")
}
