package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.SoundRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistryFabric {
    public static void register(){
        for (String soundID:
                SoundRegistry.SOUND_REGISTRY) {
            Registry.register(BuiltInRegistries.SOUND_EVENT, soundID, SoundEvent.createVariableRangeEvent(new ResourceLocation(FromAnotherWorld.MOD_ID, soundID)));
        }
    }
}
