package io.t2l.mc.matrix.bukkit;

import io.t2l.mc.matrix.appservice.AppService;
import io.t2l.mc.matrix.bridge.AppServiceBridge;
import io.t2l.mc.matrix.bridge.IBridge;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MatrixBukkitPlugin extends JavaPlugin {

    private static MatrixBukkitPlugin instance;

    public static MatrixBukkitPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        IBridge bridge = null;

        this.saveDefaultConfig();
        if (this.getConfig().getBoolean("appservice.enabled", false)) {
            this.getLogger().info("Bridging to Matrix via the configured appservice");
            bridge = new AppServiceBridge(new AppService(
                    this.getConfig().getString("appservice.hsToken", "GENERATE_ME"),
                    this.getConfig().getString("appservice.asToken", "GENERATE_ME"),
                    this.getConfig().getString("appservice.userPrefix", "_minecraft_"),
                    this.getConfig().getString("appservice.domain", "matrix.org"),
                    this.getConfig().getString("appservice.hsUrl", "https://matrix.org")
            ));
        }

        if (bridge == null) {
            this.getLogger().warning("No bridge configuration was found - please enable at least one route in the config.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new ChatListener(bridge), this);
    }
}
