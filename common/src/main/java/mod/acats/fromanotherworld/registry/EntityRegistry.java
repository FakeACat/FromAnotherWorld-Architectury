package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherworld.entity.misc.StarshipEntity;
import mod.acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.acats.fromanotherworld.entity.projectile.NeedleEntity;
import mod.acats.fromanotherworld.entity.thing.TransitionEntity;
import mod.acats.fromanotherworld.entity.thing.resultant.*;
import mod.acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import mod.acats.fromanotherworld.entity.thing.revealed.VineTentacles;
import mod.acats.fromanotherworld.entity.thing.special.AlienThing;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.HashMap;
import java.util.function.Supplier;

public class EntityRegistry {
    public static final int TRACK_RANGE_LONG = 16;
    public static final int TRACK_RANGE_MEDIUM = 9;
    public static final int TRACK_RANGE_SHORT = 4;

    public static final FALRegister<EntityType<?>> ENTITY_REGISTRY = new FALRegister<>();

    public static final FALRegistryObject<EntityType<Crawler>> CRAWLER = register(
            "crawler",
            EntityType.Builder.of(Crawler::new, MobCategory.MONSTER).sized(0.6F, 0.75F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<ChestSpitter>> CHEST_SPITTER = register(
            "chest_spitter",
            EntityType.Builder.of(ChestSpitter::new, MobCategory.MONSTER).sized(1.0F, 0.375F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<JulietteThing>> JULIETTE_THING = register(
            "juliette_thing",
            EntityType.Builder.of(JulietteThing::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<DogBeast>> DOGBEAST = register(
            "dogbeast",
            EntityType.Builder.of(DogBeast::new, MobCategory.MONSTER).sized(0.9F, 1.4F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<BloodCrawler>> BLOOD_CRAWLER = register(
            "blood_crawler",
            EntityType.Builder.of(BloodCrawler::new, MobCategory.MONSTER).sized(0.45F, 0.45F).clientTrackingRange(TRACK_RANGE_SHORT)
    );
    public static final FALRegistryObject<EntityType<DogBeastSpitter>> DOGBEAST_SPITTER = register(
            "dogbeast_spitter",
            EntityType.Builder.of(DogBeastSpitter::new, MobCategory.MONSTER).sized(0.6F, 1.2F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<Impaler>> IMPALER = register(
            "impaler",
            EntityType.Builder.of(Impaler::new, MobCategory.MONSTER).sized(0.9F, 1.4F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<StarshipEntity>> STARSHIP = register(
            "starship",
            EntityType.Builder.of(StarshipEntity::new, MobCategory.MISC).sized(5.0F, 2.0F).clientTrackingRange(TRACK_RANGE_LONG)
    );
    public static final FALRegistryObject<EntityType<Beast>> BEAST = register(
            "beast",
            EntityType.Builder.of(Beast::new, MobCategory.MONSTER).sized(2.5F, 2.5F).clientTrackingRange(TRACK_RANGE_LONG)
    );
    public static final FALRegistryObject<EntityType<PalmerThing>> PALMER_THING = register(
            "palmer_thing",
            EntityType.Builder.of(PalmerThing::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<BlairThing>> BLAIR_THING = register(
            "blair_thing",
            EntityType.Builder.of(BlairThing::new, MobCategory.MONSTER).sized(2.0F, 3.0F).clientTrackingRange(TRACK_RANGE_LONG)
    );
    public static final FALRegistryObject<EntityType<AlienThing>> ALIEN_THING = register(
            "alien_thing",
            EntityType.Builder.of(AlienThing::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );
    public static final FALRegistryObject<EntityType<AssimilationLiquidEntity>> ASSIMILATION_LIQUID = register(
            "assimilation_liquid",
            EntityType.Builder.<AssimilationLiquidEntity>of(AssimilationLiquidEntity::new, MobCategory.MISC)
                    .updateInterval(10)
                    .sized(0.25F, 0.25F)
                    .updateInterval(TRACK_RANGE_SHORT)
    );
    public static final FALRegistryObject<EntityType<NeedleEntity>> NEEDLE = register(
            "needle",
            EntityType.Builder.<NeedleEntity>of(NeedleEntity::new, MobCategory.MISC)
                    .updateInterval(10)
                    .sized(0.25F, 0.25F)
                    .updateInterval(TRACK_RANGE_SHORT)
    );
    public static final FALRegistryObject<EntityType<SplitFace>> SPLIT_FACE = register(
            "split_face",
            EntityType.Builder.of(SplitFace::new, MobCategory.MONSTER).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );

    public static final FALRegistryObject<EntityType<TransitionEntity>> TRANSITION = register(
            "transition",
            EntityType.Builder.of(TransitionEntity::new, MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );

    public static final FALRegistryObject<EntityType<Prowler>> PROWLER = register(
            "prowler",
            EntityType.Builder.of(Prowler::new, MobCategory.MONSTER).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );

    public static final FALRegistryObject<EntityType<VineTentacles>> VINE_TENTACLES = register(
            "vine_tentacles",
            EntityType.Builder.of(VineTentacles::new, MobCategory.MONSTER).sized(1.0F, 2.0F).clientTrackingRange(TRACK_RANGE_MEDIUM)
    );

    public static HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>> getAttributes() {
        HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>> map = new HashMap<>();

        map.put(CRAWLER.get(), Crawler::createCrawlerAttributes);
        map.put(CHEST_SPITTER.get(), ChestSpitter::createChestSpitterAttributes);
        map.put(JULIETTE_THING.get(), JulietteThing::createJulietteThingAttributes);
        map.put(DOGBEAST.get(), DogBeast::createDogBeastAttributes);
        map.put(BLOOD_CRAWLER.get(), BloodCrawler::createBloodCrawlerAttributes);
        map.put(DOGBEAST_SPITTER.get(), DogBeastSpitter::createDogBeastSpitterAttributes);
        map.put(IMPALER.get(), Impaler::createImpalerAttributes);
        map.put(STARSHIP.get(), StarshipEntity::createStarshipAttributes);
        map.put(BEAST.get(), Beast::createBeastAttributes);
        map.put(PALMER_THING.get(), PalmerThing::createPalmerThingAttributes);
        map.put(BLAIR_THING.get(), BlairThing::createBlairThingAttributes);
        map.put(ALIEN_THING.get(), AlienThing::createAlienThingAttributes);
        map.put(SPLIT_FACE.get(), SplitFace::createSplitFaceAttributes);
        map.put(TRANSITION.get(), TransitionEntity::createTransitionAttributes);
        map.put(PROWLER.get(), Prowler::createProwlerAttributes);
        map.put(VINE_TENTACLES.get(), VineTentacles::createVineTentaclesAttributes);

        return map;
    }

    private static <T extends Entity> FALRegistryObject<EntityType<T>> register(String id, EntityType.Builder<T> builder){
        return ENTITY_REGISTRY.register(id, () -> builder.build(id));
    }
}
