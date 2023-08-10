package mod.acats.fromanotherworld.forge.registry.client;

import mod.acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import mod.acats.fromanotherworld.block.entity.render.TunnelBlockEntityRenderer;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class BlockEntityRegistryClientForge {
    public static void registerClient(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new CorpseBlockEntityRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new TunnelBlockEntityRenderer());
    }
}
