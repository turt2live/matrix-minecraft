package io.t2l.mc.matrix.bridge;

import com.github.jneat.minibus.EventBusHandler;
import io.t2l.mc.matrix.LogService;
import io.t2l.mc.matrix.dto.MatrixAppserviceTransactionEvent;
import io.t2l.mc.matrix.events.AppserviceEvent;

public class MatrixEventHandler extends EventBusHandler<AppserviceEvent> {

    private IBridge bridge;

    MatrixEventHandler(IBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void handle(AppserviceEvent ev) throws Throwable {
        MatrixAppserviceTransactionEvent event = ev.event;
        if (!event.type.equals("m.room.message")) return;
        if (event.stateKey != null) return;
        if (!event.content.containsKey("body")) return;

        String bodyRaw = event.content.get("body").toString();
        if (bodyRaw.isEmpty()) return;

        LogService.get().info(String.format("Processing %s from %s in %s", event.type, event.sender, event.roomId));
        this.bridge.sendMinecraftMessage(event.roomId, event.sender, bodyRaw);
    }
}
