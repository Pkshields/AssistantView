plugins {
    id("org.jetbrains.intellij")
}

val intellijVersion: String by project

intellij {
    type.set("IC")
    version.set(intellijVersion)
    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
}

tasks {
    buildSearchableOptions { enabled = false }
    runPluginVerifier { enabled = false }
    verifyPlugin { enabled = false }
}

dependencies {
    implementation(project(":common"))

    testImplementation(testFixtures(project(":")))
}
