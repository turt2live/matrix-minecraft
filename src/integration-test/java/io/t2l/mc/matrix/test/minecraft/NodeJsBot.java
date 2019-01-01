package io.t2l.mc.matrix.test.minecraft;

import org.awaitility.Awaitility;
import org.awaitility.Duration;

import java.io.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class NodeJsBot {

    private String username;
    private Process process;
    private SynchronousQueue<String> sendQueue = new SynchronousQueue<>();

    public volatile String lastMessage;

    public NodeJsBot(String username) {
        this.username = username;
    }

    public void start() throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.environment().putAll(System.getenv());
        this.process = builder
                //.inheritIO()
                .directory(new File("src/integration-test/nodejs"))
                .command("node", "index.js", username)
                .start();

        new Thread(() -> {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                    if (line.startsWith("P:")) {
                        lastMessage = line.substring("P:".length());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try (PrintWriter w = new PrintWriter(process.getOutputStream())) {
                System.out.println("Waiting for startup");
                Awaitility.await().atMost(new Duration(10, TimeUnit.SECONDS)).until(() -> lastMessage != null);
                System.out.println("Waiting for messages");
                while (true) {
                    String message = sendQueue.take();
                    System.out.println("Sending message...");
                    w.println(message);
                    w.flush();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        if (this.process != null) {
            this.process.destroy();
        }
    }

    public void sendMessage(String message) throws InterruptedException {
        sendQueue.put(message);
    }
}
