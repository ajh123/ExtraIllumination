package me.ajh123.extra_illumination.neoforge;

import me.ajh123.extra_illumination.ExtraIllumination;
import net.neoforged.fml.common.Mod;

@Mod(ExtraIllumination.MOD_ID)
public final class ExtraIlluminationNeoForge {
    public ExtraIlluminationNeoForge() {
        // Run our common setup.
        ExtraIllumination.init();
    }
}
