package me.ajh123.extra_illumination.content;

import me.ajh123.extra_illumination.utils.EnergyPlatform;
import me.ajh123.extra_illumination.utils.PlatformEnergyStorage;
import me.ajh123.extra_illumination.foundation.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IlluminatedBlockEntity extends BlockEntity {
    private final PlatformEnergyStorage energyStorage;

    public IlluminatedBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.ILLUMINATED_BLOCK_ENTITY.get(), pos, blockState);
        energyStorage = EnergyPlatform.makeEnergyStorage(10, 10, 10);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.loadAdditional(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        energyStorage.saveAdditional(tag);
    }

    public PlatformEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (!(t instanceof IlluminatedBlockEntity self)) {
            return;
        }

        if (level != null && !level.isClientSide) {
            long amountExtracted = self.energyStorage.extractEnergy(2, false);
            if (amountExtracted > 0) {
                level.setBlock(blockPos, blockState.setValue(IlluminatedBlock.POWERED, true), Block.UPDATE_ALL);
            } else {
                level.setBlock(blockPos, blockState.setValue(IlluminatedBlock.POWERED, false), Block.UPDATE_ALL);
            }
        }
    }
}
