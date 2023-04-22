package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerResourceMetadata;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerClothingFeatureRenderer.class)
public abstract class VillagerClothingFeatureRendererMixin<T extends LivingEntity & VillagerDataContainer, M extends EntityModel<T> & ModelWithHat> extends FeatureRenderer<T, M> {
    public VillagerClothingFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }
    @Shadow public abstract <K> VillagerResourceMetadata.HatType getHatType(Object2ObjectMap<K, VillagerResourceMetadata.HatType> hatLookUp, String keyType, DefaultedRegistry<K> registry, K key);
    @Shadow protected abstract Identifier findTexture(String keyType, Identifier keyId);
    @Final @Shadow private Object2ObjectMap<VillagerType, VillagerResourceMetadata.HatType> villagerTypeToHat;
    @Shadow @Final private Object2ObjectMap<VillagerProfession, VillagerResourceMetadata.HatType> professionToHat;
    @Shadow @Final private static Int2ObjectMap<Identifier> LEVEL_TO_ID;

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V")
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!livingEntity.isInvisible()) {
            DisguisedThing thing = (DisguisedThing) livingEntity;
            VillagerData villagerData = livingEntity.getVillagerData();
            VillagerType villagerType = villagerData.getType();
            VillagerProfession villagerProfession = villagerData.getProfession();
            VillagerResourceMetadata.HatType hatType = this.getHatType(this.villagerTypeToHat, "type", Registries.VILLAGER_TYPE, villagerType);
            VillagerResourceMetadata.HatType hatType2 = this.getHatType(this.professionToHat, "profession", Registries.VILLAGER_PROFESSION, villagerProfession);
            M entityModel = this.getContextModel();
            entityModel.setHatVisible(hatType2 == VillagerResourceMetadata.HatType.NONE || hatType2 == VillagerResourceMetadata.HatType.PARTIAL && hatType != VillagerResourceMetadata.HatType.FULL);
            Identifier identifier = this.findTexture("type", Registries.VILLAGER_TYPE.getId(villagerType));
            RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
            entityModel.setHatVisible(true);
            if (villagerProfession != VillagerProfession.NONE && !livingEntity.isBaby()) {
                Identifier identifier2 = this.findTexture("profession", Registries.VILLAGER_PROFESSION.getId(villagerProfession));
                RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier2, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
                if (villagerProfession != VillagerProfession.NITWIT) {
                    Identifier identifier3 = this.findTexture("profession_level", LEVEL_TO_ID.get(MathHelper.clamp(villagerData.getLevel(), 1, LEVEL_TO_ID.size())));
                    RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier3, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
                }
            }
        }
    }
}
