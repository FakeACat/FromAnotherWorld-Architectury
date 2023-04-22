package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistryFabric {
    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(FromAnotherWorld.MOD_ID, "thing_gore"), ParticleRegistry.THING_GORE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(FromAnotherWorld.MOD_ID, "thing_spit"), ParticleRegistry.THING_SPIT);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(FromAnotherWorld.MOD_ID, "big_flames"), ParticleRegistry.BIG_FLAMES);
    }
    public static void clientRegister(){
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THING_GORE, RainSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THING_SPIT, SpitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BIG_FLAMES, CampfireSmokeParticle.SignalSmokeFactory::new);
    }
}
