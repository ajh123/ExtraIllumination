package me.ajh123.extra_illumination.utils.neoforge;

import me.ajh123.extra_illumination.utils.PlatformEnergyStorage;

public class EnergyPlatformImpl {
    public static PlatformEnergyStorage makeEnergyStorage(long capacity, long maxInsert, long maxExtract) {
        return new ForgeEnergyStorage(capacity, maxInsert, maxExtract);
    }
}