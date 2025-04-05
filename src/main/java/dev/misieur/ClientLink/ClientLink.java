package dev.misieur.ClientLink;

import com.github.retrooper.packetevents.PacketEvents;
import dev.misieur.ClientLink.Listeners.animatium.AnimatiumPlayerJoinListener;
import dev.misieur.ClientLink.Listeners.feather.FeatherPlayerJoinListener;
import dev.misieur.ClientLink.Listeners.lunar.ApolloPlayerJsonListener;
import dev.misieur.ClientLink.Utils.lunar.JsonPacketUtil;
import dev.misieur.ClientLink.config.Config;
import dev.misieur.ClientLink.config.ConfigLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.digitalingot.feather.serverapi.api.FeatherAPI;
import net.digitalingot.feather.serverapi.api.event.player.PlayerHelloEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ClientLink extends JavaPlugin {
    private static Boolean isPlaceholderAPIEnabled = false;

    public static Boolean IsPlaceholderAPIEnabled() {
        return isPlaceholderAPIEnabled;
    }

    private static ClientLink instance;

    public static ClientLink getInstance() {
        return instance;
    }

    private static Config pluginConfig; // pluginConfig because GetConfig() is already used in JavaPlugin

    public static Config getPluginConfig() {
        return pluginConfig;
    }

    @Override
    public void onLoad() {
        //Creating and loading the API is necessary when shading!
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;
        PacketEvents.getAPI().init();
        if (Bukkit.getPluginManager().isPluginEnabled("FeatherServerAPI")) {
            FeatherAPI.getEventService().subscribe(
                    PlayerHelloEvent.class,
                    new FeatherPlayerJoinListener()
            );
            getLogger().info("FeatherServerAPI is installed.");
        } else {
            getLogger().severe("FeatherServerAPI is not installed. Please download and install it from: https://github.com/FeatherMC/feather-server-api/releases");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI is installed.");
            isPlaceholderAPIEnabled = true;
        } else {
            getLogger().warning("PlaceholderAPI is not installed.");
        }

        saveDefaultConfig();
        pluginConfig = ConfigLoader.load(getConfig(), getDataFolder(), this);
        JsonPacketUtil.updateConfigModuleProperties(pluginConfig);

        new ApolloPlayerJsonListener(this);

        // 1.20.4-R0.1-SNAPSHOT
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] versionParts = version.split("\\.");
        int majorVersion = Integer.parseInt(versionParts[1]); // Extract the “x” from “1.x”.

        if (majorVersion >= 17) {
            new AnimatiumPlayerJoinListener(this);
            getLogger().info("Bukkit version: " + Bukkit.getBukkitVersion() + ". Animatium support enabled.");
        } else {
            getLogger().warning("Animatium support requires Minecraft 1.17 or higher!");
        }

        File backgroundFile = new File(getDataFolder(), "server-background.png");
        if (!backgroundFile.exists()) {
            saveResource("server-background.png", false);
        }

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
