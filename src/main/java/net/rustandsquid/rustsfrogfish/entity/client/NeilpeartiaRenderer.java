package net.rustandsquid.rustsfrogfish.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NeilpeartiaRenderer extends GeoEntityRenderer<NeilpeartiaEntity> {

    public NeilpeartiaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NeilpeartiaModel());
    }

    @Override
    public ResourceLocation getTextureLocation(NeilpeartiaEntity instance) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/frogfish.png");
    }

    public ResourceLocation getKermitTextureLocation(NeilpeartiaEntity instance) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/kermit.png");
    }


    //@Override
    @Override
    public RenderType getRenderType(NeilpeartiaEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}