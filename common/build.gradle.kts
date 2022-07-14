plugins {
    id("org.jetbrains.intellij")
}

val intellijType: String by project
val intellijVersion: String by project
val intellijPlugins: String by project

intellij {
    type.set(intellijType)
    version.set(intellijVersion)
    plugins.set(intellijPlugins.split(","))
}

dependencies {
    testImplementation(testFixtures(project(":")))
}
