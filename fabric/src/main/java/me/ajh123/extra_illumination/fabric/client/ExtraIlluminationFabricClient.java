package me.ajh123.extra_illumination.fabric.client;

import me.ajh123.extra_illumination.foundation.AllBlockEntities;
import me.ajh123.extra_illumination.content.IlluminatedBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public final class ExtraIlluminationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRenderers.register(
                AllBlockEntities.ILLUMINATED_BLOCK_ENTITY.get(),
                IlluminatedBlockEntityRenderer::new
        );
    }
}
