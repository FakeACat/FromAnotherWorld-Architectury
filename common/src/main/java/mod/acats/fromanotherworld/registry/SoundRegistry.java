package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;

import mod.acats.fromanotherworld.utilities.registry.FAWRegister;
import mod.acats.fromanotherworld.utilities.registry.FAWRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {

    public static final FAWRegister<SoundEvent> SOUND_REGISTRY = new FAWRegister<>();

    public static final FAWRegistryObject<SoundEvent> GENERAL_AMBIENT = register("fromanotherworld.entity.thing.general.ambient");
    public static final FAWRegistryObject<SoundEvent> GENERAL_HURT = register("fromanotherworld.entity.thing.general.hurt");
    public static final FAWRegistryObject<SoundEvent> GENERAL_DEATH = register("fromanotherworld.entity.thing.general.death");

    public static final FAWRegistryObject<SoundEvent> WEAK_AMBIENT = register("fromanotherworld.entity.thing.weak.ambient");
    public static final FAWRegistryObject<SoundEvent> WEAK_HURT = register("fromanotherworld.entity.thing.weak.hurt");
    public static final FAWRegistryObject<SoundEvent> WEAK_DEATH = register("fromanotherworld.entity.thing.weak.death");
    public static final FAWRegistryObject<SoundEvent> WEAK_ALERT = register("fromanotherworld.entity.thing.weak.alert");

    public static final FAWRegistryObject<SoundEvent> STRONG_AMBIENT = register("fromanotherworld.entity.thing.strong.ambient");
    public static final FAWRegistryObject<SoundEvent> STRONG_HURT = register("fromanotherworld.entity.thing.strong.hurt");
    public static final FAWRegistryObject<SoundEvent> STRONG_DEATH = register("fromanotherworld.entity.thing.strong.death");
    public static final FAWRegistryObject<SoundEvent> STRONG_ALERT = register("fromanotherworld.entity.thing.strong.alert");

    private static FAWRegistryObject<SoundEvent> register(String id){
        return SOUND_REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FromAnotherWorld.MOD_ID, id)));
    }
}
