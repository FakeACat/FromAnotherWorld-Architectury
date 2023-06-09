package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {

    public static final List<String> SOUND_REGISTRY = new ArrayList<>();

    public static final Supplier<SoundEvent> GENERAL_AMBIENT = register("fromanotherworld.entity.thing.general.ambient");
    public static final Supplier<SoundEvent> GENERAL_HURT = register("fromanotherworld.entity.thing.general.hurt");
    public static final Supplier<SoundEvent> GENERAL_DEATH = register("fromanotherworld.entity.thing.general.death");

    public static final Supplier<SoundEvent> WEAK_AMBIENT = register("fromanotherworld.entity.thing.weak.ambient");
    public static final Supplier<SoundEvent> WEAK_HURT = register("fromanotherworld.entity.thing.weak.hurt");
    public static final Supplier<SoundEvent> WEAK_DEATH = register("fromanotherworld.entity.thing.weak.death");
    public static final Supplier<SoundEvent> WEAK_ALERT = register("fromanotherworld.entity.thing.weak.alert");

    public static final Supplier<SoundEvent> STRONG_AMBIENT = register("fromanotherworld.entity.thing.strong.ambient");
    public static final Supplier<SoundEvent> STRONG_HURT = register("fromanotherworld.entity.thing.strong.hurt");
    public static final Supplier<SoundEvent> STRONG_DEATH = register("fromanotherworld.entity.thing.strong.death");
    public static final Supplier<SoundEvent> STRONG_ALERT = register("fromanotherworld.entity.thing.strong.alert");
    private static Supplier<SoundEvent> register(String id){
        SOUND_REGISTRY.add(id);
        return () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FromAnotherWorld.MOD_ID, id));
    }
}
