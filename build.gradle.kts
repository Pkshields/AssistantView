import io.gitlab.arturbosch.detekt.DetektPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

group = "dev.paulshields.assistantview"
version = "1.1-SNAPSHOT"

val intellijType: String by project
val intellijTargetVersion: String by project
val intellijPlugins: String by project
val intellijLocalPath: String? by project

val intellijMinimumBuildNumber: String by project

val detektConfigFile = files("detekt-config.yml")

plugins {
    id("org.gradle.java-test-fixtures")

    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.intellij") version "1.9.0"

    id("net.saliman.properties") version "1.5.2"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

intellij {
    type.set(intellijType)
    plugins.set(intellijPlugins.split(","))

    if (intellijLocalPath.isNullOrEmpty()) {
        version.set(intellijTargetVersion)
    } else {
        localPath.set(intellijLocalPath)
    }
}

// Keys are obviously not in the repo.
tasks.signPlugin {
    certificateChain.set(System.getenv("ASSISTANT_VIEW_CERT_CHAIN") ?: File("signing-keys/cert_chain.crt").readText())
    privateKey.set(System.getenv("ASSISTANT_VIEW_SIGNING_KEY") ?: File("signing-keys/key_private.pem").readText())
    password.set(System.getenv("ASSISTANT_VIEW_SIGNING_KEY_PASSWORD"))
}

tasks.patchPluginXml {
    sinceBuild.set(intellijMinimumBuildNumber)
}

// Disabled until AssistantView has a need for custom settings to speed up the build process
tasks.buildSearchableOptions { enabled = false }

dependencies {
    implementation(project(":common"))
    implementation(project(":jvm"))
    implementation(project(":cpp"))
}

allprojects {
    apply<KotlinPluginWrapper>()
    apply<DetektPlugin>()
    apply<KtlintPlugin>()
    apply<JavaTestFixturesPlugin>()

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
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
        implementation("dev.paulshields:lok:1.1")
        implementation("io.insert-koin:koin-core:3.2.2")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
        testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
        testImplementation("org.awaitility:awaitility-kotlin:4.1.1")

        // Test fixtures dependencies marked as API to "leak" the dependencies into the test compile classpath
        testFixturesApi("io.mockk:mockk:1.13.2")
        testFixturesApi("io.insert-koin:koin-test:3.2.2")
        testFixturesApi("io.insert-koin:koin-test-junit5:3.2.2")

        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }
}
