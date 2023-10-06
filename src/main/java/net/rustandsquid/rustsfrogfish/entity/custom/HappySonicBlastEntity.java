package net.rustandsquid.rustsfrogfish.entity.custom;
import com.peeko32213.unusualprehistory.common.entity.msc.util.ranged.BetterAbstractHurtingProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class HappySonicBlastEntity extends BetterAbstractHurtingProjectile implements IAnimatable {

    public HappySonicBlastEntity(EntityType<? extends BetterAbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}