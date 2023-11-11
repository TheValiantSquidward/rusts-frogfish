package net.rustandsquid.rustsfrogfish.entity.custom;

import com.peeko32213.unusualprehistory.common.entity.EntityDunkleosteus;
import com.peeko32213.unusualprehistory.common.entity.EntityTyrannosaurusRex;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityBaseDinosaurAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GiganhingaEntity extends EntityBaseDinosaurAnimal implements IAnimatable {
    public GiganhingaEntity(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(RustsFrogfish.MOD_ID, "gameplay/anhingaegglay");
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int rideCooldown = 0;
    public int soundTimer = 0;
    public int eggTime = this.random.nextInt(6000) + 6000;
    public boolean isRiding = false;
    public boolean entitySpawn = false;

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0f).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityDunkleosteus.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, EntityTyrannosaurusRex.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.7D, 17));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Squid.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Drowned.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Cod.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Salmon.class, true));
    }

    @Override
    protected SoundEvent getAttackSound() {
        return null;
    }

    @Override
    protected int getKillHealAmount() {
        return 0;
    }

    @Override
    protected boolean canGetHungry() {
        return false;
    }

    @Override
    protected boolean hasTargets() {
        return false;
    }

    @Override
    protected boolean hasAvoidEntity() {
        return false;
    }

    @Override
    protected boolean hasCustomNavigation() {
        return false;
    }

    @Override
    protected boolean hasMakeStuckInBlock() {
        return false;
    }

    @Override
    protected boolean customMakeStuckInBlockCheck(BlockState blockState) {
        return false;
    }

    @Override
    protected TagKey<EntityType<?>> getTargetTag() {
        return null;
    }


    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.isAlive() && --this.eggTime <= 0) {
            spawnRandomItems();
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
    }

    private void spawnRandomItems() {
        RandomSource randomsource = this.getRandom();

        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.THIS_ENTITY, this).withRandom(randomsource);
        LootTable loottable = this.level.getServer().getLootTables().get(LOOT_TABLE);
        for (ItemStack itemstack : loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.GIFT))) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack));
        }
        this.playSound(SoundEvents.CHICKEN_EGG);

    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && !this.isSwimming()) {
            {
                event.getController().setAnimation(new AnimationBuilder().loop("animation.model.walk"));
                return PlayState.CONTINUE;
            }
        }
        if (this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.model.floatswim"));
            event.getController().setAnimationSpeed(1.0F);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.model.idle"));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }


    public void rideTick() {
        Entity mount = this.getVehicle();

        if (this.isPassenger() && !mount.isAlive()) {
            this.stopRiding();
        } else if (mount instanceof Player player && this.isPassenger()) {
            this.setDeltaMovement(0, 0, 0);
            float radius = 0F;
            float angle = (0.01745329251F * (player.yBodyRot - 180F));
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            playPanicSound();
            this.setPos(player.getX() + extraX, Math.max(player.getY() + player.getBbHeight() + 0.1, player.getY()), player.getZ() + extraZ);
            if (!player.isAlive() || rideCooldown == 0 || player.isShiftKeyDown()) {
                this.stopRiding();
            }
        } else {
            super.rideTick();
        }

    }

    private void playPanicSound() {
        if (this.soundTimer <= 0) {
            this.playSound(SoundEvents.CHICKEN_HURT, this.getSoundVolume(), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);
            soundTimer = 80;
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.IN_WALL || super.isInvulnerableTo(source);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack itemstack2 = player.getItemInHand(InteractionHand.OFF_HAND);
        Item item = itemstack.getItem();
        if (!isFood(itemstack)) {
            if (player.getPassengers().isEmpty()) {
                this.startRiding(player);
                this.isRiding = true;
                rideCooldown = 20;
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("EggLayTime")) {
            this.eggTime = pCompound.getInt("EggLayTime");
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("EggLayTime", this.eggTime);
    }


    //sounds
protected SoundEvent getAmbientSound() {
    return null;
}

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected void playStepSound(BlockPos p_28301_, BlockState p_28302_) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.1F, 1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }
}
