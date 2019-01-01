package io.t2l.mc.matrix.test.mcserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

public class ServerInstance {

    private File serverJar;
    private Path dataPath;
    private File pluginFile;
    private File pluginConfig;
    private File[] serverConfigs;
    private Process process;
    private Semaphore lock = new Semaphore(1);
    private Exception error = null;

    ServerInstance(File serverJar, File[] serverConfigs, Path dataPath, File pluginFile, File pluginConfig) {
        this.serverJar = serverJar;
        this.dataPath = dataPath;
        this.pluginFile = pluginFile;
        this.pluginConfig = pluginConfig;
        this.serverConfigs = serverConfigs;
    }

    public void start() throws Exception {
        System.out.println("Starting server in directory: " + dataPath.toString());

        System.out.println("Creating plugin directory...");
        Files.createDirectories(Paths.get(dataPath.toString(), "plugins", "MatrixMinecraft"));
        Files.copy(pluginFile.toPath(), Paths.get(dataPath.toString(), "plugins", "MatrixMinecraft.jar"));
        Files.copy(pluginConfig.toPath(), Paths.get(dataPath.toString(), "plugins", "MatrixMinecraft", "config.yml"));

        System.out.println("Copying server JAR...");
        Files.copy(serverJar.toPath(), Paths.get(dataPath.toString(), "server.jar"));

        System.out.println("Copying server configs...");
        for(File serverConfig : serverConfigs) {
            Files.copy(serverConfig.toPath(), Paths.get(dataPath.toString(), serverConfig.getName()));
        }

        System.out.println("Starting server...");
        try (PrintWriter w = new PrintWriter(Paths.get(dataPath.toString(), "eula.txt").toString())) {
            w.println("eula=True");
        }

        lock = new Semaphore(1);
        new Thread(new ServerInstanceRunnable()).start();
        Thread.sleep(500);
        lock.acquire();
        if (error != null) {
            this.stop();
            throw error;
        }
    }

    public void stop() throws IOException {
        if (this.process != null) {
            this.process.destroy();
            lock.release();
        }
    }

    private class ServerInstanceRunnable implements Runnable {
        @Override
        public void run() {
            try {
                lock.acquire();
                process = new ProcessBuilder()
                        .directory(dataPath.toFile())
                        .command("java", "-Xmx1024M", "-XX:-UseParallelGC", "-jar", "server.jar")
                        .start();

                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                        if ((line.contains("Done") && line.contains("For help, type \"help\"")) || line.contains("Ready for connections.")) {
                            lock.release();
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                error = e;
            }
        }
    }
}
