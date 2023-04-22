package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(HorseMarkingFeatureRenderer.class)
public abstract class HorseMarkingFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    public HorseMarkingFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    @Final
    @Shadow private static Map<HorseMarking, Identifier> TEXTURES;

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V")
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HorseEntity horseEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if (!horseEntity.isInvisible()){
            Identifier identifier = TEXTURES.get(horseEntity.getMarking());
            RevealedThingFeatureRenderer.renderFleshOverlay((DisguisedThing) horseEntity, this.getContextModel(), identifier, matrixStack, vertexConsumerProvider, i, horseEntity, f, g, h, j, k, l);
        }
    }
}
