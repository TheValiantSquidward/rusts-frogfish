package net.rustandsquid.rustsfrogfish.entity.client;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.GiganhingaEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.TapejaraEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TapejaraModel extends AnimatedGeoModel<TapejaraEntity> {
    private static final ResourceLocation TEXTURE_GILDED = new ResourceLocation(RustsFrogfish .MOD_ID, "textures/entity/tapejara_gilded.png");
    private static final ResourceLocation TEXTURE_ELEGANT = new ResourceLocation(RustsFrogfish .MOD_ID, "textures/entity/tapejara_elegant.png");
    private static final ResourceLocation TEXTURE_FLAMBOYANT = new ResourceLocation(RustsFrogfish .MOD_ID,"textures/entity/tapejara_flamboyant.png");
    private ConfigSpec entityData;




    public ResourceLocation getTextureResource(TapejaraEntity object)
    {
        return switch (object.getVariant()) {
            case 1 -> TEXTURE_FLAMBOYANT;
            case 2 -> TEXTURE_ELEGANT;
            default -> TEXTURE_GILDED;
        };
    }







    @Override
    public ResourceLocation getModelResource(TapejaraEntity object) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/tapejara.geo.json");
    }



    @Override
    public ResourceLocation getAnimationResource(TapejaraEntity animatable) {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/tapejara.animation.json");
    }
}
