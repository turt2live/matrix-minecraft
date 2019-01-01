package io.t2l.mc.matrix;

import io.t2l.mc.matrix.test.mcserver.MCVersion;
import io.t2l.mc.matrix.test.mcserver.Server;
import io.t2l.mc.matrix.test.mcserver.ServerInstance;
import io.t2l.mc.matrix.test.mcserver.ServerManager;
import io.t2l.mc.matrix.test.minecraft.NodeJsBot;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class PlainServerIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(5, TimeUnit.MINUTES);

    private static final String BOT_USERNAME = "testing-bot";
    private static final boolean USE_LOCAL_SERVER = false;

    private Server server;
    private MCVersion version;
    private NodeJsBot bot;
    private static ServerInstance instance;
    private static Server forServer;
    private static MCVersion forVersion;

    public PlainServerIntegrationTest(Server server, MCVersion version) {
        this.server = server;
        this.version = version;
    }

    @Parameterized.Parameters
    public static Collection input() {
        ArrayList<Object[]> items = new ArrayList<>();
        for (Server server : Server.class.getEnumConstants()) {
            for (MCVersion version : server.versions) {
                items.add(new Object[]{server, version});
            }
        }
        return items;
    }

    @Before
    public void init() throws Exception {
        if (!USE_LOCAL_SERVER) {
            if (instance != null) {
                if (forServer != server || forVersion != version) {
                    instance.stop();
                } else return;
            }
            System.out.println("Creating server...");
            instance = ServerManager.createServer(this.server, this.version);
            instance.start();
            forServer = server;
            forVersion = version;
        }

        this.bot = new NodeJsBot(BOT_USERNAME);
        this.bot.start();
    }

    @After
    public void teardown() {
        if (this.bot != null) {
            this.bot.stop();
        }
    }

    @AfterClass
    public static void teardownClass() throws IOException {
        if (instance != null) {
            instance.stop();
        }
    }

    @Test
    public void botCanEcho() throws InterruptedException {
        this.bot.sendMessage("hello");
        Awaitility.await().atMost(new Duration(10, TimeUnit.SECONDS))
                .until(() -> Objects.equals(this.bot.lastMessage, "{\"extra\":[{\"text\":\"<" + BOT_USERNAME + "> hello\"}],\"text\":\"\"}")
                        || Objects.equals(this.bot.lastMessage, "{\"text\":\"<" + BOT_USERNAME + "> hello\"}"));
    }
}
