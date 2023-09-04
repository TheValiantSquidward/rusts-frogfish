package net.rustandsquid.rustsfrogfish.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.rustandsquid.rustsfrogfish.entity.client.armor.FroghatRenderer;
import net.rustandsquid.rustsfrogfish.entity.custom.Froghat;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenders(EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(Froghat.class, () -> new FroghatRenderer());
    }
}
