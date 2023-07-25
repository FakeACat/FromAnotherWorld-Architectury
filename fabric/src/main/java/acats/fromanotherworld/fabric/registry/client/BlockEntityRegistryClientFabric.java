package acats.fromanotherworld.fabric.registry.client;

import acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import acats.fromanotherworld.block.entity.render.TunnelBlockEntityRenderer;
import acats.fromanotherworld.registry.BlockEntityRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class BlockEntityRegistryClientFabric {

    public static void clientRegister(){
        BlockEntityRenderers.register(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new CorpseBlockEntityRenderer());
        BlockEntityRenderers.register(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), (blockEntityRendererProvider) -> new TunnelBlockEntityRenderer());
    }
}
