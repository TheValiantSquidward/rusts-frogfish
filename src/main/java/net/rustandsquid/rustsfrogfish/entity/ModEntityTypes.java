package net.rustandsquid.rustsfrogfish.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.*;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RustsFrogfish.MOD_ID);


    public static final RegistryObject<EntityType<NeilpeartiaEntity>> NEILPEARTIA =
            ENTITY_TYPES.register("neilpeartia", () -> EntityType.Builder.of(NeilpeartiaEntity::new, MobCategory.WATER_CREATURE)
                    //hitbox size
                    .sized(2f, 1.5f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "neilpeartia").toString()));


    public static final RegistryObject<EntityType<GiganhingaEntity>> GIGANHINGA =
            ENTITY_TYPES.register("giganhinga", () -> EntityType.Builder.of(GiganhingaEntity::new, MobCategory.CREATURE)
                    //hitbox size
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "giganhinga").toString()));

    public static final RegistryObject<EntityType<BlochiusEntity>> BLOCHIUS =
            ENTITY_TYPES.register("blochius", () -> EntityType.Builder.of(BlochiusEntity::new, MobCategory.WATER_AMBIENT)
                    //hitbox size
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "blochius").toString()));

    public static final RegistryObject<EntityType<TapejaraEntity>> TAPEJARA =
            ENTITY_TYPES.register("tapejara", () -> EntityType.Builder.of(TapejaraEntity::new, MobCategory.CREATURE)
                    //hitbox size
                    .sized(5.0f, 5.0f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "tapejara").toString()));

    public static final RegistryObject<EntityType<InfertileEggEntity>> INFERTILEEGG =
            ENTITY_TYPES.register("infertile_egg", () -> EntityType.Builder.of(InfertileEggEntity::new, MobCategory.MISC)
                    //hitbox size
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "infertile_egg").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
