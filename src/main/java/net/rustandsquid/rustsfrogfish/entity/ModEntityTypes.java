package net.rustandsquid.rustsfrogfish.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.custom.NeilpeartiaEntity;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RustsFrogfish.MOD_ID);


    public static final RegistryObject<EntityType<NeilpeartiaEntity>> NEILPEARTIA =
            ENTITY_TYPES.register("neilpeartia", () -> EntityType.Builder.of(NeilpeartiaEntity::new, MobCategory.WATER_CREATURE)
                    //hitbox size
                    .sized(1.5f, 1.5f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "neilpeartia").toString()));


    public static final RegistryObject<EntityType<NeilpeartiaEntity>> GIGANHINGA =
            ENTITY_TYPES.register("giganhinga", () -> EntityType.Builder.of(NeilpeartiaEntity::new, MobCategory.CREATURE)
                    //hitbox size
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(RustsFrogfish.MOD_ID, "giganhinga").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
