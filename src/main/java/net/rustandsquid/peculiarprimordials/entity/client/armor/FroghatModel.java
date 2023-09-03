package net.rustandsquid.peculiarprimordials.entity.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.peculiarprimordials.RustsFrogfish;
import net.rustandsquid.peculiarprimordials.entity.custom.Froghat;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FroghatModel extends AnimatedGeoModel<Froghat> {
    @Override
    public ResourceLocation getModelResource(Froghat object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/customarmor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Froghat object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/armor/froghelm.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Froghat animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/armor_animation.json");
    }
}
