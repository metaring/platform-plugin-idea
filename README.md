# platform-plugin-idea
MetaRing Plugin for IntelliJ Idea IDE

## Useful References

[Building plugins with Gradle](https://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system.html)

[Publishing a plugin](http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/publishing_plugin.html)

[Examples and Documentations](https://github.com/JetBrains/intellij-sdk-docs)

## Plugin installation instruction

1. To build the plugin run the task `gradlew buildPlugin` 
2. Install plugin jar/zip from folder build/distributions/platform-plugin-idea-{version}.jar/zip
3. In IDEA open Settings -> Plugins -> Select Plugin from disk and click OK.
[Please also check here](https://www.jetbrains.com/help/idea/managing-plugins.html#installing-plugins-from-disk)
4. Restart IDEA and enjoy background plugin !!!

## Start plugin instruction

Run runIde gradle task `gradlew runIde`