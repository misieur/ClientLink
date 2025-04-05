package dev.misieur.ClientLink.config.feather;

import lombok.Getter;
import net.digitalingot.feather.serverapi.api.meta.DiscordActivity;

@Getter
public class DiscordPresenceConfig {
    private final boolean enabled;
    private final DiscordActivity activity;
    private final boolean enablePartySize;

    public DiscordPresenceConfig(boolean enabled, DiscordActivity activity, boolean enablePartySize) {
        this.enabled = enabled;
        this.activity = activity;
        this.enablePartySize = enablePartySize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public DiscordActivity getActivity() {
        return activity;
    }

    public boolean isEnablePartySize() {
        return enablePartySize;
    }
}