package net.rustandsquid.rustsfrogfish.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.GiganhingaEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GiganhingaModel extends AnimatedGeoModel<GiganhingaEntity> {
    @Override
    public ResourceLocation getModelResource(GiganhingaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/giganhinga.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GiganhingaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/anhingatexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GiganhingaEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/anhinga.animation.json");
    }
}
