package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.registry.SoundRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class SoundRegistryFabric {
    public static void register(){
        SoundRegistry.SOUND_REGISTRY.registerAll(
                (id, builder) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, builder.get())
        );
    }
}
