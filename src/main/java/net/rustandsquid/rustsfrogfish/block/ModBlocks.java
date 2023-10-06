package net.rustandsquid.rustsfrogfish.block;

import com.peeko32213.unusualprehistory.UnusualPrehistory;
import com.peeko32213.unusualprehistory.common.block.BlockDinosaurLandEggs;
import com.peeko32213.unusualprehistory.common.block.BlockDinosaurWaterEggs;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;
import net.rustandsquid.rustsfrogfish.entity.ModEntityTypes;
import net.rustandsquid.rustsfrogfish.item.ModItems;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.rustandsquid.rustsfrogfish.item.ModCreativeModeTab.RUSTSFROGFISH_TAB;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RustsFrogfish.MOD_ID);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        return toReturn;
    }

    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(UnusualPrehistory.DINO_TAB)));
        return block;
    }

    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block, Function<Supplier<T>, Item> item) {
        Supplier<T> entry = create(key, block);
        ModItems.ITEMS.register(key, () -> item.apply(entry));
        return entry;
    }

    private static <T extends Block> Supplier<T> create(String key, Supplier<T> block) {
        return BLOCKS.register(key, block);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }


    public static final Supplier<Block> NEILPEARTIA_EGGS = create("neilpeartia_eggs",
            () -> new BlockDinosaurWaterEggs(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).instabreak().noOcclusion().noCollission().randomTicks(),
                    ModEntityTypes.NEILPEARTIA,
                    false
            ),
            entry -> new PlaceOnWaterBlockItem(entry.get(), new Item.Properties().tab(RUSTSFROGFISH_TAB)));

    public static final Supplier<Block> BLOCHIUS_EGGS = create("blochius_eggs",
            () -> new BlockDinosaurWaterEggs(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).instabreak().noOcclusion().noCollission().randomTicks(),
                    ModEntityTypes.BLOCHIUS,
                    false
            ),
            entry -> new PlaceOnWaterBlockItem(entry.get(), new Item.Properties().tab(RUSTSFROGFISH_TAB)));


    public static final Supplier<Block> GIGANHINGA_EGGS = create("giganhinga_eggs",
            () -> new BlockDinosaurLandEggs(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).instabreak().noOcclusion().noCollission().randomTicks(),
                    ModEntityTypes.GIGANHINGA,
                    4
            ),
            entry -> new BlockItem(entry.get(), new Item.Properties().tab(RUSTSFROGFISH_TAB)));

    public static final Supplier<Block> TAPEJARA_EGGS = create("tapejara_eggs",
            () -> new BlockDinosaurLandEggs(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).instabreak().noOcclusion().noCollission().randomTicks(),
                    ModEntityTypes.TAPEJARA,
                    3
            ),
            entry -> new BlockItem(entry.get(), new Item.Properties().tab(RUSTSFROGFISH_TAB)));

    //public static final RegistryObject<Block> BANANA_CROP = registerBlock("banana_crop",
           // () -> new DoublePlantBlock(new FoxiiTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
