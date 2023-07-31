package net.rustandsquid.rustsfrogfish.entity.client;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NeilpeartiaModel extends AnimatedGeoModel<NeilpeartiaEntity> {
    private static final ResourceLocation TEXTURE_DULLED = new ResourceLocation("rustsfrogfish:textures/entity/2frogfish");
    private static final ResourceLocation TEXTURE_GOLDEN = new ResourceLocation("rustsfrogfish:textures/entity/frogfish");
    private ConfigSpec entityData;

    public ResourceLocation getTextureResource(NeilpeartiaEntity object)
    {
        return switch (object.getVariant()) {
            case 1 -> TEXTURE_GOLDEN;
            default -> TEXTURE_DULLED;
        };
    }







    @Override
    public ResourceLocation getModelResource(NeilpeartiaEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/frogfish.geo.json");
    }



    @Override
    public ResourceLocation getAnimationResource(NeilpeartiaEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/model.animation.json");
    }
}
