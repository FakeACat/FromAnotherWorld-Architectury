package acats.fromanotherworld.entity.render.projectile;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.projectile.NeedleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class NeedleEntityRenderer extends ProjectileEntityRenderer<NeedleEntity> {

    public NeedleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(NeedleEntity entity) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/projectile/needle.png");
    }
}
