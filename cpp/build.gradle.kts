plugins {
    id("org.jetbrains.intellij")
}

val intellijVersion: String by project

intellij {
    type.set("CL")
    version.set(intellijVersion)
    plugins.set(listOf("com.intellij.cidr.lang"))
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
