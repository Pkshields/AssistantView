import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

group = "dev.paulshields.assistantview"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.intellij") version "1.5.3"

    id("io.gitlab.arturbosch.detekt").version("1.20.0")
    id("org.jlleitschuh.gradle.ktlint").version("10.3.0")
}

intellij {
    version.set("2021.2.1")
    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
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
    implementation("io.insert-koin:koin-core:3.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.insert-koin:koin-test:3.2.0")
    testImplementation("io.insert-koin:koin-test-junit5:3.2.0")
    testImplementation("org.awaitility:awaitility-kotlin:4.1.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
