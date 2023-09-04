package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.FromAnotherWorld;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {
    public static final FALRegister<SoundEvent> SOUND_REGISTRY = new FALRegister<>();

    public static final FALRegistryObject<SoundEvent> GENERAL_AMBIENT = register("fromanotherworld.entity.thing.general.ambient");
    public static final FALRegistryObject<SoundEvent> GENERAL_HURT = register("fromanotherworld.entity.thing.general.hurt");
    public static final FALRegistryObject<SoundEvent> GENERAL_DEATH = register("fromanotherworld.entity.thing.general.death");

    public static final FALRegistryObject<SoundEvent> WEAK_AMBIENT = register("fromanotherworld.entity.thing.weak.ambient");
    public static final FALRegistryObject<SoundEvent> WEAK_HURT = register("fromanotherworld.entity.thing.weak.hurt");
    public static final FALRegistryObject<SoundEvent> WEAK_DEATH = register("fromanotherworld.entity.thing.weak.death");
    public static final FALRegistryObject<SoundEvent> WEAK_ALERT = register("fromanotherworld.entity.thing.weak.alert");

    public static final FALRegistryObject<SoundEvent> STRONG_AMBIENT = register("fromanotherworld.entity.thing.strong.ambient");
    public static final FALRegistryObject<SoundEvent> STRONG_HURT = register("fromanotherworld.entity.thing.strong.hurt");
    public static final FALRegistryObject<SoundEvent> STRONG_DEATH = register("fromanotherworld.entity.thing.strong.death");
    public static final FALRegistryObject<SoundEvent> STRONG_ALERT = register("fromanotherworld.entity.thing.strong.alert");

    private static FALRegistryObject<SoundEvent> register(String id){
        return SOUND_REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FromAnotherWorld.MOD_ID, id)));
    }
}
