# matrix-minecraft
Minecraft plugin and optional proxy server for bridging to [Matrix](https://matrix.org/).

You will need a Matrix Homeserver that you can register an appservice with. This requires access to the server's configuration and likely means that you'll need to run your own Matrix server.

The plugin gets tested with the following Minecraft servers:
* [CraftBukkit](https://getbukkit.org/)
* [Glowstone](https://glowstone.net/)
* [PaperMC](https://papermc.io/)
* [SpigotMC](https://www.spigotmc.org/)

## Build
This plugin gets build with the [Gradle Build Tool](https://gradle.org/).

Download the source code and open the terminal in the source code folder.

```
gradle -q jar
```
This command runs the `jar` task and creates a new file called `./build/libs/matrix-minecraft-1.0-SNAPSHOT.jar`. If the build passes, this jar file is the plugin to install on your Minecraft Server.

## Installation
Copy the `matrix-minecraft-*.jar` file into `SERVERFOLDER/plugins`.

To configure the plugin, edit `SERVERFOLDER/plugins/MatrixMinecraft/config.yml`.
If the file does not exist, copy the contents from `./src/main/resources/config.yml` or restart your server to get the default config.

TODO: Describe how to register the appservice with a Matrix homeserver.
