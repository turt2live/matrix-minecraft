package io.t2l.mc.matrix.bukkit;

import io.t2l.mc.matrix.minecraft.IMinecraft;

public class BukkitMinecraft implements IMinecraft {

    private MatrixBukkitPlugin plugin;

    public BukkitMinecraft(MatrixBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(String senderName, String text) {
        this.plugin.getServer().broadcastMessage(String.format("<%s> %s", senderName, text));
    }
}
