package io.t2l.mc.matrix.bridge;

import io.t2l.mc.matrix.MatrixClient;
import io.t2l.mc.matrix.MatrixException;
import io.t2l.mc.matrix.appservice.AppService;
import io.t2l.mc.matrix.dto.MatrixMessage;

import java.util.UUID;

public class AppServiceBridge implements IBridge {

    private AppService appservice;

    public AppServiceBridge(AppService appservice) {
        this.appservice = appservice;
    }

    @Override
    public void sendMessage(String roomId, UUID sender, String message) throws MatrixException {
        MatrixClient client = this.appservice.getClient(sender.toString());
        client.joinRoom(roomId);
        client.sendMessage(roomId, new MatrixMessage() {{
            body = message;
            msgtype = "m.text";
        }});
    }
}
