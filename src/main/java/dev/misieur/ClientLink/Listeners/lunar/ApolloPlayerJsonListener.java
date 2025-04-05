package dev.misieur.ClientLink.Listeners.lunar;

import com.google.gson.JsonObject;
import dev.misieur.ClientLink.ClientLink;
import dev.misieur.ClientLink.Utils.lunar.JsonPacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.Messenger;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ApolloPlayerJsonListener implements Listener {

    private final Set<UUID> playersRunningApollo = new HashSet<>();

    public ApolloPlayerJsonListener(ClientLink plugin) {
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerIncomingPluginChannel(plugin, "lunar:apollo", (s, player, bytes) -> { });
        messenger.registerIncomingPluginChannel(plugin, "apollo:json", (s, player, bytes) -> { });
        messenger.registerOutgoingPluginChannel(plugin, "apollo:json");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private boolean isPlayerRunningApollo(Player player) {
        return this.playersRunningApollo.contains(player.getUniqueId());
    }

    @EventHandler
    private void onRegisterChannel(PlayerRegisterChannelEvent event) {
        if (!event.getChannel().equalsIgnoreCase("lunar:apollo")) {
            return;
        }

        Player player = event.getPlayer();
        JsonPacketUtil.enableModules(player);

        // Sending the player's world name to the client is required for some modules
        JsonPacketUtil.sendPacket(player, this.createUpdatePlayerWorldMessage(player));

        this.playersRunningApollo.add(player.getUniqueId());
        Bukkit.getLogger().info(player.getName()+" is using Lunar!");
    }

    @EventHandler
    private void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // Sending the player's world name to the client is required for some modules
        JsonPacketUtil.sendPacket(player, this.createUpdatePlayerWorldMessage(player));
    }

    private JsonObject createUpdatePlayerWorldMessage(Player player) {
        JsonObject message = new JsonObject();
        message.addProperty("@type", "type.googleapis.com/lunarclient.apollo.player.v1.UpdatePlayerWorldMessage");
        message.addProperty("world", player.getWorld().getName());
        return message;
    }
}
