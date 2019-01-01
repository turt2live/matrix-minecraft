package io.t2l.mc.matrix.test.mcserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerManager {

    private ServerManager() {
    }

    public static ServerInstance createServer(Server server, MCVersion version) throws IOException {
        Path dataPath = Files.createTempDirectory("mc_tests");
        File serverJar = new File(ServerManager.tryGetFile("servers/" + server.serverName + "-" + version.version + ".jar"));
        File pluginFile = new File(ServerManager.tryGetFile("plugin/MatrixMinecraft.jar"));
        File pluginConfig = new File(ServerManager.tryGetFile("plugin/MatrixMinecraft.yml"));
        File[] serverConfifs = new File[]{
                new File(ServerManager.tryGetFile("servers/server.properties")),
        };

        return new ServerInstance(serverJar, serverConfifs, dataPath, pluginFile, pluginConfig);
    }

    private static String tryGetFile(String resourceName) {
        ClassLoader classLoader = ServerManager.class.getClassLoader();
        if (classLoader == null) throw new NullPointerException("classLoader is null");

        URL resource = classLoader.getResource(resourceName);
        if (resource == null) throw new NullPointerException("resource is null");

        return resource.getFile();
    }

}
