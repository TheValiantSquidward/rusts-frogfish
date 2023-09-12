package net.rustandsquid.rustsfrogfish.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.ModEntityTypes;
import net.rustandsquid.rustsfrogfish.item.armor.material.ModArmorMaterials;
import net.rustandsquid.rustsfrogfish.item.custom.ThrowableEggItem;
import net.rustandsquid.rustsfrogfish.item.custom.ThrowableGoldenEggItem;
import net.rustandsquid.rustsfrogfish.sound.ModSounds;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RustsFrogfish.MOD_ID);



    //dna vials
    public static final RegistryObject<Item> NEILPEARTIA_VIAL = ITEMS.register("neilpeartia_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> BLOCHIUS_VIAL = ITEMS.register("blochius_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> GIGANHINGA_VIAL = ITEMS.register("giganhinga_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> TAPEJARA_VIAL = ITEMS.register("tapejara_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> ENSETE_VIAL = ITEMS.register("ensete_oregonese_dna",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    //food items
    public static final RegistryObject<Item> RAW_FROGFISH = ITEMS.register("raw_frogfish",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(1).saturationMod(1)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,200, 0), 1f)
                            .build())
            ));

    public static final RegistryObject<Item> RAW_BLOCHIUS = ITEMS.register("raw_blochius",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(3).saturationMod(2)
                            .build())
            ));

    public static final RegistryObject<Item> RAW_GIGANHINGA = ITEMS.register("raw_giganhinga",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(1).saturationMod(1)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON,200, 0), 0.5f)
                            .build())
            ));

    public static final RegistryObject<Item> ROAST_GIGANHINGA = ITEMS.register("roast_giganhinga",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(7).saturationMod(9)
                            .build())
            ));


    public static final RegistryObject<Item> STONY_SEEDS = ITEMS.register("stony_seeds",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> PURPLE_BANANA = ITEMS.register("ensete_oregonese_fruit",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(7)
                            .build())
            ));

    public static final RegistryObject<Item> STARCHY_REDROOTS = ITEMS.register("starchy_redroots",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)
                    .food(new FoodProperties.Builder().nutrition(6).saturationMod(4)
                            .build())
            ));



    //crafting items
    public static final RegistryObject<Item> FROGFISH_LURE = ITEMS.register("frogfish_lure",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> FISH_LEATHER = ITEMS.register("fish_leather",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> FISH_BONE = ITEMS.register("fish_bone",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> GILDED_CREST = ITEMS.register("gilded_crest",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> FLAMBOYANT_CREST = ITEMS.register("flamboyant_crest",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> ELEGANT_CREST = ITEMS.register("elegant_crest",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    //weapons and armor
    public static final RegistryObject<Item> FROGDISC = ITEMS.register("frogdisc",
            () -> new RecordItem(5, ModSounds.FROGSONG,
                    new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB).stacksTo(1), 262));

    public static final RegistryObject<Item> FROGHAT = ITEMS.register("froghat",
            () -> new ArmorItem(ModArmorMaterials.FROGHAT, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> GILDED_HORN = ITEMS.register("gilded_horn",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB).stacksTo(1)));

    public static final RegistryObject<Item> FLAMBOYANT_SAXOPHONE = ITEMS.register("flamboyant_saxophone",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB).stacksTo(1)));

    public static final RegistryObject<Item> ELEGANT_FLUTE = ITEMS.register("elegant_flute",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB).stacksTo(1)));

    //spawn eggs
    public static final RegistryObject<Item> NEILPEARTIASPAWNEGG = ITEMS.register( "neilpeartia_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.NEILPEARTIA, 0xfac553, 0x732011,
            new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> BLOCHIUSSPAWNEGG = ITEMS.register( "blochius_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BLOCHIUS, 0x9bcfe9, 0x0b3042,
                    new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));


    public static final RegistryObject<Item> GIGANHINGASPAWNEGG = ITEMS.register( "giganhinga_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GIGANHINGA, 0x2f3c42, 0x805746,
                    new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    public static final RegistryObject<Item> TAPEJARASPAWNEGG = ITEMS.register( "tapejara_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.TAPEJARA, 0x131313, 0xe5a742,
                    new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));


    //usable items
    public static final RegistryObject<Item> THROWABLEEGGITEM = ITEMS.register("throwable_anhinga_egg",
            () -> new ThrowableEggItem(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));
    public static final RegistryObject<Item> GOLDENEGGITEM = ITEMS.register("golden_anhinga_item",
            () -> new ThrowableGoldenEggItem(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));

    //fossils
    public static final RegistryObject<Item> SEEDY_REMAINS = ITEMS.register("seedy_remains",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.RUSTSFROGFISH_TAB)));


    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }

}