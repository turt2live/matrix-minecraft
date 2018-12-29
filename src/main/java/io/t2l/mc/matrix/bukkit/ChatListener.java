package io.t2l.mc.matrix.bukkit;

import io.t2l.mc.matrix.MatrixException;
import io.t2l.mc.matrix.bridge.IBridge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private IBridge bridge;

    ChatListener(IBridge bridge) {
        this.bridge = bridge;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChatMessage(AsyncPlayerChatEvent e) {
        try {
            this.bridge.sendMessage("!QrQxqLruMjHFnRxrvs:dev.t2host.io", e.getPlayer().getUniqueId(), e.getMessage());
        } catch (MatrixException ex) {
            ex.printStackTrace();
        }
    }
}
