package dev.misieur.ClientLink.config;

import dev.misieur.ClientLink.config.feather.DiscordPresenceConfig;
import dev.misieur.ClientLink.config.feather.FeatherModsConfig;
import dev.misieur.ClientLink.config.feather.ServerListBackgroundConfig;
import dev.misieur.ClientLink.config.lunar.LunarModsConfig;

public class Config {
    private final boolean missPenaltyBypassed;
    private final boolean leftClickItemUsageEnabled;
    private final boolean shadersDisabled;
    private final boolean chunkReloadingDisabled;
    private final boolean broadcastingDisabled;
    private final boolean antiPortalTrapsEnabled;
    private final ServerListBackgroundConfig serverListBackground;
    private final DiscordPresenceConfig discordPresence;
    private final FeatherModsConfig featherMods;
    private final LunarModsConfig lunarModsConfig;

    public Config(boolean missPenaltyBypassed,
                  boolean leftClickItemUsageEnabled,
                  boolean shadersDisabled,
                  boolean chunkReloadingDisabled,
                  boolean broadcastingDisabled,
                  boolean antiPortalTrapsEnabled,
                  ServerListBackgroundConfig serverListBackground,
                  DiscordPresenceConfig discordPresence,
                  FeatherModsConfig featherMods,
                  LunarModsConfig lunarModsConfig) {
        this.missPenaltyBypassed = missPenaltyBypassed;
        this.leftClickItemUsageEnabled = leftClickItemUsageEnabled;
        this.shadersDisabled = shadersDisabled;
        this.chunkReloadingDisabled = chunkReloadingDisabled;
        this.broadcastingDisabled = broadcastingDisabled;
        this.antiPortalTrapsEnabled = antiPortalTrapsEnabled;
        this.serverListBackground = serverListBackground;
        this.discordPresence = discordPresence;
        this.featherMods = featherMods;
        this.lunarModsConfig = lunarModsConfig;
    }

    public boolean isMissPenaltyBypassed() {
        return missPenaltyBypassed;
    }

    public boolean isLeftClickItemUsageEnabled() {
        return leftClickItemUsageEnabled;
    }

    public boolean isShadersDisabled() {
        return shadersDisabled;
    }

    public boolean isChunkReloadingDisabled() {
        return chunkReloadingDisabled;
    }

    public boolean isBroadcastingDisabled() {
        return broadcastingDisabled;
    }

    public boolean isAntiPortalTrapsEnabled() {
        return antiPortalTrapsEnabled;
    }

    public ServerListBackgroundConfig getServerListBackground() {
        return serverListBackground;
    }

    public DiscordPresenceConfig getDiscordPresence() {
        return discordPresence;
    }

    public FeatherModsConfig getFeatherMods() {
        return featherMods;
    }

    public LunarModsConfig getLunarModsConfig() {
        return lunarModsConfig;
    }
}