package net.rustandsquid.peculiarprimordials.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.peculiarprimordials.RustsFrogfish;
import net.rustandsquid.peculiarprimordials.entity.custom.GiganhingaEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GiganhingaModel extends AnimatedGeoModel<GiganhingaEntity> {
    @Override
    public ResourceLocation getModelResource(GiganhingaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/anhinga.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GiganhingaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/2frogfish.png.json");
    }

    @Override
    public ResourceLocation getAnimationResource(GiganhingaEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/anhinga.animations.json");
    }
}
