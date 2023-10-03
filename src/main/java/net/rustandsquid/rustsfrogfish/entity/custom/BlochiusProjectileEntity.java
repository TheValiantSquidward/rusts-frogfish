package net.rustandsquid.rustsfrogfish.entity.custom;

import com.peeko32213.unusualprehistory.common.entity.msc.util.ranged.BetterAbstractHurtingProjectile;
import com.peeko32213.unusualprehistory.common.entity.msc.util.ranged.RangedMeleeMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.rustandsquid.rustsfrogfish.entity.ModEntityTypes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BlochiusProjectileEntity extends BetterAbstractHurtingProjectile implements IAnimatable {


    
    protected BlochiusProjectileEntity(EntityType<? extends BlochiusProjectileEntity> p_i50160_1_, Level p_i50160_2_) {
        super(p_i50160_1_, p_i50160_2_);
    }

    protected int timeInAir;
    protected boolean inAir;
    private int ticksInAir;
    
    //public BlochiusProjectileEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ,
   //                                   float directHitDamage) {
        //super(ModEntityTypes.BLOCHIUSBLAST.get(), shooter, accelX, accelY, accelZ, worldIn);
      //  this.directHitDamage = directHitDamage;
    //}

    //public BlochiusProjectileEntity(net.minecraft.world.level.Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
  //      super(ModEntityTypes.BLOCHIUSBLAST.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    //}

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.ticksInAir = 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("life", (short) this.ticksInAir);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.ticksInAir = compound.getShort("life");
    }

    private float directHitDamage = 5;

    public void setDirectHitDamage(float directHitDamage) {
        this.directHitDamage = directHitDamage;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level.isClientSide
                || (entity == null || entity.isAlive()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != HitResult.Type.MISS
                    && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            this.checkInsideBlocks();
            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale((double) f));
            this.setPos(d0, d1, d2);
        } else {
            this.remove(RemovalReason.KILLED);
        }
    }



    protected boolean shouldBurn() {
        return false;
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isNoGravity() {
        if (this.isInWater()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            Entity entity1 = this.getOwner();
            if (!(entity instanceof RangedMeleeMob))
                entity.hurt(DamageSource.mobAttack((LivingEntity) entity1), directHitDamage);
            this.remove(RemovalReason.KILLED);
            if (entity1 instanceof LivingEntity) {
                if (!(entity instanceof RangedMeleeMob))
                    this.doEnchantDamageEffects((LivingEntity) entity1, entity);
            }
        }
    }


    @Override
    public void registerControllers(AnimationData data) {
        
    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}
