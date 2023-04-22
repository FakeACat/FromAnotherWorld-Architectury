package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.particle.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistryForge {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FromAnotherWorld.MOD_ID);

    public static void register(IEventBus eventBus){
        PARTICLES.register("thing_gore", () -> ParticleRegistry.THING_GORE);
        PARTICLES.register("thing_spit", () -> ParticleRegistry.THING_SPIT);
        PARTICLES.register("big_flames", () -> ParticleRegistry.BIG_FLAMES);

        PARTICLES.register(eventBus);
    }

    public static void clientRegister(){

        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.THING_GORE, RainSplashParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.THING_SPIT, SpitParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.BIG_FLAMES, CampfireSmokeParticle.SignalSmokeFactory::new);
    }
}
