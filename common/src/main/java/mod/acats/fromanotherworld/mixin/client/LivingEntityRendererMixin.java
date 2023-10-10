package mod.acats.fromanotherworld.mixin.client;

import mod.acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import mod.acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import mod.acats.fromanotherworld.registry.client.ClientEntityRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M>  {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Shadow protected abstract boolean addLayer(RenderLayer<T, M> feature);

    @Inject(at = @At("HEAD"), method = "isShaking", cancellable = true)
    private void isShaking(T entity, CallbackInfoReturnable<Boolean> cir){
        if (((PossibleDisguisedThing) entity).faw$getSupercellConcentration() >= 1.0F){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context ctx, M model, float shadowRadius, CallbackInfo ci){
        this.addLayer(new RevealedThingFeatureRenderer<>(this, ctx.getModelSet().bakeLayer(ClientEntityRegistry.SPIDER_LEGS_MODEL_LAYER)));
    }
}
