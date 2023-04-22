package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.entity.render.feature.RevealedThingFeatureRenderer;
import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M>  {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Shadow protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

    @Inject(at = @At("HEAD"), method = "isShaking", cancellable = true)
    private void isShaking(T entity, CallbackInfoReturnable<Boolean> cir){
        if (((DisguisedThing) entity).getSupercellConcentration() >= 1.0F){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci){
        this.addFeature(new RevealedThingFeatureRenderer<>(this, ctx.getModelLoader().getModelPart(EntityRegistry.spiderLegsModelLayer)));
    }
}
