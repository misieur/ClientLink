package dev.misieur.ClientLink.Listeners.animatium;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import dev.misieur.ClientLink.ClientLink;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AnimatiumPlayerJoinListener implements Listener {
    private final Set<UUID> playersRunningAnimatium = new HashSet<>();

    public AnimatiumPlayerJoinListener(ClientLink plugin) {
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerIncomingPluginChannel(plugin, "animatium:info", (s, player, bytes) -> {
            playersRunningAnimatium.add(player.getUniqueId());
            Bukkit.getLogger().info(player.getName() + " is using Animatium!");
            Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "animatium:set_features");
            List<String> enabledFeatures = new ArrayList<>();
            if (ClientLink.getPluginConfig().isMissPenaltyBypassed()) {
                enabledFeatures.add("miss_penalty");
            }
            if (ClientLink.getPluginConfig().isLeftClickItemUsageEnabled()) {
                enabledFeatures.add("left_click_item_usage");
            }
            try {
                player.sendPluginMessage(plugin, "animatium:set_features", convertListToBytes(enabledFeatures));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private boolean isPlayerRunningAnimatium(Player player) {
        return this.playersRunningAnimatium.contains(player.getUniqueId());
    }

    public static byte[] convertListToBytes(List<String> list) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        // Écrit la taille de la liste en varint
        writeVarInt(dos, list.size());

        for (String feature : list) {
            // Écrit la chaîne en UTF-8 préfixée par sa longueur en varint
            byte[] featureBytes = feature.getBytes(StandardCharsets.UTF_8);
            writeVarInt(dos, featureBytes.length);
            dos.write(featureBytes);
        }

        return bos.toByteArray();
    }

    private static void writeVarInt(DataOutputStream dos, int value) throws IOException {
        while (true) {
            if ((value & ~0x7F) == 0) {
                dos.writeByte(value);
                return;
            } else {
                dos.writeByte((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) { // Why? Because Animatium is poorly made
        byte[] result = "Animatium".getBytes(StandardCharsets.UTF_8);

        WrapperPlayServerPluginMessage packet = new WrapperPlayServerPluginMessage(
                "minecraft:register",
                result
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(event.getPlayer(), packet);
    }
}
