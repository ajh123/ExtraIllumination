package me.ajh123.extra_illumination.content;

import dev.architectury.platform.Platform;
import me.ajh123.extra_illumination.utils.EnergyPlatform;
import me.ajh123.extra_illumination.utils.PlatformEnergyStorage;
import me.ajh123.extra_illumination.foundation.AllBlockEntities;
import net.createmod.catnip.animation.LerpedFloat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class IlluminatedBlockEntity extends BlockEntity {
    private final PlatformEnergyStorage energyStorage;
    private DyeColor glowColor = DyeColor.WHITE;

    public static final float GLOW_BASE_INTENSITY = 0.25f; // Base intensity for the glow effect

    @Environment(EnvType.CLIENT)
    private final LerpedFloat glow = LerpedFloat.linear();

    public IlluminatedBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.ILLUMINATED_BLOCK_ENTITY.get(), pos, blockState);
        energyStorage = EnergyPlatform.makeEnergyStorage(10, 10, 10);

        if (Platform.getEnv() == EnvType.CLIENT) {
            glow.startWithValue(0);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.loadAdditional(tag);
        if (tag.contains("GlowColor")) {
            glowColor = DyeColor.byId(tag.getInt("GlowColor"));
        } else {
            glowColor = DyeColor.WHITE; // Default color if not set
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        energyStorage.saveAdditional(tag);
        tag.putInt("GlowColor", glowColor.getId());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        energyStorage.saveAdditional(tag);
        tag.putInt("GlowColor", glowColor.getId());
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public PlatformEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public DyeColor getGlowColor() {
        return glowColor;
    }

    @Environment(EnvType.CLIENT)
    public LerpedFloat getGlow() {
        return glow;
    }

    public void setGlowColor(DyeColor glowColor) {
        this.glowColor = glowColor;
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            this.setChanged();
        }
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

        if (level != null && level.isClientSide) {
            if (self.getBlockState().getValue(IlluminatedBlock.POWERED)) {
                self.glow.chase(1,0.02, LerpedFloat.Chaser.EXP);
                self.glow.tickChaser();
            } else {
                self.glow.chase(0, 0.4, LerpedFloat.Chaser.EXP);
                self.glow.tickChaser();
            }
        }
    }
}
