package net.rustandsquid.rustsfrogfish.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.BlochiusEntity;
import net.rustandsquid.rustsfrogfish.entity.custom.GiganhingaEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BlochiusModel extends AnimatedGeoModel<BlochiusEntity> {
    @Override
    public ResourceLocation getModelResource(BlochiusEntity object)
    {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "geo/blochius.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlochiusEntity object)
    {
        if (object.isGolden()) {
            return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/needlefish.png");
        }
        return new ResourceLocation(RustsFrogfish.MOD_ID, "textures/entity/needlefish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlochiusEntity object)
    {
        return new ResourceLocation(RustsFrogfish.MOD_ID, "animations/blochius.animation.json");
    }

    @Override
    public void setCustomAnimations(BlochiusEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone body = this.getAnimationProcessor().getBone("body");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (!entity.isInWater() && !entity.isFromBook()) {
            body.setRotationZ(1.5708f);
        }
    }

}
