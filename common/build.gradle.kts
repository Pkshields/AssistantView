plugins {
    id("org.jetbrains.intellij")
}

val intellijType: String by project
val intellijTargetVersion: String by project
val intellijPlugins: String by project

intellij {
    type.set(intellijType)
    version.set(intellijTargetVersion)
    plugins.set(intellijPlugins.split(","))
}

tasks {
    buildSearchableOptions { enabled = false }
    runPluginVerifier { enabled = false }
    verifyPlugin { enabled = false }
    listProductsReleases { enabled = false }
}

dependencies {
    testImplementation(testFixtures(project(":")))
}
