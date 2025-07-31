package me.ajh123.extra_illumination.utils.neoforge;

import me.ajh123.extra_illumination.utils.PlatformEnergyStorage;
import net.minecraft.nbt.CompoundTag;

public class ForgeEnergyStorage implements PlatformEnergyStorage {
    public ForgeEnergyStorage(long capacity, long maxInsert, long maxExtract) {
    }

    @Override
    public long getStoredEnergy() {
        return 0;
    }

    @Override
    public long getCapacity() {
        return 0;
    }

    @Override
    public long insertEnergy(long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extractEnergy(long amount, boolean simulate) {
        return 0;
    }

    @Override
    public void loadAdditional(CompoundTag tag) {

    }

    @Override
    public void saveAdditional(CompoundTag tag) {

    }
}
