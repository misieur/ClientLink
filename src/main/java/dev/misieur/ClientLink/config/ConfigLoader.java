package dev.misieur.ClientLink.config;

import dev.misieur.ClientLink.ClientLink;
import dev.misieur.ClientLink.Utils.feather.FeatherDiscordActivityUpdater;
import dev.misieur.ClientLink.config.feather.DiscordPresenceConfig;
import dev.misieur.ClientLink.config.feather.FeatherModsConfig;
import dev.misieur.ClientLink.config.feather.ServerListBackgroundConfig;
import dev.misieur.ClientLink.config.lunar.LunarModsConfig;
import net.digitalingot.feather.serverapi.api.FeatherAPI;
import net.digitalingot.feather.serverapi.api.meta.DiscordActivity;
import net.digitalingot.feather.serverapi.api.meta.ServerListBackground;
import net.digitalingot.feather.serverapi.api.meta.ServerListBackgroundFactory;
import net.digitalingot.feather.serverapi.api.meta.exception.ImageSizeExceededException;
import net.digitalingot.feather.serverapi.api.meta.exception.InvalidImageException;
import net.digitalingot.feather.serverapi.api.meta.exception.UnsupportedImageFormatException;
import net.digitalingot.feather.serverapi.api.model.FeatherMod;
import net.digitalingot.feather.serverapi.api.player.FeatherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigLoader {

    public static Config load(FileConfiguration config, File dataFolder, ClientLink plugin) {

        boolean missPenaltyBypassed = config.getBoolean(ConfigKeys.MISS_PENALTY);
        boolean leftClickItemUsageEnabled = config.getBoolean(ConfigKeys.LEFT_CLICK_ITEM_USAGE);
        boolean shadersDisabled = config.getBoolean(ConfigKeys.DISABLE_SHADERS);
        boolean chunkReloadingDisabled = config.getBoolean(ConfigKeys.DISABLE_CHUNK_RELOADING);
        boolean broadcastingDisabled = config.getBoolean(ConfigKeys.DISABLE_BROADCASTING);
        boolean antiPortalTrapsEnabled = config.getBoolean(ConfigKeys.ANTI_PORTAL_TRAPS);
        ServerListBackgroundConfig serverListBg = loadServerListBackground(config, dataFolder, plugin);
        DiscordPresenceConfig discordPresence = loadDiscordPresence(config, plugin);
        FeatherModsConfig featherMods = loadFeatherMods(config, plugin);
        LunarModsConfig lunarModsConfig = loadLunarMods(config, plugin);

        return new Config(missPenaltyBypassed,leftClickItemUsageEnabled, shadersDisabled, chunkReloadingDisabled, broadcastingDisabled, antiPortalTrapsEnabled, serverListBg, discordPresence, featherMods, lunarModsConfig);
    }

    private static LunarModsConfig loadLunarMods(FileConfiguration config, ClientLink plugin) {
        List<String> disabledMods = config.getStringList(ConfigKeys.LUNAR_MODS_DISABLED);
        List<String> enabledMods = config.getStringList(ConfigKeys.LUNAR_MODS_ENABLED);

        Bukkit.getLogger().info("Loaded Lunar mods configuration:");
        Bukkit.getLogger().info("Disabled: " + disabledMods);
        Bukkit.getLogger().info("Enabled: " + enabledMods);

        return new LunarModsConfig(disabledMods, enabledMods);
    }

    private static FeatherModsConfig loadFeatherMods(FileConfiguration config, ClientLink plugin) {
        List<FeatherMod> blockedMods = config.getStringList(ConfigKeys.FEATHER_MODS_BLOCKED).stream()
                .map(FeatherMod::new)
                .collect(java.util.stream.Collectors.toList());

        List<FeatherMod> disabledMods = config.getStringList(ConfigKeys.FEATHER_MODS_DISABLED).stream()
                .map(FeatherMod::new)
                .collect(java.util.stream.Collectors.toList());

        List<FeatherMod> enabledMods = config.getStringList(ConfigKeys.FEATHER_MODS_ENABLED).stream()
                .map(FeatherMod::new)
                .collect(java.util.stream.Collectors.toList());

        Bukkit.getLogger().info("Loaded Feather mods configuration:");
        Bukkit.getLogger().info("Blocked: " + blockedMods);
        Bukkit.getLogger().info("Disabled: " + disabledMods);
        Bukkit.getLogger().info("Enabled: " + enabledMods);

        return new FeatherModsConfig(blockedMods, disabledMods, enabledMods);
    }

    private static boolean loadMissPenalty(FileConfiguration config, String key) {
        return config.getBoolean(key);
    }

    private static ServerListBackgroundConfig loadServerListBackground(FileConfiguration config, File dataFolder, ClientLink plugin) {
        boolean enabled = config.getBoolean(ConfigKeys.SERVER_BG_ENABLED);
        Path imagePath = new File(dataFolder, "server-background.png").toPath();

        if (enabled) {
            try {
                ServerListBackgroundFactory factory = FeatherAPI.getMetaService().getServerListBackgroundFactory();
                ServerListBackground background = factory.byPath(imagePath);
                FeatherAPI.getMetaService().setServerListBackground(background);
                Bukkit.getLogger().info("Feather server list background enabled");
            } catch (UnsupportedImageFormatException e) {
                handleImageError("Image format not supported. Only PNG is supported.");
            } catch (ImageSizeExceededException e) {
                handleImageError("Image is too large. Max dimensions: 1009×202, Max size: 512KB");
            } catch (InvalidImageException e) {
                handleImageError("Image file is corrupted or invalid");
            } catch (IOException e) {
                handleImageError("Error reading image file: " + e.getMessage());
            }
        }
        return new ServerListBackgroundConfig(enabled, imagePath);
    }

    private static DiscordPresenceConfig loadDiscordPresence(FileConfiguration config, ClientLink plugin) {
        boolean enabled = config.getBoolean(ConfigKeys.DISCORD_PRESENCE_ENABLED);
        boolean enablePartySize = config.getBoolean(ConfigKeys.DISCORD_PARTY_SIZE);
        DiscordActivity activity = null;

        if (enabled) {
            activity = buildDiscordActivity(config);
            scheduleActivityUpdates(plugin, enablePartySize);
            Bukkit.getLogger().info("Feather Discord rich presence enabled");
        }
        return new DiscordPresenceConfig(enabled, activity, enablePartySize);
    }

    private static DiscordActivity buildDiscordActivity(FileConfiguration config) {
        DiscordActivity.Builder builder = DiscordActivity.builder()
                .withImage(config.getString(ConfigKeys.DISCORD_IMAGE))
                .withImageText(config.getString(ConfigKeys.DISCORD_IMAGE_TEXT))
                .withState(config.getString(ConfigKeys.DISCORD_STATE))
                .withDetails(config.getString(ConfigKeys.DISCORD_DETAILS))
                .withStartTimestamp(0L);

        if (config.getBoolean(ConfigKeys.DISCORD_PARTY_SIZE)) {
            builder.withPartySize(1, 1);
        }
        return builder.build();
    }

    private static void scheduleActivityUpdates(ClientLink plugin, boolean enablePartySize) {
        if (ClientLink.IsPlaceholderAPIEnabled() || enablePartySize) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                for (FeatherPlayer player : FeatherAPI.getPlayerService().getPlayers()) {
                    FeatherDiscordActivityUpdater.updateDiscordActivity(player);
                }
            }, 20L, 5 * 20L);
        }
    }

    private static void handleImageError(String message) {
        Bukkit.getLogger().severe(message);
    }
}