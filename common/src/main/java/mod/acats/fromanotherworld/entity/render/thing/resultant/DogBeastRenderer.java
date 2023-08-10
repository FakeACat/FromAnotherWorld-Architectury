package mod.acats.fromanotherworld.entity.render.thing.resultant;

import mod.acats.fromanotherworld.entity.model.thing.resultant.DogBeastModel;
import mod.acats.fromanotherworld.entity.render.thing.ResizeableThingRenderer;
import mod.acats.fromanotherworld.entity.thing.resultant.DogBeast;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DogBeastRenderer extends ResizeableThingRenderer<DogBeast> {

    public DogBeastRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogBeastModel());
        this.shadowRadius = 0.5F;
    }
}
