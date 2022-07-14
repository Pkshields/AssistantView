plugins {
    id("org.jetbrains.intellij")
}

val intellijVersion: String by project

intellij {
    type.set("IC")
    version.set(intellijVersion)
    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
}

dependencies {
    implementation(project(":common"))

    testImplementation(testFixtures(project(":")))
}
