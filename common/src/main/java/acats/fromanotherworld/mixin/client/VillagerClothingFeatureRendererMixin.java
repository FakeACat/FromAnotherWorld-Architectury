package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.resources.metadata.animation.VillagerMetaDataSection;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerProfessionLayer.class)
public abstract class VillagerClothingFeatureRendererMixin<T extends LivingEntity & VillagerDataHolder, M extends EntityModel<T> & VillagerHeadModel> extends RenderLayer<T, M> {
    public VillagerClothingFeatureRendererMixin(RenderLayerParent<T, M> context) {
        super(context);
    }
    @Shadow public abstract <K> VillagerMetaDataSection.Hat getHatData(Object2ObjectMap<K, VillagerMetaDataSection.Hat> hatLookUp, String keyType, DefaultedRegistry<K> registry, K key);
    @Shadow protected abstract ResourceLocation getResourceLocation(String keyType, ResourceLocation keyId);
    @Final @Shadow private Object2ObjectMap<VillagerType, VillagerMetaDataSection.Hat> typeHatCache;
    @Shadow @Final private Object2ObjectMap<VillagerProfession, VillagerMetaDataSection.Hat> professionHatCache;
    @Shadow @Final private static Int2ObjectMap<ResourceLocation> LEVEL_LOCATIONS;

    @Inject(at = @At("TAIL"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V")
    private void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!livingEntity.isInvisible()) {
            PossibleDisguisedThing thing = (PossibleDisguisedThing) livingEntity;
            VillagerData villagerData = livingEntity.getVillagerData();
            VillagerType villagerType = villagerData.getType();
            VillagerProfession villagerProfession = villagerData.getProfession();
            VillagerMetaDataSection.Hat hatType = this.getHatData(this.typeHatCache, "type", BuiltInRegistries.VILLAGER_TYPE, villagerType);
            VillagerMetaDataSection.Hat hatType2 = this.getHatData(this.professionHatCache, "profession", BuiltInRegistries.VILLAGER_PROFESSION, villagerProfession);
            M entityModel = this.getParentModel();
            entityModel.hatVisible(hatType2 == VillagerMetaDataSection.Hat.NONE || hatType2 == VillagerMetaDataSection.Hat.PARTIAL && hatType != VillagerMetaDataSection.Hat.FULL);
            ResourceLocation identifier = this.getResourceLocation("type", BuiltInRegistries.VILLAGER_TYPE.getKey(villagerType));
            RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
            entityModel.hatVisible(true);
            if (villagerProfession != VillagerProfession.NONE && !livingEntity.isBaby()) {
                ResourceLocation identifier2 = this.getResourceLocation("profession", BuiltInRegistries.VILLAGER_PROFESSION.getKey(villagerProfession));
                RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier2, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
                if (villagerProfession != VillagerProfession.NITWIT) {
                    ResourceLocation identifier3 = this.getResourceLocation("profession_level", LEVEL_LOCATIONS.get(Mth.clamp(villagerData.getLevel(), 1, LEVEL_LOCATIONS.size())));
                    RevealedThingFeatureRenderer.renderFleshOverlay(thing, entityModel, identifier3, matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
                }
            }
        }
    }
}
