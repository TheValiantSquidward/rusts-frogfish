package net.rustandsquid.rustsfrogfish.item;

import com.peeko32213.unusualprehistory.common.item.ModItemConsumable;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.ModEntityTypes;
import net.rustandsquid.rustsfrogfish.sound.ModSounds;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RustsFrogfish.MOD_ID);

    public static final RegistryObject<Item> FISH_LEATHER = ITEMS.register("fish_leather",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> NEILPEARTIA_VIAL = ITEMS.register("neilpeartia_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> FROGFISH_LURE = ITEMS.register("frogfish_lure",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> FROGDISC = ITEMS.register("frogdisc",
            () -> new RecordItem(5, ModSounds.FROGSONG,
                    new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB).stacksTo(1), 262));

    public static final RegistryObject<Item> RAW_FROGFISH = ITEMS.register("raw_frogfish",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(1).saturationMod(1)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,200, 0), 1f)
                            .build())
            ));



    public static final RegistryObject<Item> NEILPEARTIASPAWNEGG = ITEMS.register( "neilpeartia_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.NEILPEARTIA, 0xfac553, 0x732011,
            new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));


    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
