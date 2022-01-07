import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

group = "dev.paulshields"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.intellij") version "1.3.0"

    id("io.gitlab.arturbosch.detekt").version("1.18.1")
    id("org.jlleitschuh.gradle.ktlint").version("10.2.0")
}

intellij {
    version.set("2021.3.1")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<KtlintExtension> {
    verbose.set(true)
}

detekt {
    config = files("detekt-config.yml")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("dev.paulshields:lok:1.0")
    implementation("io.insert-koin:koin-core:3.1.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.insert-koin:koin-test:3.1.5")
    testImplementation("io.insert-koin:koin-test-junit5:3.1.5")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
