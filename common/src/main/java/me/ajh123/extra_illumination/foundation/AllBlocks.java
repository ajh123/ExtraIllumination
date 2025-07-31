package me.ajh123.extra_illumination.foundation;

import dev.architectury.registry.registries.RegistrySupplier;
import me.ajh123.extra_illumination.content.IlluminatedBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static me.ajh123.extra_illumination.foundation.Registration.BLOCKS;

public class AllBlocks {
    public static final RegistrySupplier<Block> ILLUMINATED_BLOCK = registerWithItem("illuminated_block", IlluminatedBlock::new);

    private static RegistrySupplier<Block> registerWithItem(String name, Supplier<Block> block) {
        RegistrySupplier<Block> res =  BLOCKS.register(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(res.get(), new Item.Properties().arch$tab(Registration.MAIN_TAB)));
        return res;
    }

    public static void initialize() {
        // This method is intentionally left empty to ensure that the static initializers are run.
    }
}
