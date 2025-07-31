package me.ajh123.extra_illumination.foundation;

import dev.architectury.registry.registries.RegistrySupplier;
import me.ajh123.extra_illumination.content.IlluminatedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AllBlockEntities {
    public static final RegistrySupplier<BlockEntityType<IlluminatedBlockEntity>> ILLUMINATED_BLOCK_ENTITY = Registration.BLOCK_ENTITIES.register(
            "illuminated_block",
            () -> BlockEntityType.Builder.of(IlluminatedBlockEntity::new, AllBlocks.ILLUMINATED_BLOCK.get()).build(null));

    public static void initialize() {
        // This method is intentionally left empty to ensure that the static initializers are run.
    }
}
