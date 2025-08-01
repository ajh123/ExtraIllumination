package me.ajh123.extra_illumination.content;

import me.ajh123.extra_illumination.foundation.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class IlluminatedBlock extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public IlluminatedBlock() {
        super(BlockBehaviour.Properties.of().lightLevel(litBlockEmission()).strength(0.3f).sound(SoundType.GLASS));
        this.registerDefaultState(stateDefinition.any()
                .setValue(POWERED, false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IlluminatedBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == AllBlockEntities.ILLUMINATED_BLOCK_ENTITY.get() ? IlluminatedBlockEntity::tick : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (stack.isEmpty()) {
            return ItemInteractionResult.FAIL;
        }

        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IlluminatedBlockEntity illuminatedBlockEntity) {
                illuminatedBlockEntity.setGlowColor(dyeColor);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.CONSUME;
    }

    private static ToIntFunction<BlockState> litBlockEmission() {
        return blockState -> blockState.getValue(BlockStateProperties.POWERED) ? 15 : 0;
    }
}
