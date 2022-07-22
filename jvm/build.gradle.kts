plugins {
    id("org.jetbrains.intellij")
}

val intellijTargetVersion: String by project

intellij {
    type.set("IC")
    version.set(intellijTargetVersion)
    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
}

tasks {
    buildSearchableOptions { enabled = false }
    runPluginVerifier { enabled = false }
    verifyPlugin { enabled = false }
    listProductsReleases { enabled = false }
}

dependencies {
    implementation(project(":common"))

    testImplementation(testFixtures(project(":")))
}
