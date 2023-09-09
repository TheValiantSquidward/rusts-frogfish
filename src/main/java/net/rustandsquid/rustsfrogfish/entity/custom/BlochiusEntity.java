package net.rustandsquid.rustsfrogfish.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.rustandsquid.rustsfrogfish.sound.ModSounds;

public class BlochiusEntity extends AbstractFish {
    public BlochiusEntity(EntityType<? extends AbstractFish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return null;
    }
}
