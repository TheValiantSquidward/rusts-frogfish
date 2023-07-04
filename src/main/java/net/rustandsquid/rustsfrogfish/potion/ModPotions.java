package net.rustandsquid.rustsfrogfish.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, RustsFrogfish.MOD_ID);

    public static final RegistryObject<Potion> GLOW_POTION = POTIONS.register("glow_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.GLOWING, 10000, 0)));


    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
