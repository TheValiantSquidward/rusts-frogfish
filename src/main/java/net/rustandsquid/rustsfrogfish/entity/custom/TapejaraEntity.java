package net.rustandsquid.rustsfrogfish.entity.custom;

import com.peeko32213.unusualprehistory.common.config.UnusualPrehistoryConfig;
import com.peeko32213.unusualprehistory.common.entity.EntityMajungasaurus;
import com.peeko32213.unusualprehistory.common.entity.EntityTyrannosaurusRex;
import com.peeko32213.unusualprehistory.common.entity.msc.util.AnuroPolinateGoal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.FlyingMoveController;
import com.peeko32213.unusualprehistory.core.registry.UPItems;
import com.peeko32213.unusualprehistory.core.registry.UPSounds;
import com.peeko32213.unusualprehistory.core.registry.UPTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.rustandsquid.rustsfrogfish.item.ModItems;
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
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class TapejaraEntity extends AgeableMob implements IAnimatable, NeutralMob {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    private static final EntityDataAccessor<Boolean> FERMENTED = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHEDDING = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> GLOWBERRYCRUSH = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FLYING;
    private static final EntityDataAccessor<Integer> CROPS_POLLINATED;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    private static final UniformInt ALERT_INTERVAL;
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
        this.switchNavigator(true);
    }

    public @org.jetbrains.annotations.Nullable AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.MAX_HEALTH, 16.0D).build();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.0));
        this.goalSelector.addGoal(1, new TapejaraEntity.AIFlyIdle());
        GoalSelector var10000 = this.goalSelector;
        Predicate var10009 = EntitySelector.NO_SPECTATORS;
        Objects.requireNonNull(var10009);
        var10000.addGoal(2, new AvoidEntityGoal(this, EntityMajungasaurus.class, 8.0F, 1.6, 1.4, var10009::test));
        var10000 = this.goalSelector;
        var10009 = EntitySelector.NO_SPECTATORS;
        Objects.requireNonNull(var10009);
        var10000.addGoal(2, new AvoidEntityGoal(this, EntityTyrannosaurusRex.class, 8.0F, 1.6, 1.4, var10009::test));
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }

    }



    public void setFermented(boolean fermented) {
        this.entityData.set(FERMENTED, Boolean.valueOf(fermented));
    }

    public void setShedding(boolean fermented) {
        this.entityData.set(SHEDDING, Boolean.valueOf(fermented));
    }

    public void setGlowberrycrush(boolean fermented) {
        this.entityData.set(GLOWBERRYCRUSH, Boolean.valueOf(fermented));
    }

    public boolean isFermented() {
        return this.entityData.get(FERMENTED).booleanValue();
    }

    public boolean isShedding() {
        return this.entityData.get(SHEDDING).booleanValue();
    }

    public boolean isGlowberrycrush() {
        return this.entityData.get(GLOWBERRYCRUSH).booleanValue();
    }


    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if(hand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;

        if (item == Items.GLISTERING_MELON_SLICE) {
            int size = itemstack.getCount();
            if (!player.isCreative()) {
                itemstack.shrink(1);
                this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
            }
            int brewAmount = 58 + random.nextInt(16);
            if (size > brewAmount) {
                this.fullEffect();
                this.setFermented(true);
            }
            return InteractionResult.SUCCESS;

        }

        if (item == Items.GLOW_BERRIES) {
            int size = itemstack.getCount();
            if (!player.isCreative()) {
                itemstack.shrink(1);
                this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
            }
            int brewAmount = 58 + random.nextInt(16);
            if (size > brewAmount) {
                this.fullEffect();
                this.setGlowberrycrush(true);
            }
            return InteractionResult.SUCCESS;

        }

        if (item == ModItems.PURPLE_BANANA.get()) {
            int size = itemstack.getCount();
            if (!player.isCreative()) {
                itemstack.shrink(1);
                this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
            }
            int brewAmount = 58 + random.nextInt(16);
            if (size > brewAmount) {
                this.fullEffect();
                this.setShedding(true);
            }
            return InteractionResult.SUCCESS;

        }
        if (this.isFermented()) {
            this.spawnAtLocation(ModItems.SEEDY_REMAINS.get());
            this.setFermented(false);
            this.playSound(this.getBurpSound(itemstack), 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (this.isGlowberrycrush()) {
            this.spawnAtLocation(Items.GLOWSTONE_DUST);
            this.setGlowberrycrush(false);
            this.playSound(this.getBurpSound(itemstack), 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (this.isShedding()) {
            this.spawnAtLocation(ModItems.GILDED_CREST.get());
            this.setShedding(false);
            this.playSound(this.getBurpSound(itemstack), 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);

    }

    private void fullEffect() {
        if (this.random.nextInt(6) == 0) {
            double d = this.getX() - (double) this.getBbWidth() * Math.sin(this.yBodyRot * ((float) Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            double e = this.getY() + (double) this.getBbHeight() - 0.3;
            double f = this.getZ() + (double) this.getBbWidth() * Math.cos(this.yBodyRot * ((float) Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            level.addParticle(ParticleTypes.COMPOSTER, true, this.getX(), this.getEyeY() + 0.5F, this.getZ(), 0, 0, 0);
        }
    }

    public SoundEvent getEatingSound(ItemStack p_28540_) {
        return SoundEvents.GENERIC_EAT;
    }

    public SoundEvent getBurpSound(ItemStack p_28540_) {
        return SoundEvents.PLAYER_BURP;
    }



    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, this.level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new FlyingMoveController(this, 0.6F, false, true);
            this.navigation = new FlyingPathNavigation(this, this.level) {
                public boolean isStableDestination(BlockPos pos) {
                    return !this.level.getBlockState(pos.below(2)).isAir();
                }
            };
            this.navigation.setCanFloat(false);
            this.isLandNavigator = false;
        }

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
        this.entityData.define(CROPS_POLLINATED, 0);
        this.entityData.define(FERMENTED, false);
        this.entityData.define(SHEDDING, false);
        this.entityData.define(GLOWBERRYCRUSH, false);
    }

    public void tick() {
        super.tick();
        this.prevFlyProgress = this.flyProgress;
        if (this.isFlying() && this.flyProgress < 5.0F) {
            ++this.flyProgress;
        }

        if (!this.isFlying() && this.flyProgress > 0.0F) {
            --this.flyProgress;
        }

        if (this.ringBufferIndex < 0) {
            for(int i = 0; i < this.ringBuffer.length; ++i) {
                this.ringBuffer[i] = 15.0F;
            }
        }

        if (this.pollinateCooldown > 0) {
            --this.pollinateCooldown;
        }

        ++this.ringBufferIndex;
        if (this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }

        if (!this.level.isClientSide) {
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }

            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }

            if (this.isFlying()) {
                if (this.isFlying() && !this.onGround && !this.isInWaterOrBubble()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6000000238418579, 1.0));
                }

                if (this.isOnGround() && this.timeFlying > 20) {
                    this.setFlying(false);
                }

                ++this.timeFlying;
            } else {
                this.timeFlying = 0;
            }
        }

    }

    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        return prev;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || super.isInvulnerableTo(source);
    }

    public boolean isFlying() {
        return (Boolean)this.entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (!flying || !this.isBaby()) {
            this.entityData.set(FLYING, flying);
        }
    }

    public int getCropsPollinated() {
        return (Integer)this.entityData.get(CROPS_POLLINATED);
    }

    public void setCropsPollinated(int crops) {
        this.entityData.set(CROPS_POLLINATED, crops);
    }

    public boolean canBlockBeSeen(BlockPos pos) {
        double x = (double)((float)pos.getX() + 0.5F);
        double y = (double)((float)pos.getY() + 0.5F);
        double z = (double)((float)pos.getZ() + 0.5F);
        HitResult result = this.level.clip(new ClipContext(new Vec3(this.getX(), this.getY() + (double)this.getEyeHeight(), this.getZ()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("CropsPollinated", this.getCropsPollinated());
        compound.putInt("PollinateCooldown", this.pollinateCooldown);
        compound.putBoolean("Fermented", this.isFermented());
        compound.putBoolean("Shedding", this.isShedding());
        compound.putBoolean("Glowberrycrush", this.isGlowberrycrush());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setCropsPollinated(compound.getInt("CropsPollinated"));
        this.pollinateCooldown = compound.getInt("PollinateCooldown");
        this.setFermented(compound.getBoolean("Fermented"));
        this.setShedding(compound.getBoolean("Shedding"));
        this.setGlowberrycrush(compound.getBoolean("Glowberrycrush"));
    }

    protected SoundEvent getAmbientSound() {
        return (SoundEvent)UPSounds.ANURO_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return (SoundEvent)UPSounds.ANURO_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return (SoundEvent)UPSounds.ANURO_DEATH.get();
    }

    public void killed(ServerLevel world, LivingEntity entity) {
        this.heal(10.0F);
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = (float)(10 + this.getRandom().nextInt(15));
        float neg = this.getRandom().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.yBodyRot;
        float angle = 0.017453292F * renderYawOffset + 3.15F + this.getRandom().nextFloat() * neg;
        double extraX = (double)(radius * Mth.sin((float)(Math.PI + (double)angle)));
        double extraZ = (double)(radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos(fleePos.x() + extraX, this.getY(), fleePos.z() + extraZ);
        BlockPos ground = this.getAnuroGround(radialPos);
        if (ground.getY() < -64) {
            return null;
        } else {
            for(ground = this.blockPosition(); ground.getY() > -64 && !this.level.getBlockState(ground).getMaterial().isSolidBlocking(); ground = ground.below()) {
            }

            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground.below()) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        return this.level.clip(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5.0F + radiusAdd + (float)this.getRandom().nextInt(5);
        float neg = this.getRandom().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.yBodyRot;
        float angle = 0.017453292F * renderYawOffset + 3.15F + this.getRandom().nextFloat() * neg;
        double extraX = (double)(radius * Mth.sin((float)(Math.PI + (double)angle)));
        double extraZ = (double)(radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos(fleePos.x() + extraX, 0.0, fleePos.z() + extraZ);
        BlockPos ground = this.getAnuroGround(radialPos);
        int distFromGround = (int)this.getY() - ground.getY();
        int flightHeight = 5 + this.getRandom().nextInt(5);
        int j = this.getRandom().nextInt(5) + 5;
        BlockPos newPos = ground.above(distFromGround > 5 ? flightHeight : j);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.distanceToSqr(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position;
        for(position = this.blockPosition(); position.getY() > -65 && this.level.isEmptyBlock(position); position = position.below()) {
        }

        return !this.level.getFluidState(position).isEmpty() || this.level.getBlockState(position).is(Blocks.VINE) || position.getY() <= -65;
    }

    public BlockPos getAnuroGround(BlockPos in) {
        BlockPos position;
        for(position = new BlockPos((double)in.getX(), this.getY(), (double)in.getZ()); position.getY() < 320 && !this.level.getFluidState(position).isEmpty(); position = position.above()) {
        }

        while(position.getY() > -64 && !this.level.getBlockState(position).getMaterial().isSolidBlocking() && this.level.getFluidState(position).isEmpty()) {
            position = position.below();
        }

        return position;
    }

    public boolean doHurtTarget(Entity target) {
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity livingEntity) {
            damage += livingEntity.getMobType().equals(MobType.ARTHROPOD) ? damage : 0.0F;
            knockback += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean shouldHurt;
        if (shouldHurt = target.hurt(DamageSource.mobAttack(this), damage)) {
            if (knockback > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback((double)(knockback * 0.5F), (double)Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }

        return shouldHurt;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && this.isOnGround() && this.onGround) {
            event.getController().setAnimation((new AnimationBuilder()).loop("animation.model.new5"));
            event.getController().setAnimationSpeed(1.05);
            return PlayState.CONTINUE;
        } else if (!event.isMoving() && this.isOnGround() && this.onGround) {
            event.getController().setAnimation((new AnimationBuilder()).loop("animation.model.new4"));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation((new AnimationBuilder()).loop("animation.model.new"));
            return PlayState.CONTINUE;
        }
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation((new AnimationBuilder()).playOnce("animation.anuro.bite"));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5.0);
        AnimationController<TapejaraEntity> controller = new AnimationController(this, "controller", 5.0F, this::predicate);
        data.addAnimationController(new AnimationController(this, "attackController", 0.0F, this::attackPredicate));
        data.addAnimationController(controller);
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setRemainingPersistentAngerTime(int p_21673_) {
        this.remainingPersistentAngerTime = p_21673_;
    }

    public @org.jetbrains.annotations.Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@org.jetbrains.annotations.Nullable UUID p_21672_) {
        this.persistentAngerTarget = p_21672_;
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasCustomName();
    }

    public boolean removeWhenFarAway(double d) {
        return !this.hasCustomName();
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_28134_, DifficultyInstance p_28135_, MobSpawnType p_28136_, @javax.annotation.Nullable SpawnGroupData p_28137_, @javax.annotation.Nullable CompoundTag p_28138_) {
        p_28137_ = super.finalizeSpawn(p_28134_, p_28135_, p_28136_, p_28137_, p_28138_);
        Level level = p_28134_.getLevel();
        if (level instanceof ServerLevel) {
            this.setPersistenceRequired();
        }

        return p_28137_;
    }

    public static boolean checkSurfaceDinoSpawnRules(EntityType<? extends TapejaraEntity> p_186238_, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource p_186242_) {
        return level.getBlockState(pos.below()).is(UPTags.DINO_NATURAL_SPAWNABLE) && (Boolean)UnusualPrehistoryConfig.DINO_NATURAL_SPAWNING.get();
    }

    static {
        FLYING = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.BOOLEAN);
        CROPS_POLLINATED = SynchedEntityData.defineId(TapejaraEntity.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
        ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    }

    private class AIFlyIdle extends Goal {
        protected double x;
        protected double y;
        protected double z;

        public AIFlyIdle() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (!TapejaraEntity.this.isVehicle() && (TapejaraEntity.this.getTarget() == null || !TapejaraEntity.this.getTarget().isAlive()) && !TapejaraEntity.this.isPassenger()) {
                if (TapejaraEntity.this.getRandom().nextInt(45) != 0 && !TapejaraEntity.this.isFlying()) {
                    return false;
                } else {
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
            } else {
                return false;
            }
        }

        public void tick() {
            TapejaraEntity.this.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0);
            if (TapejaraEntity.this.isFlying() && TapejaraEntity.this.onGround && TapejaraEntity.this.timeFlying > 10) {
                TapejaraEntity.this.setFlying(false);
            }

        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = TapejaraEntity.this.position();
            return TapejaraEntity.this.timeFlying >= 200 && !TapejaraEntity.this.isOverWaterOrVoid() ? TapejaraEntity.this.getBlockGrounding(vector3d) : TapejaraEntity.this.getBlockInViewAway(vector3d, 0.0F);
        }

        public boolean canContinueToUse() {
            return TapejaraEntity.this.isFlying() && TapejaraEntity.this.distanceToSqr(this.x, this.y, this.z) > 5.0;
        }

        public void start() {
            TapejaraEntity.this.setFlying(true);
            TapejaraEntity.this.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0);
        }

        public void stop() {
            TapejaraEntity.this.getNavigation().stop();
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            super.stop();
        }
    }

}
