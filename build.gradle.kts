import io.gitlab.arturbosch.detekt.DetektPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

group = "dev.paulshields.assistantview"
version = "1.0-SNAPSHOT"

val intellijType: String by project
val intellijVersion: String by project
val intellijPlugins: String by project
val detektConfigFile = files("detekt-config.yml")

plugins {
    id("org.gradle.java-test-fixtures")

    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.intellij") version "1.7.0"

    id("net.saliman.properties") version "1.5.2"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

intellij {
    type.set(intellijType)
    version.set(intellijVersion)
    plugins.set(intellijPlugins.split(","))
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":jvm"))
}

allprojects {
    apply<KotlinPluginWrapper>()
    apply<DetektPlugin>()
    apply<KtlintPlugin>()
    apply<JavaTestFixturesPlugin>()

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    configure<KtlintExtension> {
        verbose.set(true)
    }

    detekt {
        config = detektConfigFile
    }

    tasks.test {
        useJUnitPlatform()
    }

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        implementation(kotlin("stdlib"))

        implementation("dev.paulshields:lok:1.0")
        implementation("io.insert-koin:koin-core:3.2.0")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
        testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
        testImplementation("org.awaitility:awaitility-kotlin:4.1.1")

        // Test fixtures dependencies marked as API to "leak" the dependencies into the test compile classpath
        testFixturesApi("io.mockk:mockk:1.12.4")
        testFixturesApi("io.insert-koin:koin-test:3.2.0")
        testFixturesApi("io.insert-koin:koin-test-junit5:3.2.0")

        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }
}
