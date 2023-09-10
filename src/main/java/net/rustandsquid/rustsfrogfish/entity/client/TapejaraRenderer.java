package net.rustandsquid.rustsfrogfish.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.entity.custom.GiganhingaEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.TapejaraEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TapejaraRenderer extends GeoEntityRenderer<TapejaraEntity> {

    public TapejaraRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TapejaraModel());
    }
    @Override
    public RenderType getRenderType(TapejaraEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}