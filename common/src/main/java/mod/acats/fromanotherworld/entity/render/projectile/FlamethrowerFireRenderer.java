package mod.acats.fromanotherworld.entity.render.projectile;

import mod.acats.fromanotherworld.entity.projectile.FlamethrowerFire;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlamethrowerFireRenderer extends EntityRenderer<FlamethrowerFire> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/item/fire_charge.png");

    public FlamethrowerFireRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FlamethrowerFire entity) {
        return TEXTURE;
    }
}
