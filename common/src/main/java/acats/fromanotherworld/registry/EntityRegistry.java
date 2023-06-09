package acats.fromanotherworld.registry;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.entity.thing.TransitionEntity;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import acats.fromanotherworld.entity.thing.resultant.*;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
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

    public static final FAWEntity<CrawlerEntity> CRAWLER = registerLiving(
            "crawler",
            EntityType.Builder.of(CrawlerEntity::new, MobCategory.CREATURE).sized(0.75F, 0.75F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            CrawlerEntity::createCrawlerAttributes
    );
    public static final FAWEntity<ChestSpitterEntity> CHEST_SPITTER = registerLiving(
            "chest_spitter",
            EntityType.Builder.of(ChestSpitterEntity::new, MobCategory.CREATURE).sized(1.0F, 0.375F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            ChestSpitterEntity::createChestSpitterAttributes
    );
    public static final FAWEntity<JulietteThingEntity> JULIETTE_THING = registerLiving(
            "juliette_thing",
            EntityType.Builder.of(JulietteThingEntity::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            JulietteThingEntity::createJulietteThingAttributes
    );
    public static final FAWEntity<DogBeastEntity> DOGBEAST = registerLiving(
            "dogbeast",
            EntityType.Builder.of(DogBeastEntity::new, MobCategory.CREATURE).sized(0.8F, 0.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeastEntity::createDogBeastAttributes
    );
    public static final FAWEntity<BloodCrawlerEntity> BLOOD_CRAWLER = registerLiving(
            "blood_crawler",
            EntityType.Builder.of(BloodCrawlerEntity::new, MobCategory.CREATURE).sized(0.45F, 0.45F).clientTrackingRange(TRACK_RANGE_SHORT),
            BloodCrawlerEntity::createBloodCrawlerAttributes
    );
    public static final FAWEntity<DogBeastSpitterEntity> DOGBEAST_SPITTER = registerLiving(
            "dogbeast_spitter",
            EntityType.Builder.of(DogBeastSpitterEntity::new, MobCategory.CREATURE).sized(0.8F, 1.2F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeastSpitterEntity::createDogBeastSpitterAttributes
    );
    public static final FAWEntity<ImpalerEntity> IMPALER = registerLiving(
            "impaler",
            EntityType.Builder.of(ImpalerEntity::new, MobCategory.CREATURE).sized(0.8F, 0.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            ImpalerEntity::createImpalerAttributes
    );
    public static final FAWEntity<StarshipEntity> STARSHIP = registerLiving(
            "starship",
            EntityType.Builder.of(StarshipEntity::new, MobCategory.MISC).sized(5.0F, 2.0F).clientTrackingRange(TRACK_RANGE_LONG),
            StarshipEntity::createStarshipAttributes
    );
    public static final FAWEntity<BeastEntity> BEAST = registerLiving(
            "beast",
            EntityType.Builder.of(BeastEntity::new, MobCategory.CREATURE).sized(2.5F, 2.5F).clientTrackingRange(TRACK_RANGE_LONG),
            BeastEntity::createBeastAttributes
    );
    public static final FAWEntity<PalmerThingEntity> PALMER_THING = registerLiving(
            "palmer_thing",
            EntityType.Builder.of(PalmerThingEntity::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            PalmerThingEntity::createPalmerThingAttributes
    );
    public static final FAWEntity<BlairThingEntity> BLAIR_THING = registerLiving(
            "blair_thing",
            EntityType.Builder.of(BlairThingEntity::new, MobCategory.CREATURE).sized(2.0F, 3.0F).clientTrackingRange(TRACK_RANGE_LONG),
            BlairThingEntity::createBlairThingAttributes
    );
    public static final FAWEntity<AlienThingEntity> ALIEN_THING = registerLiving(
            "alien_thing",
            EntityType.Builder.of(AlienThingEntity::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            AlienThingEntity::createAlienThingAttributes
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
    public static final FAWEntity<SplitFaceEntity> SPLIT_FACE = registerLiving(
            "split_face",
            EntityType.Builder.of(SplitFaceEntity::new, MobCategory.CREATURE).sized(0.8F, 1.8F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            SplitFaceEntity::createSplitFaceAttributes
    );

    public static final FAWEntity<TransitionEntity> TRANSITION = registerLiving(
            "transition",
            EntityType.Builder.of(TransitionEntity::new, MobCategory.CREATURE).sized(1.0F, 1.0F).clientTrackingRange(TRACK_RANGE_MEDIUM),
            TransitionEntity::createTransitionAttributes
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
}
