package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.SoundRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistryFabric {
    public static void register(){
        for (String soundID:
                SoundRegistry.SOUND_REGISTRY) {
            Registry.register(Registries.SOUND_EVENT, soundID, SoundEvent.of(new Identifier(FromAnotherWorld.MOD_ID, soundID)));
        }
    }
}
