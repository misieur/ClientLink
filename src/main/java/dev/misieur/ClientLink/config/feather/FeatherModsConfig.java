package dev.misieur.ClientLink.config.feather;

import lombok.Getter;
import net.digitalingot.feather.serverapi.api.model.FeatherMod;

import java.util.List;

@Getter
public class FeatherModsConfig {
    private final List<FeatherMod> blockedMods;
    private final List<FeatherMod> disabledMods;
    private final List<FeatherMod> enabledMods;

    public FeatherModsConfig(List<FeatherMod> blockedMods, List<FeatherMod> disabledMods, List<FeatherMod> enabledMods) {
        this.blockedMods = blockedMods;
        this.disabledMods = disabledMods;
        this.enabledMods = enabledMods;
    }

    public List<FeatherMod> getBlockedMods() {
        return blockedMods;
    }

    public List<FeatherMod> getDisabledMods() {
        return disabledMods;
    }

    public List<FeatherMod> getEnabledMods() {
        return enabledMods;
    }
}
