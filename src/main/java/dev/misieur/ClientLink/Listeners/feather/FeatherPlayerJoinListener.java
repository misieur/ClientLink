package dev.misieur.ClientLink.Listeners.feather;

import dev.misieur.ClientLink.ClientLink;
import dev.misieur.ClientLink.Utils.feather.FeatherDiscordActivityUpdater;
import dev.misieur.ClientLink.config.Config;
import net.digitalingot.feather.serverapi.api.event.player.PlayerHelloEvent;
import net.digitalingot.feather.serverapi.api.player.FeatherPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static org.bukkit.Bukkit.getLogger;


public class FeatherPlayerJoinListener implements Consumer<PlayerHelloEvent> {

    public static final Map<UUID, Long> joinTimestamps = new HashMap<>();

    @Override
    public void accept(PlayerHelloEvent event) {
        Config config = ClientLink.getPluginConfig();
        FeatherPlayer player = event.getPlayer();
        getLogger().info(player.getName() + " is using Feather!");
        player.bypassMissPenalty(config.isMissPenaltyBypassed());
        joinTimestamps.put(player.getUniqueId(), System.currentTimeMillis());
        if (config.getDiscordPresence().isEnabled()) {
            FeatherDiscordActivityUpdater.updateDiscordActivity(player);
        }
        player.blockMods(config.getFeatherMods().getBlockedMods());
        player.enableMods(config.getFeatherMods().getEnabledMods());
        player.disableMods(config.getFeatherMods().getDisabledMods());
    }
}
