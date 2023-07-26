package net.rustandsquid.rustsfrogfish.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> MOD_SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RustsFrogfish.MOD_ID);


    public static void register(IEventBus eventBus) { MOD_SOUNDS.register(eventBus); }
}
