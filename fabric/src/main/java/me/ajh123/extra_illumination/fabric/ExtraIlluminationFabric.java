package me.ajh123.extra_illumination.fabric;

import me.ajh123.extra_illumination.ExtraIllumination;
import me.ajh123.extra_illumination.content.IlluminatedBlockEntity;
import me.ajh123.extra_illumination.foundation.AllBlockEntities;
import net.fabricmc.api.ModInitializer;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public final class ExtraIlluminationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ExtraIllumination.init();

        // Register the IlluminatedBlockEntity with the EnergyStorage API.
        EnergyStorage.SIDED.registerForBlockEntity(
                (myBlockEntity, direction) -> (SimpleEnergyStorage) myBlockEntity.getEnergyStorage(),
                AllBlockEntities.ILLUMINATED_BLOCK_ENTITY.get()
        );
    }
}
