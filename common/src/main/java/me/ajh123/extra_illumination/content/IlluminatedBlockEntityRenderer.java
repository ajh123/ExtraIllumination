package me.ajh123.extra_illumination.content;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class IlluminatedBlockEntityRenderer implements BlockEntityRenderer<IlluminatedBlockEntity> {
    // 1) Define a custom RenderType that:
    //    - uses POSITION_COLOR shader (no tex coords)
    //    - additive blending
    //    - fullbright/lightmap support
    //    - no culling (so all faces draw)
    private static final RenderType GLOW_LAYER = RenderType.create(
            "extra_illumination:untextured_glow",
            DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(POSITION_COLOR_SHADER)        // correct built-in shader
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY) // we are not using additive blending
                    .setLightmapState(LIGHTMAP)                  // enable lightmap
                    .setCullState(NO_CULL)                       // draw both sides
                    .setWriteMaskState(COLOR_DEPTH_WRITE)        // write color & depth
                    .createCompositeState(false)
    );

    public IlluminatedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Constructor is required, but we don’t need to do anything here.
        // The context provides access to the renderer’s resources if needed.
    }

    @Override
    public void render(IlluminatedBlockEntity blockEntity, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffers,
                       int packedLight, int packedOverlay) {
        boolean POWERED = blockEntity.getBlockState().getValue(IlluminatedBlock.POWERED);

        int brightness = LightTexture.pack(15, 15);

        // glow color
        int argb = blockEntity.getGlowColor().getTextureDiffuseColor();

        int rInt = (argb >> 16) & 0xFF;
        int gInt = (argb >> 8) & 0xFF;
        int bInt = argb & 0xFF;

        float r = rInt / 255f;
        float g = gInt / 255f;
        float b = bInt / 255f;

        if (POWERED) {
            float boost = 1.5f;
            r = Math.min(1.0f, r * boost);
            g = Math.min(1.0f, g * boost);
            b = Math.min(1.0f, b * boost);
        }

        float a = 0.8f;

        if (POWERED) {
            a = 1.0f;
        }

        // tiny offset to push the quad outwards along its normal
        final float zOffset = 0.002f;

        // inset border of 2 pixels (2/16)
        final float inset = 2f / 16f;

        VertexConsumer vc = buffers.getBuffer(GLOW_LAYER);

        PoseStack.Pose ps = poseStack.last();
        Matrix4f mat = ps.pose();

        Vector3f[] normals = {
                new Vector3f( 0,  1,  0), // up
                new Vector3f( 0, -1,  0), // down
                new Vector3f( 0,  0,  1), // north
                new Vector3f( 0,  0, -1), // south
                new Vector3f( 1,  0,  0), // east
                new Vector3f(-1,  0,  0)  // west
        };

        // raw faces as before (0/1)
        float[][] faces = {
                { 0,1,0,   1,1,0,   1,1,1,   0,1,1 }, // up
                { 0,0,1,   1,0,1,   1,0,0,   0,0,0 }, // down
                { 0,0,1,   1,0,1,   1,1,1,   0,1,1 }, // north
                { 1,0,0,   0,0,0,   0,1,0,   1,1,0 }, // south
                { 1,0,1,   1,0,0,   1,1,0,   1,1,1 }, // east
                { 0,0,0,   0,0,1,   0,1,1,   0,1,0 }  // west
        };

        for (int i = 0; i < 6; i++) {
            Vector3f n = normals[i];
            float[] v = faces[i];

            // Determine which axis is the face‐normal axis: 0=X,1=Y,2=Z
            int axis = (Math.abs(n.x()) > 0 ? 0 : (Math.abs(n.y()) > 0 ? 1 : 2));
            // The other two axes to inset:
            int a1 = (axis + 1) % 3, a2 = (axis + 2) % 3;

            for (int q = 0; q < 4; q++) {
                // raw coords
                float rawX = v[q * 3];
                float rawY = v[q * 3 + 1];
                float rawZ = v[q * 3 + 2];
                float[] raw = {rawX, rawY, rawZ};

                // Build final xyz
                float[] pos = new float[3];

                // 1) Face‐plane coordinate: exactly 0 or 1, then push outward
                pos[axis] = (raw[axis] == 0 ? 0f : 1f) + n.get(axis) * zOffset;

                // 2) Inset the in‐plane axes
                pos[a1] = (raw[a1] == 0 ? inset : 1f - inset);
                pos[a2] = (raw[a2] == 0 ? inset : 1f - inset);

                // Emit
                vc.addVertex(mat, pos[0], pos[1], pos[2])
                        .setColor(r, g, b, a)
                        .setOverlay(packedOverlay)
                        .setLight(brightness)
                        .setNormal(n.x(), n.y(), n.z());
            }
        }
    }
}
