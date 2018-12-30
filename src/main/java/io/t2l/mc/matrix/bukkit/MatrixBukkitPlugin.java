package io.t2l.mc.matrix.bukkit;

import io.t2l.mc.matrix.LogService;
import io.t2l.mc.matrix.appservice.Appservice;
import io.t2l.mc.matrix.bridge.AppserviceBridge;
import io.t2l.mc.matrix.bridge.IBridge;
import io.t2l.mc.matrix.minecraft.IMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class MatrixBukkitPlugin extends JavaPlugin {

    private static MatrixBukkitPlugin instance;

    public static MatrixBukkitPlugin getInstance() {
        return instance;
    }

    private IBridge bridge;

    @Override
    public void onEnable() {
        instance = this;
        LogService.setLogger(this.getLogger());
        this.bridge = null; // just in case we were reloaded

        IMinecraft minecraft = new BukkitMinecraft(this);

        this.saveDefaultConfig();
        if (this.getConfig().getBoolean("appservice.enabled", false)) {
            this.getLogger().info("Bridging to Matrix via the configured appservice");
            bridge = new AppserviceBridge(new Appservice(
                    this.getConfig().getInt("appservice.port", 8080),
                    this.getConfig().getString("appservice.hsToken", "GENERATE_ME"),
                    this.getConfig().getString("appservice.asToken", "GENERATE_ME"),
                    this.getConfig().getString("appservice.userPrefix", "_minecraft_"),
                    this.getConfig().getString("appservice.domain", "matrix.org"),
                    this.getConfig().getString("appservice.hsUrl", "https://matrix.org")
            ), minecraft);
        }

        if (bridge == null) {
            this.getLogger().warning("No bridge configuration was found - please enable at least one route in the config.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            this.getLogger().info("Starting bridge handler...");
            bridge.start();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new ChatListener(bridge), this);
    }

    @Override
    public void onDisable() {
        if (this.bridge != null) {
            try {
                this.bridge.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
