package me.ajh123.extra_illumination.foundation;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.ajh123.extra_illumination.ExtraIllumination;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class Registration {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ExtraIllumination.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ExtraIllumination.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ExtraIllumination.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ExtraIllumination.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<CreativeModeTab> MAIN_TAB = TABS.register("main_tab", () ->
            CreativeTabRegistry.create(Component.translatable("category.extra_illumination.main"),
                    () -> new ItemStack(AllBlocks.ILLUMINATED_BLOCK.get())));

    public static void initialize() {
        TABS.register();
        AllBlocks.initialize();
        BLOCKS.register();
        ITEMS.register();
        AllBlockEntities.initialize();
        BLOCK_ENTITIES.register();
    }
}
