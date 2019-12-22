# Assistant View

![](https://github.com/pkshields/assistantview/workflows/Build/badge.svg)

Assistant View is a plugin for JetBrains IDEs intended to provide an XCode inspired "Assistant View" to any project. In JetBrains land, this Assistant View will simply be a smart split view tab. When finished, this plugin should provide the following:

* When a source file is opened, its implemented interface will open in Assistant View.
* When a source file is opened, its superclass/base class/parent class will open in Assistant View.
* When a test suite is opened, the target class under test will open in Assistant View.

## Development

### Requirements

This project requires:

* [JDK 8 or above](https://adoptopenjdk.net/)

That's it. the Gradle build will acquire all required dependencies, including the [JBR](https://confluence.jetbrains.com/display/JBR/JetBrains+Runtime). For contributing to the project, however, using [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Community Edition is grand) as your IDE is a requirement as it is the only IDE that will allow you to run and debug the plugin.

It helps that the JetBrains suite of IDEs are the best on the market today. I recommend using them for any other projects as well.

### Build

Run `./gradlew clean build` to build the project. 

### Running unit tests

Run `./gradlew test` to execute the unit tests. The tests are written using the built in [JetBrains SDK testing platform](http://www.jetbrains.org/intellij/sdk/docs/basics/testing_plugins/testing_plugins.html) which is backed by [JUnit](https://junit.org/junit5/).
