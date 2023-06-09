package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;

@Mixin(HorseMarkingLayer.class)
public abstract class HorseMarkingFeatureRendererMixin extends RenderLayer<Horse, HorseModel<Horse>> {
    public HorseMarkingFeatureRendererMixin(RenderLayerParent<Horse, HorseModel<Horse>> context) {
        super(context);
    }

    @Final
    @Shadow private static Map<Markings, ResourceLocation> LOCATION_BY_MARKINGS;

    @Inject(at = @At("TAIL"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V")
    private void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, Horse horseEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!horseEntity.isInvisible()){
            ResourceLocation identifier = LOCATION_BY_MARKINGS.get(horseEntity.getMarkings());
            RevealedThingFeatureRenderer.renderFleshOverlay((PossibleDisguisedThing) horseEntity, this.getParentModel(), identifier, matrixStack, vertexConsumerProvider, i, horseEntity, f, g, h, j, k, l);
        }
    }
}
