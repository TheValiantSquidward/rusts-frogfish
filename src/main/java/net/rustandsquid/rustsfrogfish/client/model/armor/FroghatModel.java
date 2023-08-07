package net.rustandsquid.rustsfrogfish.client.model.armor;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.item.armor.FroghatItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FroghatModel extends AnimatedGeoModel<FroghatItem> {
    @Override
    public ResourceLocation getModelResource(FroghatItem object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/froghat.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(FroghatItem object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/armor/froghelm.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FroghatItem animatable) {
        return null;
    }

}