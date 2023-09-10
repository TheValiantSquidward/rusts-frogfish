package net.rustandsquid.rustsfrogfish.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.GiganhingaEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.TapejaraEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TapejaraModel extends AnimatedGeoModel<TapejaraEntity> {
    @Override
    public ResourceLocation getModelResource(TapejaraEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/tapejara.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TapejaraEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/tapejara_gilded.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TapejaraEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/tapejara.animation.json");
    }
}
