package me.ajh123.extra_illumination.utils.fabric;

import me.ajh123.extra_illumination.utils.PlatformEnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class FabricEnergyStorage extends SimpleEnergyStorage implements PlatformEnergyStorage {
    public FabricEnergyStorage(long capacity, long maxInsert, long maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    @Override
    public long getStoredEnergy() {
        return this.amount;
    }

    @Override
    public long getCapacity() {
        return this.capacity;
    }

    @Override
    public long insertEnergy(long amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            // Try to insert, will return how much was actually inserted
            long amountInserted = this.insert(amount, transaction);
            if (!simulate) {
                // "Commit" the transaction to make sure the change is applied.
                transaction.commit();
            }
            return amountInserted;
        }
    }

    @Override
    public long extractEnergy(long amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            // Try to extract, will return how much was actually extracted
            long amountExtracted = this.extract(amount, transaction);
            if (!simulate) {
                // "Commit" the transaction to make sure the change is applied.
                transaction.commit();
            }
            return amountExtracted;
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        this.amount = tag.getLong("EnergyStored");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putLong("EnergyStored", this.amount);
    }
}
