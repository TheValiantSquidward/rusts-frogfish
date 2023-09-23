package net.rustandsquid.rustsfrogfish;

import com.mojang.logging.LogUtils;
import com.peeko32213.unusualprehistory.common.config.UnusualPrehistoryConfig;
import com.peeko32213.unusualprehistory.core.registry.UPTags;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.rustandsquid.rustsfrogfish.block.ModBlocks;
import net.rustandsquid.rustsfrogfish.entity.ModEntityTypes;
import net.rustandsquid.rustsfrogfish.entity.client.*;
import net.rustandsquid.rustsfrogfish.item.ModItems;
import net.rustandsquid.rustsfrogfish.potion.ModPotions;
import net.rustandsquid.rustsfrogfish.util.HahaSuperBrewer;
import net.rustandsquid.rustsfrogfish.sound.ModSounds;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RustsFrogfish.MOD_ID)
public class RustsFrogfish {
    public static final String MOD_ID = "rustsfrogfish";
    private static final Logger LOGGER = LogUtils.getLogger();
    public RustsFrogfish() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModPotions.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        GeckoLib.initialize();

        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::registerSpawnPlacements);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(new HahaSuperBrewer(Potions.AWKWARD,
                ModItems.RAW_FROGFISH.get(), ModPotions.GLOW_POTION.get()));
    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

    private void registerSpawnPlacements(final SpawnPlacementRegisterEvent event) {
        event.register(ModEntityTypes.GIGANHINGA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this::canLandAnimalSpawn, SpawnPlacementRegisterEvent.Operation.AND);
    }

    private boolean canLandAnimalSpawn(EntityType<? extends Animal> p_186238_, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource p_186242_) {
        return level.getBlockState(pos.below()).is(UPTags.DINO_NATURAL_SPAWNABLE) && UnusualPrehistoryConfig.DINO_NATURAL_SPAWNING.get();
    }

    private boolean canFishAnimalSpawn(EntityType<? extends AbstractFish> p_186238_, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource p_186242_) {
        return level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER) && UnusualPrehistoryConfig.DINO_NATURAL_SPAWNING.get();
    }
    
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register
                    (ModEntityTypes.NEILPEARTIA.get(), NeilpeartiaRenderer::new);
            EntityRenderers.register
                    (ModEntityTypes.GIGANHINGA.get(), GiganhingaRenderer::new);
            EntityRenderers.register
                    (ModEntityTypes.BLOCHIUS.get(), BlochiusRenderer::new);
            EntityRenderers.register
                    (ModEntityTypes.TAPEJARA.get(), TapejaraRenderer::new);


        }
    }
}
