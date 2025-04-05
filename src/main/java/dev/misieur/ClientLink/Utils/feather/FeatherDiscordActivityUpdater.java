package dev.misieur.ClientLink.Utils.feather;

import dev.misieur.ClientLink.ClientLink;
import dev.misieur.ClientLink.config.Config;
import dev.misieur.ClientLink.config.feather.DiscordPresenceConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import net.digitalingot.feather.serverapi.api.FeatherAPI;
import net.digitalingot.feather.serverapi.api.meta.DiscordActivity;
import net.digitalingot.feather.serverapi.api.player.FeatherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static dev.misieur.ClientLink.Listeners.feather.FeatherPlayerJoinListener.joinTimestamps;

public class FeatherDiscordActivityUpdater {
    public static void updateDiscordActivity(FeatherPlayer player) {
        DiscordActivity.Builder discordActivity = DiscordActivity.builder();
        Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
        Config config = ClientLink.getPluginConfig();
        DiscordPresenceConfig discordPresence = config.getDiscordPresence();
        if (ClientLink.IsPlaceholderAPIEnabled()) {
            if (discordPresence.getActivity().getImage().isPresent()) {
                discordActivity.withImage(PlaceholderAPI.setPlaceholders(bukkitPlayer, discordPresence.getActivity().getImage().get()));
            }
            if (discordPresence.getActivity().getImageText().isPresent()) {
                discordActivity.withImageText(PlaceholderAPI.setPlaceholders(bukkitPlayer, discordPresence.getActivity().getImageText().get()));
            }
            if (discordPresence.getActivity().getState().isPresent()) {
                discordActivity.withState(PlaceholderAPI.setPlaceholders(bukkitPlayer, discordPresence.getActivity().getState().get()));
            }
            if (discordPresence.getActivity().getDetails().isPresent()) {
                discordActivity.withDetails(PlaceholderAPI.setPlaceholders(bukkitPlayer, discordPresence.getActivity().getDetails().get()));
            }
        } else {
            if (discordPresence.getActivity().getImage().isPresent()) {
                discordActivity.withImage(discordPresence.getActivity().getImage().get());
            }
            if (discordPresence.getActivity().getImageText().isPresent()) {
                discordActivity.withImageText(discordPresence.getActivity().getImageText().get());
            }
            if (discordPresence.getActivity().getState().isPresent()) {
                discordActivity.withState(discordPresence.getActivity().getState().get());
            }
            if (discordPresence.getActivity().getDetails().isPresent()) {
                discordActivity.withDetails(discordPresence.getActivity().getDetails().get());
            }
        }
        if (discordPresence.getActivity().getPartySize().isPresent()) {
            discordActivity.withPartySize(
                    Math.max(Bukkit.getOnlinePlayers().size(), 1), //Party size must be at least 1
                    Math.max(
                            Math.max(Bukkit.getMaxPlayers(), 1), //Party maximum size must be at least 1
                            Bukkit.getOnlinePlayers().size()) //Party size cannot exceed party maximum size
            );
        }
        discordActivity.withStartTimestamp(joinTimestamps.get(player.getUniqueId())); // Shouldn't have any errors here
        FeatherAPI.getMetaService().updateDiscordActivity(player, discordActivity.build());
    }
}
