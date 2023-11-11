package net.rustandsquid.rustsfrogfish.entity.custom;

import com.peeko32213.unusualprehistory.common.entity.EntityDunkleosteus;
import com.peeko32213.unusualprehistory.common.entity.EntityTyrannosaurusRex;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityBaseDinosaurAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;


public class NeilpeartiaEntity extends WaterAnimal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(RustsFrogfish.MOD_ID, "gameplay/frogfishing");


    private long lastSpawnTime = 0;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(NeilpeartiaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> GOLDEN = SynchedEntityData.defineId(NeilpeartiaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DULLED = SynchedEntityData.defineId(NeilpeartiaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> KERMIT = SynchedEntityData.defineId(NeilpeartiaEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FISHINGFROG = SynchedEntityData.defineId(NeilpeartiaEntity.class, EntityDataSerializers.BOOLEAN);


    public NeilpeartiaEntity(EntityType<? extends WaterAnimal> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }



    private final long spawnInterval = 4800;
    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide()) {
            return;
        }

        if (isInWater()) {
            long currentTime = level.getGameTime();
            long timeSinceLastSpawn = currentTime - lastSpawnTime;

            if (timeSinceLastSpawn >= spawnInterval) {
                spawnRandomItems();
                lastSpawnTime = currentTime;
            }
        }
    }

    private void spawnRandomItems() {
        RandomSource randomsource = this.getRandom();

        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.THIS_ENTITY, this).withRandom(randomsource);
        LootTable loottable = this.level.getServer().getLootTables().get(LOOT_TABLE);
        for (ItemStack itemstack : loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.GIFT))) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack));
        }
        this.playSound(SoundEvents.FISHING_BOBBER_SPLASH);

    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.MAX_HEALTH, 16.0D).build();
    }

    public boolean isPushedByFluid() {
        return false;
    }
    public boolean canBreatheUnderwater() {
        return true;
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityDunkleosteus.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityTyrannosaurusRex.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 0.7D, 1));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 15));
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.model.walk"));
            return PlayState.CONTINUE;
        } else
            event.getController().setAnimation(new AnimationBuilder().loop("animation.model.idle"));
        return PlayState.CONTINUE;

    }

   public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
   compound.putInt("Variant", getVariant());
    compound.putBoolean("Dulled", this.isDull());
    compound.putBoolean("Golden", this.isGolden());
       compound.putBoolean("Kermit", this.isKermit());
    }







    public boolean isDull() {
        return this.entityData.get(DULLED).booleanValue();
    }

    public boolean isGolden() {
        return this.entityData.get(GOLDEN).booleanValue();
    }

    public boolean isKermit() {
        return this.entityData.get(KERMIT).booleanValue();
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setVariant(compound.getInt("Variant"));
        this.setDull(compound.getBoolean("Dulled"));
        this.setGolden(compound.getBoolean("Golden"));
        this.setKermit(compound.getBoolean("Kermit"));
    }

    public void setGolden(boolean green) {
        boolean prev = isGolden();
        if (!prev && green) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
            this.setHealth(20.0F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        }
        this.heal(this.getMaxHealth());
        this.entityData.set(GOLDEN, green);
    }

    public void setDull(boolean green) {
        boolean prev = isDull();
        if (!prev && green) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
            this.setHealth(16.0F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        }
        this.heal(this.getMaxHealth());
        this.entityData.set(DULLED, green);
    }

    public void setKermit(boolean green) {
        boolean prev = isKermit();
        if (!prev && green) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
            this.setHealth(300.0F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        }
        this.heal(this.getMaxHealth());
        this.entityData.set(KERMIT, green);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(DULLED, Boolean.valueOf(false));
        this.entityData.define(GOLDEN, Boolean.valueOf(false));
        this.entityData.define(KERMIT, Boolean.valueOf(false));

    }


    public void determineVariant(int variantChange){
        if (variantChange <= 1) {
            this.setKermit(true);
            this.setVariant(2);

        } else if (variantChange <= 50) {
            this.setDull(true);
            this.setVariant(1);

        }else {
            this.setGolden(true);
            this.setVariant(0);
        }

    }


    @javax.annotation.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        int variantChange = this.random.nextInt(0, 100);
        this.determineVariant(variantChange);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private <E extends IAnimatable> PlayState eatPredicate(AnimationEvent<E> event) {
        if (this.spawnInterval == 0) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.model.gulp"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "eatController", 1, this::eatPredicate));

    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.COD_FLOP, 0.15F, 1.0F);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }
}