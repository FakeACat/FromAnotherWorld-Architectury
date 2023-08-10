package mod.acats.fromanotherworld.entity.render.projectile;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.projectile.NeedleEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NeedleEntityRenderer extends ArrowRenderer<NeedleEntity> {

    public NeedleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(NeedleEntity entity) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/projectile/needle.png");
    }
}
