package mod.acats.fromanotherworld.fabric;

import mod.acats.fromanotherlibrary.registry.client.ClientRegistryFabric;
import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.fabric.registry.ClientEventRegistryFabric;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class FromAnotherWorldClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientRegistryFabric.registerClient(new FromAnotherWorld());

        ClientEventRegistryFabric.register();

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.TENTACLE.get(), RenderType.cutout()); // note to self - forge does not handle this with code, must be done in the model json
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.DISGUISED_TENDRILS.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ASSIMILATED_SCULK_VEIN.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ASSIMILATED_SCULK_ACTIVATOR.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ASSIMILATED_SCULK_OVERGROWTH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ASSIMILATED_SCULK_BRAMBLES.get(), RenderType.cutout());
    }
}
