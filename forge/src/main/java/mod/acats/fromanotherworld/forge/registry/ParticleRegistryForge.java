package mod.acats.fromanotherworld.forge.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.ParticleType;
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

        Minecraft.getInstance().particleEngine.register(ParticleRegistry.THING_GORE, WaterDropParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.THING_SPIT, SpitParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.BIG_FLAMES, CampfireSmokeParticle.SignalProvider::new);
    }
}
