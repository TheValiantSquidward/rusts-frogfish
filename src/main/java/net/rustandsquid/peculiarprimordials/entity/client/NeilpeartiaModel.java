package net.rustandsquid.peculiarprimordials.entity.client;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.peculiarprimordials.RustsFrogfish;
import net.rustandsquid.peculiarprimordials.entity.custom.NeilpeartiaEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NeilpeartiaModel extends AnimatedGeoModel<NeilpeartiaEntity> {
    private static final ResourceLocation TEXTURE_DULLED = new ResourceLocation(RustsFrogfish .MOD_ID, "textures/entity/2frogfish.png");
    private static final ResourceLocation TEXTURE_KERMIT = new ResourceLocation(RustsFrogfish .MOD_ID, "textures/entity/kermit.png");
    private static final ResourceLocation TEXTURE_GOLDEN = new ResourceLocation(RustsFrogfish .MOD_ID,"textures/entity/frogfish.png");
    private ConfigSpec entityData;




    public ResourceLocation getTextureResource(NeilpeartiaEntity object)
    {
        return switch (object.getVariant()) {
            case 1 -> TEXTURE_GOLDEN;
            case 2 -> TEXTURE_KERMIT;
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
