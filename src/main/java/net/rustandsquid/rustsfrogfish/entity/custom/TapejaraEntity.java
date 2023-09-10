package net.rustandsquid.rustsfrogfish.entity.custom;

import com.peeko32213.unusualprehistory.common.config.UnusualPrehistoryConfig;
import com.peeko32213.unusualprehistory.common.entity.EntityMajungasaurus;
import com.peeko32213.unusualprehistory.common.entity.EntityTyrannosaurusRex;
import com.peeko32213.unusualprehistory.common.entity.msc.util.AnuroPolinateGoal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.FlyingMoveController;
import com.peeko32213.unusualprehistory.core.registry.UPSounds;
import com.peeko32213.unusualprehistory.core.registry.UPTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;
import java.util.logging.Level;

public class TapejaraEntity extends AgeableMob implements IAnimatable, NeutralMob {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CROPS_POLLINATED = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BOOK = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    private static final int ALERT_RANGE_Y = 10;
    private int remainingPersistentAngerTime;
    private boolean isSchool = true;
    private int ticksUntilNextAlert;
    public int pollinateCooldown = 0;
    public final float[] ringBuffer = new float[64];
    public float prevFlyProgress;
    public float flyProgress;
    public int ringBufferIndex = -1;
    private boolean isLandNavigator;
    private int timeFlying;

    public TapejaraEntity(EntityType<? extends AgeableMob> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
        switchNavigator(true);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0f).build();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1D));
        this.goalSelector.addGoal(1, new AIFlyIdle());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityMajungasaurus.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityTyrannosaurusRex.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false, false, entity -> entity.getType().is(UPTags.ANURO_TARGETS)));
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }


    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new FlyingMoveController(this, 0.6F, false, true);
            this.navigation = new FlyingPathNavigation(this, level) {
                public boolean isStableDestination(BlockPos pos) {
                    return !this.level.getBlockState(pos.below(2)).isAir();
                }
            };
            navigation.setCanFloat(false);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
        this.entityData.define(CROPS_POLLINATED, 0);
        this.entityData.define(FROM_BOOK, false);

    }



    public void tick() {
        super.tick();
        this.prevFlyProgress = flyProgress;
        if (this.isFlying() && flyProgress < 5F) {
            flyProgress++;
        }
        if (!this.isFlying() && flyProgress > 0F) {
            flyProgress--;
        }
        if (this.ringBufferIndex < 0) {
            //initial population of buffer
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                this.ringBuffer[i] = 15;
            }
        }
        if(pollinateCooldown > 0){
            pollinateCooldown--;
        }
        this.ringBufferIndex++;
        if (this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }
        if (!level.isClientSide) {
            if (isFlying() && this.isLandNavigator) {
                switchNavigator(false);
            }
            if (!isFlying() && !this.isLandNavigator) {
                switchNavigator(true);
            }
            if (this.isFlying()) {
                if (this.isFlying() && !this.onGround) {
                    if (!this.isInWaterOrBubble()) {
                        this.setDeltaMovement(this.getDeltaMovement().multiply(1F, 0.6F, 1F));
                    }
                }
                if (this.isOnGround() && timeFlying > 20) {
                    this.setFlying(false);
                }
                this.timeFlying++;
            } else {
                this.timeFlying = 0;
            }
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        return prev;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || super.isInvulnerableTo(source);
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && isBaby()) {
            return;
        }
        this.entityData.set(FLYING, flying);
    }

    public int getCropsPollinated() {
        return this.entityData.get(CROPS_POLLINATED).intValue();
    }

    public void setCropsPollinated(int crops) {
        this.entityData.set(CROPS_POLLINATED, Integer.valueOf(crops));
    }

    public boolean canBlockBeSeen(BlockPos pos) {
        double x = pos.getX() + 0.5F;
        double y = pos.getY() + 0.5F;
        double z = pos.getZ() + 0.5F;
        HitResult result = this.level.clip(new ClipContext(new Vec3(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0D || result.getType() == HitResult.Type.MISS;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("CropsPollinated", this.getCropsPollinated());
        compound.putInt("PollinateCooldown", this.pollinateCooldown);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setCropsPollinated(compound.getInt("CropsPollinated"));
        this.pollinateCooldown = compound.getInt("PollinateCooldown");
    }

    protected SoundEvent getAmbientSound() {
        return UPSounds.ANURO_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return UPSounds.ANURO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return UPSounds.ANURO_DEATH.get();
    }

    public void killed(ServerLevel world, LivingEntity entity) {
        this.heal(10);
    }


    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = 1 + this.getRandom().nextInt(15);
        float neg = this.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (this.getRandom().nextFloat() * neg);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = new BlockPos(fleePos.x() + extraX, getY(), fleePos.z() + extraZ);
        BlockPos ground = this.getAnuroGround(radialPos);
        if (ground.getY() < -64) {
            return null;
        } else {
            ground = this.blockPosition();
            while (ground.getY() > -64 && !level.getBlockState(ground).getMaterial().isSolidBlocking()) {
                ground = ground.below();
            }
        }
        if (!this.isTargetBlocked(Vec3.atCenterOf(ground.above()))) {
            return Vec3.atCenterOf(ground.below());
        }
        return null;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());

        return this.level.clip(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5 + radiusAdd + this.getRandom().nextInt(5);
        float neg = this.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (this.getRandom().nextFloat() * neg);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = new BlockPos(fleePos.x() + extraX, 0, fleePos.z() + extraZ);
        BlockPos ground = getAnuroGround(radialPos);
        int distFromGround = (int) this.getY() - ground.getY();
        int flightHeight = 5 + this.getRandom().nextInt(5);
        int j = this.getRandom().nextInt(5) + 5;

        BlockPos newPos = ground.above(distFromGround > 5 ? flightHeight : j);
        if (!this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.distanceToSqr(Vec3.atCenterOf(newPos)) > 1) {
            return Vec3.atCenterOf(newPos);
        }
        return null;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.blockPosition();
        while (position.getY() > -65 && level.isEmptyBlock(position)) {
            position = position.below();
        }
        return !level.getFluidState(position).isEmpty() || level.getBlockState(position).is(Blocks.VINE) || position.getY() <= -65;
    }

    public BlockPos getAnuroGround(BlockPos in) {
        BlockPos position = new BlockPos(in.getX(), this.getY(), in.getZ());
        while (position.getY() < 320 && !level.getFluidState(position).isEmpty()) {
            position = position.above();
        }
        while (position.getY() > -64 && !level.getBlockState(position).getMaterial().isSolidBlocking() && level.getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }


    @Override
    public boolean doHurtTarget(Entity target) {
        boolean shouldHurt;
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity livingEntity) {
            damage += livingEntity.getMobType().equals(MobType.ARTHROPOD) ? damage : 0;
            knockback += (float) EnchantmentHelper.getKnockbackBonus(this);
        }
        if (shouldHurt = target.hurt(DamageSource.mobAttack(this), damage)) {
            if (knockback > 0.0f && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback(knockback * 0.5f, Mth.sin(this.getYRot() * ((float)Math.PI / 180)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }
        return shouldHurt;
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.isFromBook()){
            return PlayState.CONTINUE;
        }
        if (event.isMoving() && this.isOnGround() && this.onGround) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.anuro.walk"));
            event.getController().setAnimationSpeed(1.05);
            return PlayState.CONTINUE;
        }
        if (!event.isMoving() && this.isOnGround() && this.onGround) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.anuro.idle"));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().loop("animation.anuro.fly"));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.anuro.bite"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        AnimationController<TapejaraEntity> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
        data.addAnimationController(controller);
    }

    private class AIFlyIdle extends Goal {
        protected double x;
        protected double y;
        protected double z;

        public AIFlyIdle() {
            super();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (TapejaraEntity.this.isVehicle() || (TapejaraEntity.this.getTarget() != null && TapejaraEntity.this.getTarget().isAlive()) || TapejaraEntity.this.isPassenger()) {
                return false;
            } else {
                if (TapejaraEntity.this.getRandom().nextInt(45) != 0 && !TapejaraEntity.this.isFlying()) {
                    return false;
                }

                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            }
        }

        public void tick() {
            TapejaraEntity.this.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1F);
            if (isFlying() && TapejaraEntity.this.onGround && TapejaraEntity.this.timeFlying > 10) {
                TapejaraEntity.this.setFlying(false);
            }
        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = TapejaraEntity.this.position();
            if (TapejaraEntity.this.timeFlying < 200 || TapejaraEntity.this.isOverWaterOrVoid()) {
                return TapejaraEntity.this.getBlockInViewAway(vector3d, 0);
            } else {
                return TapejaraEntity.this.getBlockGrounding(vector3d);
            }
        }

        public boolean canContinueToUse() {
            return TapejaraEntity.this.isFlying() && TapejaraEntity.this.distanceToSqr(x, y, z) > 5F;
        }

        public void start() {
            TapejaraEntity.this.setFlying(true);
            TapejaraEntity.this.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1F);
        }

        public void stop() {
            TapejaraEntity.this.getNavigation().stop();
            x = 0;
            y = 0;
            z = 0;
            super.stop();
        }

    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int p_21673_) {
        this.remainingPersistentAngerTime = p_21673_;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@org.jetbrains.annotations.Nullable UUID p_21672_) {
        this.persistentAngerTarget = p_21672_;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public boolean isFromBook() {
        return this.entityData.get(FROM_BOOK).booleanValue();
    }
    public void setIsFromBook(boolean fromBook) {
        this.entityData.set(FROM_BOOK, fromBook);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasCustomName();
    }

    public boolean removeWhenFarAway(double d) {
        return !this.hasCustomName();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_28134_, DifficultyInstance p_28135_, MobSpawnType p_28136_, @Nullable SpawnGroupData p_28137_, @Nullable CompoundTag p_28138_) {
        p_28137_ = super.finalizeSpawn(p_28134_, p_28135_, p_28136_, p_28137_, p_28138_);
        ServerLevel level = p_28134_.getLevel();
        if (level instanceof ServerLevel) {
            {
                this.setPersistenceRequired();
            }
        }
        return p_28137_;
    }

}
