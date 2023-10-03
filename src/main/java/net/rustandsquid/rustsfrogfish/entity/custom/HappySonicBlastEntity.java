package net.rustandsquid.rustsfrogfish.entity.custom;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class HappySonicBlastEntity extends SonicBoom implements IAnimatable {

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}