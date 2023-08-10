package mod.acats.fromanotherworld.forge.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.StatusEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StatusEffectRegistryForge {
    public static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FromAnotherWorld.MOD_ID);
    public static void register(IEventBus eventBus){
        StatusEffectRegistry.STATUS_EFFECT_REGISTRY.registerAll(STATUS_EFFECTS::register);
        STATUS_EFFECTS.register(eventBus);
    }
}
