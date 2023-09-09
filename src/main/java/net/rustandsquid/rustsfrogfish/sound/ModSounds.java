package net.rustandsquid.rustsfrogfish.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rustandsquid.rustsfrogfish.RustsFrogfish;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RustsFrogfish.MOD_ID);


    public static final RegistryObject<SoundEvent> FROGSONG = registerSoundEvent("frogsong");

    public static final RegistryObject<SoundEvent> ANHINGADEATH = registerSoundEvent("anhingadeath1");

    public static final RegistryObject<SoundEvent> ANHINGAIDLE = registerSoundEvent("anhingaidle1");

    public static final RegistryObject<SoundEvent> ANHINGAHURT = registerSoundEvent("anhingahurt1");

private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RustsFrogfish.MOD_ID, name)));
}

    public static void register(IEventBus eventBus) { SOUND_EVENTS.register(eventBus); }
}
