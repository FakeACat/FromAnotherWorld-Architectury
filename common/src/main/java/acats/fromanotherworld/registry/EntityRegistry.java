package acats.fromanotherworld.registry;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.entity.thing.TransitionEntity;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import acats.fromanotherworld.entity.thing.resultant.*;
import acats.fromanotherworld.entity.thing.special.AlienThing;
import net.minecraft.client.model.geom.ModelLayerLocation;
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

    public static class FAWEntity<T extends Entity>{
        public FAWEntity(EntityType.Builder<T> builder){
            this.builder = builder;
        }
        EntityType.Builder<T> builder;
        public EntityType<T> build(String id){
            type = builder.build(id);
            return type;
        }
        private EntityType<T> type;
        public EntityType<T> get(){
            return type;
        }
    }

    public static final HashMap<String, FAWEntity<?>> ENTITY_REGISTRY = new HashMap<>();
    public static final HashMap<String, FAWEntity<? extends LivingEntity>> LIVING_ENTITY_REGISTRY = new HashMap<>();
    public static final HashMap<String, Supplier<AttributeSupplier.Builder>> ATTRIBUTE_REGISTRY = new HashMap<>();

    public static final FAWEntity<Crawler> CRAWLER = registerLiving(
            "crawler",
            EntityType.Builder.of(Crawler::new, MobCategory.CREATURE).sized(0.6F, 0.75F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            Crawler::createCrawlerAttributes
    );
    public static final FAWEntity<ChestSpitter> CHEST_SPITTER = registerLiving(
            "chest_spitter",
            EntityType.Builder.of(ChestSpitter::new, MobCategory.CREATURE).sized(1.0F, 0.375F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            ChestSpitter::createChestSpitterAttributes
    );
    public static final FAWEntity<JulietteThing> JULIETTE_THING = registerLiving(
            "juliette_thing",
            EntityType.Builder.of(JulietteThing::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            JulietteThing::createJulietteThingAttributes
    );
    public static final FAWEntity<DogBeast> DOGBEAST = registerLiving(
            "dogbeast",
            EntityType.Builder.of(DogBeast::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeast::createDogBeastAttributes
    );
    public static final FAWEntity<BloodCrawler> BLOOD_CRAWLER = registerLiving(
            "blood_crawler",
            EntityType.Builder.of(BloodCrawler::new, MobCategory.CREATURE).sized(0.45F, 0.45F).clientTrackingRange(TRACK_RANGE_SHORT),
            BloodCrawler::createBloodCrawlerAttributes
    );
    public static final FAWEntity<DogBeastSpitter> DOGBEAST_SPITTER = registerLiving(
            "dogbeast_spitter",
            EntityType.Builder.of(DogBeastSpitter::new, MobCategory.CREATURE).sized(0.6F, 1.2F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeastSpitter::createDogBeastSpitterAttributes
    );
    public static final FAWEntity<Impaler> IMPALER = registerLiving(
            "impaler",
            EntityType.Builder.of(Impaler::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            Impaler::createImpalerAttributes
    );
    public static final FAWEntity<StarshipEntity> STARSHIP = registerLiving(
            "starship",
            EntityType.Builder.of(StarshipEntity::new, MobCategory.MISC).sized(5.0F, 2.0F).clientTrackingRange(TRACK_RANGE_LONG),
            StarshipEntity::createStarshipAttributes
    );
    public static final FAWEntity<Beast> BEAST = registerLiving(
            "beast",
            EntityType.Builder.of(Beast::new, MobCategory.CREATURE).sized(2.5F, 2.5F).clientTrackingRange(TRACK_RANGE_LONG),
            Beast::createBeastAttributes
    );
    public static final FAWEntity<PalmerThing> PALMER_THING = registerLiving(
            "palmer_thing",
            EntityType.Builder.of(PalmerThing::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            PalmerThing::createPalmerThingAttributes
    );
    public static final FAWEntity<BlairThing> BLAIR_THING = registerLiving(
            "blair_thing",
            EntityType.Builder.of(BlairThing::new, MobCategory.CREATURE).sized(2.0F, 3.0F).clientTrackingRange(TRACK_RANGE_LONG),
            BlairThing::createBlairThingAttributes
    );
    public static final FAWEntity<AlienThing> ALIEN_THING = registerLiving(
            "alien_thing",
            EntityType.Builder.of(AlienThing::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            AlienThing::createAlienThingAttributes
    );
    public static final FAWEntity<AssimilationLiquidEntity> ASSIMILATION_LIQUID = register(
            "assimilation_liquid",
            EntityType.Builder.<AssimilationLiquidEntity>of(AssimilationLiquidEntity::new, MobCategory.MISC)
                    .updateInterval(10)
                    .sized(0.25F, 0.25F)
                    .updateInterval(TRACK_RANGE_SHORT)
    );
    public static final FAWEntity<NeedleEntity> NEEDLE = register(
            "needle",
            EntityType.Builder.<NeedleEntity>of(NeedleEntity::new, MobCategory.MISC)
                    .updateInterval(10)
                    .sized(0.25F, 0.25F)
                    .updateInterval(TRACK_RANGE_SHORT)
    );
    public static final FAWEntity<SplitFace> SPLIT_FACE = registerLiving(
            "split_face",
            EntityType.Builder.of(SplitFace::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            SplitFace::createSplitFaceAttributes
    );

    public static final FAWEntity<TransitionEntity> TRANSITION = registerLiving(
            "transition",
            EntityType.Builder.of(TransitionEntity::new, MobCategory.CREATURE).sized(1.0F, 1.0F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            TransitionEntity::createTransitionAttributes
    );

    public static final FAWEntity<Prowler> PROWLER = registerLiving(
            "prowler",
            EntityType.Builder.of(Prowler::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            Prowler::createProwlerAttributes
    );

    private static <T extends LivingEntity> FAWEntity<T> registerLiving(String id, EntityType.Builder<T> builder, Supplier<AttributeSupplier.Builder> attributeSupplier){
        ATTRIBUTE_REGISTRY.put(id, attributeSupplier);
        FAWEntity<T> fawEntity = new FAWEntity<>(builder);
        LIVING_ENTITY_REGISTRY.put(id, fawEntity);
        ENTITY_REGISTRY.put(id, fawEntity);
        return fawEntity;
    }

    private static <T extends Entity> FAWEntity<T> register(String id, EntityType.Builder<T> builder){
        FAWEntity<T> fawEntity = new FAWEntity<>(builder);
        ENTITY_REGISTRY.put(id, fawEntity);
        return fawEntity;
    }

    public static ModelLayerLocation spiderLegsModelLayer;
    public static ModelLayerLocation tentacleSegmentModelLayer;
}
