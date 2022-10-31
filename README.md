# Assistant View

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/20283-assistant-view)](https://plugins.jetbrains.com/plugin/20283-assistant-view) [![CI Status Badge](https://github.com/Pkshields/AssistantView/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/Pkshields/AssistantView/actions)

Assistant View is a second code editor that aims to show you files that are contextually helpful, based on the file you are currently working on.

## Supported Platforms

Assistant View is an IDE plugin that supports...
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (CE and Ultimate)
- [Android Studio](https://developer.android.com/studio)
- [CLion](https://www.jetbrains.com/clion/)

...and supports the following languages...

- Kotlin
- Java
- C
- C++

## Installation

COMING SOON

## Usage

Once installed, the Assistant View window will be available in the tool window list on the right hand side of your IDE, or under `View >> Tool Windows >> Assistant View`.

Once Assistant View is open, that's it! Navigate around your codebase and Assistant View will do its best trying to open files that it thinks will be helpful to you.  

## Development

Working in the Assistant View codebase requires:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (CE and Ultimate)
- [JDK 11](https://adoptium.net/temurin/releases?version=11)

It is a Gradle based project, utilising the [Gradle IntelliJ Plugin](https://github.com/JetBrains/gradle-intellij-plugin), so all [standard Gradle commands](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks) are used here, plus a [few specialised ones](https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#tasks) provided by the IntelliJ plugin.

### Project Structure

Due to Assistant View being a cross-IDE plugin that still requires language specific implementations, the project has a slightly peculiar project structure. This is designed to get around the IDE specific limitations of the Gradle IntelliJ Plugin. Logic in this project is structured like so:

- ***Root Project***: The first port of call for any features in Assistant View. Contains all cross-platform logic that does not need to be accessed by language specific subprojects.
  - ***jvm***: JVM language specific logic.
  - ***cpp***: C / C++ language specific logic.
  - ***common***: All cross-platform logic that *does* need to be accessed by language specific subprojects.

### Debugging The Plugin

Within the IntelliJ project, you will notice separate run configurations to debug the plugin in each of the supported IDEs. This can also be run via the command line:

- Run in IntelliJ CE - `./gradlew runIde`
- Run in Android Studio - `./gradlew runIde -Pide=android-studio`
- Run in CLion (requires CLion license) - `./gradlew runIde -Pide=clion`
- Run in CLion EAP - `./gradlew runIde -Pide=clion-eap`
