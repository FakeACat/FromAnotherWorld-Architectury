package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.ImpalerEntityModel;
import acats.fromanotherworld.entity.thing.resultant.ImpalerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ImpalerEntityRenderer extends AbsorberThingEntityRenderer<ImpalerEntity> {
    public ImpalerEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ImpalerEntityModel());
        this.shadowRadius = 0.5F;
    }
}
