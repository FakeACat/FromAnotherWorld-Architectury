package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.ParticleRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.SpitParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ParticleRegistryFabric {
    public static void register(){
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, "thing_gore"), ParticleRegistry.THING_GORE);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, "thing_spit"), ParticleRegistry.THING_SPIT);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, "big_flames"), ParticleRegistry.BIG_FLAMES);
    }
    public static void clientRegister(){
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THING_GORE, WaterDropParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THING_SPIT, SpitParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BIG_FLAMES, CampfireSmokeParticle.SignalProvider::new);
    }
}
