package net.rustandsquid.peculiarprimordials.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rustandsquid.peculiarprimordials.RustsFrogfish;
import net.rustandsquid.peculiarprimordials.entity.ModEntityTypes;
import net.rustandsquid.peculiarprimordials.entity.client.armor.FroghatRenderer;
import net.rustandsquid.peculiarprimordials.entity.custom.Froghat;
import net.rustandsquid.peculiarprimordials.entity.custom.GiganhingaEntity;
import net.rustandsquid.peculiarprimordials.entity.custom.NeilpeartiaEntity;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = RustsFrogfish.MOD_ID)
    public class ForgeEvents {



    }
    @Mod.EventBusSubscriber(modid = RustsFrogfish.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class  ModEventBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.NEILPEARTIA.get(), NeilpeartiaEntity.setAttributes());
            event.put(ModEntityTypes.GIGANHINGA.get(), GiganhingaEntity.setAttributes());
        }

        @SubscribeEvent
        public static void registerArmorRenders(EntityRenderersEvent.AddLayers event) {
            GeoArmorRenderer.registerArmorRenderer(Froghat.class, () -> new FroghatRenderer());
        }
    }
}
