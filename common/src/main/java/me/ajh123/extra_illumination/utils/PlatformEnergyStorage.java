package me.ajh123.extra_illumination.utils;

import net.minecraft.nbt.CompoundTag;

public interface PlatformEnergyStorage {
    long getStoredEnergy();
    long getCapacity();
    long insertEnergy(long amount, boolean simulate);
    long extractEnergy(long amount, boolean simulate);
    void loadAdditional(CompoundTag tag);
    void saveAdditional(CompoundTag tag);
}