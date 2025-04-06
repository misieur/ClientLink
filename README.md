# ClientLink

ClientLink is a plugin for Spigot/Bukkit, Paper, and their forks that enhances integration with modified Minecraft clients such as Feather Client, Lunar Client, and Animatium. It provides features to customize the gaming experience for users of these clients, including configuring server display, managing allowed or blocked mods, and providing additional information via Feather's Discord Rich Presence.

## Features

*   **Feather Client Integration:**
    *    <details>
          <summary>Configuration of the server background in the Feather server list.</summary>
          
          ![Feather Background Preview](https://github.com/user-attachments/assets/7e9eb7d8-8f6f-4f49-ba44-e30a1de6bc9c)
    *   <details>
          <summary>Management of Discord Rich Presence to display custom server information.</summary>
          
          ![Feather Discord Presence Preview](https://github.com/user-attachments/assets/31d9c08d-373b-4832-aa9f-7284d3ba0fa8)
          
        </details>
    *   Blocking, enabling, or disabling specific Feather mods.
*   **Lunar Client Integration:**
    *   Disabling server-specific game features (shaders, chunk reloading, broadcasting, portal traps).
    *   Enabling or disabling specific Lunar mods.
*   **Animatium Integration:**
    *   Bypass miss penalty (works with Animatium only on servers running Minecraft 1.17+ due to the mod's implementation).
    *   Enable left click item usage.
*   **General Integration**
    *   Bypass miss penalty is available for all launchers.
*   **Centralized Configuration:** All features are configurable via a `config.yml` file.
*   **PlaceholderAPI Support:** Use of PlaceholderAPI for further customization of displayed information (Discord Rich Presence).

## Prerequisites

*   Spigot/Bukkit, Paper, or their forks server (version 1.8 or higher).
*   **FeatherServerAPI (Required for the plugin to function).**
*   PlaceholderAPI (optional, for increased customization).

## Installation

1.  [Download the latest version of ClientLink](https://github.com/misieur/ClientLink/releases).
2.  Place the `.jar` file in the `plugins` folder of your server.
3.  **Install FeatherServerAPI in your `plugins` folder. This is required for the plugin to function.** Download it here: [https://github.com/FeatherMC/feather-server-api/releases](https://github.com/FeatherMC/feather-server-api/releases).
4.  (Optional) Install PlaceholderAPI if you want to use placeholders in the Discord Rich Presence.
5.  Restart the server.
6.  Configure the plugin by modifying the `plugins/ClientLink/config.yml` file.
7.  Restart the server to apply the changes.

## Configuration

The `config.yml` file allows you to configure all the features of the plugin. Here is an example configuration:

```yaml
bypass-miss-penalty: true #Feather, Animatium & Lunar
left-click-item-usage: true #Animatium only
disable-shaders: false #Lunar only
disable-chunk-reloading: true #Lunar only
disable-broadcasting: true #Lunar only
anti-portal-traps: true #Lunar only

feather-server-list-background: # https://docs.feathermc.com/server-api/meta/server-list-background
  enabled: true
  image: "server-background.png"

discord-rich-presence: # https://docs.feathermc.com/server-api/meta/discord-rich-presence Feather only (Your server must be a part of Lunar Client's ServerMappings to use Rich Presence Module. https://lunarclient.dev/apollo/developers/modules/richpresence)
  enabled: true
  image: "https://upload.wikimedia.org/wikipedia/en/c/c7/Chill_guy_original_artwork.jpg"
  imageText: "Epic Survival Server"
  state: "Mining diamonds"
  details: "Join us at play.example.com"
  enable-party-size: false
feather-mods-blocked: # https://docs.feathermc.com/server-api/mods#reference
  - perspective # please don't do that
  - teamtracker
feather-mods-disabled: # players will still be able to reactivate them
  - autohidehud
feather-mods-enabled: # players will still be able to disable them
  - saturation
lunar-mods-disabled:
  - freelook # please don't do that
lunar-mods-enabled:
  - minimap
```

*   `bypass-miss-penalty`: Enables or disables bypassing the miss penalty for players using Feather, Animatium, and Lunar.  Works with Animatium only on servers running Minecraft 1.17+ due to the mod's implementation.
*   `left-click-item-usage`: Enables or disables left click item usage for players using Animatium.
*   `disable-shaders`: Enables or disables shaders for players using Lunar Client.
*   `disable-chunk-reloading`: Enables or disables chunk reloading for players using Lunar Client.
*   `disable-broadcasting`: Enables or disables broadcasting for players using Lunar Client.
*   `anti-portal-traps`: Enables or disables portal traps for players using Lunar Client.
*   `feather-server-list-background`:
    *   `enabled`: Enables or disables the custom background in the Feather server list.
    *   `image`: Name of the image file (PNG) to use as background. Place the image in the `plugins/ClientLink/` folder.
*   `discord-rich-presence`:
    *   `enabled`: Enables or disables Discord Rich Presence for players using Feather Client.
    *   `image`: URL or name of the image to display.
    *   `imageText`: Text displayed when hovering over the image.
    *   `state`: State displayed in the Discord Rich Presence.
    *   `details`: Details displayed in the Discord Rich Presence.
    *   `enable-party-size`: Enables or disables displaying the number of online players in the Discord Rich Presence.
*   `feather-mods-blocked`: List of Feather mods that players will not be able to use on the server.
*   `feather-mods-disabled`: List of Feather mods that are disabled by default, but players can re-enable.
*   `feather-mods-enabled`: List of Feather mods that are enabled by default, but players can disable.
*   `lunar-mods-disabled`: List of Lunar Client mods disabled on the server.
*   `lunar-mods-enabled`: List of Lunar Client mods enabled on the server.

## Support

If you encounter any problems or have questions, [please contact me via Discord](https://discord.com/users/1012039502287622244) or create an issue on the GitHub repository.
