package io.t2l.mc.matrix.bridge;

import io.t2l.mc.matrix.MatrixClient;
import io.t2l.mc.matrix.MatrixException;
import io.t2l.mc.matrix.appservice.Appservice;
import io.t2l.mc.matrix.dto.MatrixMessage;
import io.t2l.mc.matrix.events.PluginEventBus;
import io.t2l.mc.matrix.minecraft.IMinecraft;

import java.io.IOException;
import java.util.UUID;

public class AppserviceBridge implements IBridge {

    private Appservice appservice;
    private IMinecraft minecraft;

    public AppserviceBridge(Appservice appservice, IMinecraft minecraft) {
        this.appservice = appservice;
        this.minecraft = minecraft;

        PluginEventBus.register(new MatrixEventHandler(this));
    }

    @Override
    public void start() throws IOException {
        this.appservice.start();
    }

    @Override
    public void stop() throws IOException {
        this.appservice.stop();
    }

    @Override
    public void sendMatrixMessage(String roomId, UUID sender, String message) throws MatrixException {
        MatrixClient client = this.appservice.getClient(sender.toString());
        client.joinRoom(roomId);
        client.sendMessage(roomId, new MatrixMessage() {{
            body = message;
            msgtype = "m.text";
        }});
    }

    @Override
    public void sendMinecraftMessage(String sourceRoomId, String sender, String plaintext) {
        if (this.appservice.isNamespaced(sender)) {
            return;
        }
        this.minecraft.sendMessage(sender, plaintext);
    }
}
