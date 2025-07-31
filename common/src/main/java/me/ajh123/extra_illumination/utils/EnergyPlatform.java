package me.ajh123.extra_illumination.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class EnergyPlatform {
    @ExpectPlatform
    public static PlatformEnergyStorage makeEnergyStorage(long capacity, long maxInsert, long maxExtract) {
        throw new AssertionError();
    }
}