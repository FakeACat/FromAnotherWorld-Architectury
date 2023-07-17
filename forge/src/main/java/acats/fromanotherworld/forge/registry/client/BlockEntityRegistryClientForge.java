package acats.fromanotherworld.forge.registry.client;

import acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class BlockEntityRegistryClientForge {
    public static void registerClient(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new CorpseBlockEntityRenderer());
    }
}
