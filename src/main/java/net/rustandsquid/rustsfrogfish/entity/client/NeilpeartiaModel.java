package net.rustandsquid.rustsfrogfish.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NeilpeartiaModel extends AnimatedGeoModel<NeilpeartiaEntity> {
    @Override
    public ResourceLocation getModelResource(NeilpeartiaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/frogfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NeilpeartiaEntity object) {
        return NeilpeartiaRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }


    @Override
    public ResourceLocation getAnimationResource(NeilpeartiaEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/model.animation.json");
    }
}
