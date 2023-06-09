package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepFurLayer.class)
public abstract class SheepFurLayerMixin {
    @Final
    @Shadow private static ResourceLocation SHEEP_FUR_LOCATION;
    @Final
    @Shadow private SheepFurModel<Sheep> model;
    @Inject(at = @At("TAIL"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V")
    private void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, Sheep sheepEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!sheepEntity.isInvisible()){
            RevealedThingFeatureRenderer.renderFleshOverlay((PossibleDisguisedThing) sheepEntity, this.model, SHEEP_FUR_LOCATION, matrixStack, vertexConsumerProvider, i, sheepEntity, f, g, h, j, k, l);
        }
    }
}
