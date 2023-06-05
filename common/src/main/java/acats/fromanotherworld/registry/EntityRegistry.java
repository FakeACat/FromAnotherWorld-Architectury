package acats.fromanotherworld.registry;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import acats.fromanotherworld.entity.thing.TransitionEntity;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import acats.fromanotherworld.entity.thing.resultant.*;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

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
    public static final HashMap<String, Supplier<DefaultAttributeContainer.Builder>> ATTRIBUTE_REGISTRY = new HashMap<>();

    public static final FAWEntity<CrawlerEntity> CRAWLER = registerLiving(
            "crawler",
            EntityType.Builder.create(CrawlerEntity::new, SpawnGroup.CREATURE).setDimensions(0.75F, 0.75F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            CrawlerEntity::createCrawlerAttributes
    );
    public static final FAWEntity<ChestSpitterEntity> CHEST_SPITTER = registerLiving(
            "chest_spitter",
            EntityType.Builder.create(ChestSpitterEntity::new, SpawnGroup.CREATURE).setDimensions(1.0F, 0.375F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            ChestSpitterEntity::createChestSpitterAttributes
    );
    public static final FAWEntity<JulietteThingEntity> JULIETTE_THING = registerLiving(
            "juliette_thing",
            EntityType.Builder.create(JulietteThingEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            JulietteThingEntity::createJulietteThingAttributes
    );
    public static final FAWEntity<DogBeastEntity> DOGBEAST = registerLiving(
            "dogbeast",
            EntityType.Builder.create(DogBeastEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 0.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeastEntity::createDogBeastAttributes
    );
    public static final FAWEntity<BloodCrawlerEntity> BLOOD_CRAWLER = registerLiving(
            "blood_crawler",
            EntityType.Builder.create(BloodCrawlerEntity::new, SpawnGroup.CREATURE).setDimensions(0.45F, 0.45F).maxTrackingRange(TRACK_RANGE_SHORT),
            BloodCrawlerEntity::createBloodCrawlerAttributes
    );
    public static final FAWEntity<DogBeastSpitterEntity> DOGBEAST_SPITTER = registerLiving(
            "dogbeast_spitter",
            EntityType.Builder.create(DogBeastSpitterEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.2F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            DogBeastSpitterEntity::createDogBeastSpitterAttributes
    );
    public static final FAWEntity<ImpalerEntity> IMPALER = registerLiving(
            "impaler",
            EntityType.Builder.create(ImpalerEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 0.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            ImpalerEntity::createImpalerAttributes
    );
    public static final FAWEntity<StarshipEntity> STARSHIP = registerLiving(
            "starship",
            EntityType.Builder.create(StarshipEntity::new, SpawnGroup.MISC).setDimensions(5.0F, 2.0F).maxTrackingRange(TRACK_RANGE_LONG),
            StarshipEntity::createStarshipAttributes
    );
    public static final FAWEntity<BeastEntity> BEAST = registerLiving(
            "beast",
            EntityType.Builder.create(BeastEntity::new, SpawnGroup.CREATURE).setDimensions(2.5F, 2.5F).maxTrackingRange(TRACK_RANGE_LONG),
            BeastEntity::createBeastAttributes
    );
    public static final FAWEntity<PalmerThingEntity> PALMER_THING = registerLiving(
            "palmer_thing",
            EntityType.Builder.create(PalmerThingEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            PalmerThingEntity::createPalmerThingAttributes
    );
    public static final FAWEntity<BlairThingEntity> BLAIR_THING = registerLiving(
            "blair_thing",
            EntityType.Builder.create(BlairThingEntity::new, SpawnGroup.CREATURE).setDimensions(2.0F, 3.0F).maxTrackingRange(TRACK_RANGE_LONG),
            BlairThingEntity::createBlairThingAttributes
    );
    public static final FAWEntity<AlienThingEntity> ALIEN_THING = registerLiving(
            "alien_thing",
            EntityType.Builder.create(AlienThingEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            AlienThingEntity::createAlienThingAttributes
    );
    public static final FAWEntity<AssimilationLiquidEntity> ASSIMILATION_LIQUID = register(
            "assimilation_liquid",
            EntityType.Builder.<AssimilationLiquidEntity>create(AssimilationLiquidEntity::new, SpawnGroup.MISC)
                    .trackingTickInterval(10)
                    .setDimensions(0.25F, 0.25F)
                    .trackingTickInterval(TRACK_RANGE_SHORT)
    );
    public static final FAWEntity<NeedleEntity> NEEDLE = register(
            "needle",
            EntityType.Builder.<NeedleEntity>create(NeedleEntity::new, SpawnGroup.MISC)
                    .trackingTickInterval(10)
                    .setDimensions(0.25F, 0.25F)
                    .trackingTickInterval(TRACK_RANGE_SHORT)
    );
    public static final FAWEntity<SplitFaceEntity> SPLIT_FACE = registerLiving(
            "split_face",
            EntityType.Builder.create(SplitFaceEntity::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.8F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            SplitFaceEntity::createSplitFaceAttributes
    );

    public static final FAWEntity<TransitionEntity> TRANSITION = registerLiving(
            "transition",
            EntityType.Builder.create(TransitionEntity::new, SpawnGroup.CREATURE).setDimensions(1.0F, 1.0F).maxTrackingRange(TRACK_RANGE_MEDIUM),
            TransitionEntity::createTransitionAttributes
    );

    private static <T extends LivingEntity> FAWEntity<T> registerLiving(String id, EntityType.Builder<T> builder, Supplier<DefaultAttributeContainer.Builder> attributeSupplier){
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

    public static EntityModelLayer spiderLegsModelLayer;
}
