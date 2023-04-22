package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.SoundRegistry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistryForge {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FromAnotherWorld.MOD_ID);
    public static void register(IEventBus eventBus){
        for (String soundID:
                SoundRegistry.SOUND_REGISTRY) {
            SOUNDS.register(soundID, () -> SoundEvent.of(new Identifier(FromAnotherWorld.MOD_ID, soundID)));
        }
        SOUNDS.register(eventBus);
    }
}
