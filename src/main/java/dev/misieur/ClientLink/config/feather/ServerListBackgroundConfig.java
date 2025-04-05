package dev.misieur.ClientLink.config.feather;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public class ServerListBackgroundConfig {
    private final boolean enabled;
    private final Path imagePath;

    public ServerListBackgroundConfig(boolean enabled, Path imagePath) {
        this.enabled = enabled;
        this.imagePath = imagePath;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Path getImagePath() {
        return imagePath;
    }
}