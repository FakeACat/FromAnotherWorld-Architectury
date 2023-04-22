package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin {
    @Final
    @Shadow private static Identifier SKIN;
    @Final
    @Shadow private SheepWoolEntityModel<SheepEntity> model;
    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V")
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, SheepEntity sheepEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!sheepEntity.isInvisible()){
            RevealedThingFeatureRenderer.renderFleshOverlay((DisguisedThing) sheepEntity, this.model, SKIN, matrixStack, vertexConsumerProvider, i, sheepEntity, f, g, h, j, k, l);
        }
    }
}
