package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleRegistry {

    public static final FALRegister<ParticleType<?>> PARTICLE_REGISTRY = new FALRegister<>();
    public static final FALRegistryObject<SimpleParticleType> THING_GORE = PARTICLE_REGISTRY.register("thing_gore", () -> new SimpleParticleType(false) {});
    public static final FALRegistryObject<SimpleParticleType> THING_SPIT = PARTICLE_REGISTRY.register("thing_spit", () -> new SimpleParticleType(false) {});
    public static final FALRegistryObject<SimpleParticleType> BIG_FLAMES = PARTICLE_REGISTRY.register("big_flames", () -> new SimpleParticleType(true) {});
}
