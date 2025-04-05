package dev.misieur.ClientLink.Utils.lunar;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.*;
import dev.misieur.ClientLink.ClientLink;
import dev.misieur.ClientLink.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonPacketUtil {
    private static final List<String> APOLLO_MODULES = Arrays.asList("limb", "beam", "border", "chat", "colored_fire", "combat", "cooldown",
            "entity", "glow", "hologram", "mod_setting", "nametag", "nick_hider", "notification", "packet_enrichment", "rich_presence",
            "server_rule", "staff_mod", "stopwatch", "team", "title", "tnt_countdown", "transfer", "vignette", "waypoint"
    );

    private static final Table<String, String, Object> CONFIG_MODULE_PROPERTIES = HashBasedTable.create();

    public static void updateConfigModuleProperties(Config config) {
        CONFIG_MODULE_PROPERTIES.put("combat", "disable-miss-penalty", config.isMissPenaltyBypassed());
        CONFIG_MODULE_PROPERTIES.put("server_rule", "disable-shaders", config.isShadersDisabled());
        CONFIG_MODULE_PROPERTIES.put("server_rule", "disable-chunk-reloading", config.isChunkReloadingDisabled());
        CONFIG_MODULE_PROPERTIES.put("server_rule", "disable-broadcasting", config.isBroadcastingDisabled());
        CONFIG_MODULE_PROPERTIES.put("server_rule", "anti-portal-traps", config.isAntiPortalTrapsEnabled());
        for (String mod : config.getLunarModsConfig().getDisabledMods()) {
            CONFIG_MODULE_PROPERTIES.put("mod_setting", mod + ".enabled", false);
        }
        for (String mod : config.getLunarModsConfig().getEnabledMods()) {
            CONFIG_MODULE_PROPERTIES.put("mod_setting", mod + ".enabled", true);
        }
    }

    public static void sendPacket(Player player, JsonObject message) {
        player.sendPluginMessage(ClientLink.getInstance(), "apollo:json", message.toString().getBytes());
    }

    public static void broadcastPacket(JsonObject message) {
        byte[] data = message.toString().getBytes();

        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendPluginMessage(ClientLink.getInstance(), "apollo:json", data));
    }

    public static JsonObject createEnableModuleObject(@NotNull String module, Map<String, Object> properties) {
        JsonObject enableModuleObject = new JsonObject();
        enableModuleObject.addProperty("apollo_module", module);
        enableModuleObject.addProperty("enable", true);

        if (properties != null) {
            JsonObject propertiesObject = new JsonObject();
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                propertiesObject.add(entry.getKey(), JsonPacketUtil.convertToJsonElement(entry.getValue()));
            }

            enableModuleObject.add("properties", propertiesObject);
        }

        return enableModuleObject;
    }

    private static JsonElement convertToJsonElement(Object value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        } else if (value instanceof String) {
            return new JsonPrimitive((String) value);
        } else if (value instanceof Number) {
            return new JsonPrimitive((Number) value);
        } else if (value instanceof Boolean) {
            return new JsonPrimitive((Boolean) value);
        } else if (value instanceof List) {
            JsonArray jsonArray = new JsonArray();
            for (Object item : (List<?>) value) {
                jsonArray.add(JsonPacketUtil.convertToJsonElement(item));
            }
            return jsonArray;
        }

        throw new RuntimeException("Unable to wrap value of type '" + value.getClass().getSimpleName() + "'!");
    }

    public static void enableModules(Player player) {
        JsonArray settings = APOLLO_MODULES.stream()
                .map(module -> JsonPacketUtil.createEnableModuleObject(module, CONFIG_MODULE_PROPERTIES.row(module)))
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);

        JsonObject message = new JsonObject();
        message.addProperty("@type", "type.googleapis.com/lunarclient.apollo.configurable.v1.OverrideConfigurableSettingsMessage");
        message.add("configurable_settings", settings);

        JsonPacketUtil.sendPacket(player, message);
    }
}
