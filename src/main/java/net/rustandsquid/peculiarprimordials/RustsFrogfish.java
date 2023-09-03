package net.rustandsquid.peculiarprimordials;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.rustandsquid.peculiarprimordials.block.ModBlocks;
import net.rustandsquid.peculiarprimordials.entity.ModEntityTypes;
import net.rustandsquid.peculiarprimordials.entity.client.NeilpeartiaRenderer;
import net.rustandsquid.peculiarprimordials.item.ModItems;
import net.rustandsquid.peculiarprimordials.potion.ModPotions;
import net.rustandsquid.peculiarprimordials.util.HahaSuperBrewer;
import net.rustandsquid.peculiarprimordials.sound.ModSounds;
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

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ModEntityTypes.NEILPEARTIA.get(), NeilpeartiaRenderer::new);

            BrewingRecipeRegistry.addRecipe(new HahaSuperBrewer(Potions.AWKWARD,
                    ModItems.RAW_FROGFISH.get(), ModPotions.GLOW_POTION.get()));
        }
    }
}
