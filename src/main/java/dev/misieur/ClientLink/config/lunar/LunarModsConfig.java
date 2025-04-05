package dev.misieur.ClientLink.config.lunar;

import java.util.List;

public class LunarModsConfig {
    private final List<String> disabledMods;
    private final List<String> enabledMods;

    public LunarModsConfig(List<String> disabledMods, List<String> enabledMods) {
        this.disabledMods = disabledMods;
        this.enabledMods = enabledMods;
    }

    public List<String> getDisabledMods() {
        return disabledMods;
    }

    public List<String> getEnabledMods() {
        return enabledMods;
    }
}
